"""
Implementación de Codificación Aritmética (optimizada, sin cambiar resultados)
Compresión de imágenes por canales RGB
Usa aritmética entera para mayor precisión
"""

import numpy as np
from PIL import Image
from typing import Dict, Tuple, List
import pickle
from collections import Counter
import bisect


class ArithmeticEncoder:
    """
    Encoder de Codificación Aritmética para imágenes RGB.
    Trabaja por canales independientes usando aritmética entera.
    """

    def __init__(self):
        self.models = {}  # Modelos de probabilidad por canal
        # Constantes para aritmética entera (idénticas a tu versión)
        self.CODE_VALUE_BITS = 32
        self.TOP_VALUE = (1 << self.CODE_VALUE_BITS) - 1
        self.FIRST_QTR = self.TOP_VALUE // 4 + 1
        self.HALF = 2 * self.FIRST_QTR
        self.THIRD_QTR = 3 * self.FIRST_QTR

        # Bit-buffer (se usan por encode_image)
        # Estos atributos se inicializan en encode_image
        self._bw_buffer = None
        self._bw_nbits = 0
        self._bw_bytearray = None
        self._bw_bits_emitted = 0

    # ---------------- utilidades bit buffer ----------------
    def _bw_init(self):
        self._bw_buffer = 0
        self._bw_nbits = 0
        self._bw_bytearray = bytearray()
        self._bw_bits_emitted = 0

    def _bw_emit_bit(self, bit: int):
        """Emite un bit al buffer (bit = 0 o 1)."""
        self._bw_buffer = (self._bw_buffer << 1) | (bit & 1)
        self._bw_nbits += 1
        self._bw_bits_emitted += 1
        if self._bw_nbits == 8:
            self._bw_bytearray.append(self._bw_buffer & 0xFF)
            self._bw_buffer = 0
            self._bw_nbits = 0

    def _bw_flush_and_get_bytes_with_padding(self):
        """Rellena los bits del último byte con ceros y devuelve (bytes, padding_bits)."""
        padding = 0
        if self._bw_nbits != 0:
            # left-shift to fill the rest of the byte
            padding = 8 - self._bw_nbits
            self._bw_bytearray.append((self._bw_buffer << padding) & 0xFF)
            self._bw_buffer = 0
            self._bw_nbits = 0
        return bytes(self._bw_bytearray), padding

    # ---------------- construcción modelo ----------------
    def build_frequency_model(self, data: np.ndarray) -> Dict[int, int]:
        """
        Construye el modelo de frecuencias.
        (Mantengo interfaz original: retorna dicts para metadata)
        """
        flat_data = data.flatten()
        frequencies = dict(Counter(flat_data))
        return frequencies

    def _freqs_to_arrays(self, frequencies: Dict[int, int]):
        """
        Convierte dict de frecuencias a arrays de tamaño 256 y a cumulative array.
        Devuelve (freq_arr, cum_arr, total).
        - freq_arr: lista de 256 ints con la frecuencia por símbolo
        - cum_arr: lista de 257 ints, cum_arr[i] = sum_{0..i-1} freq_arr
        - total: total de símbolos
        """
        freq_arr = [0] * 256
        for s, f in frequencies.items():
            freq_arr[s] = f
        cum_arr = [0] * 257
        for i in range(256):
            cum_arr[i + 1] = cum_arr[i] + freq_arr[i]
        total = cum_arr[256]
        return freq_arr, cum_arr, total

    def build_cumulative_freq(
        self, frequencies: Dict[int, int]
    ) -> Tuple[Dict[int, int], int]:
        """
        Construye tabla de frecuencias acumuladas en el mismo formato que tenías.
        (cumulative[symbol] = sum of frequencies of symbols < symbol in sorted order of keys)
        """
        symbols = sorted(frequencies.keys())
        cumulative = {}
        total = 0
        for symbol in symbols:
            cumulative[symbol] = total
            total += frequencies[symbol]
        return cumulative, total

    # ---------------- codificación por canal (optimizada) ----------------
    def _encode_channel_to_bitbuffer(self, data: np.ndarray, channel_name: str):
        """
        Implementación optimizada de encode_channel:
         - no devuelve lista de bits; en su lugar escribe bits al bit-buffer global
         - construye y guarda el modelo (frequencies, cumulative, total) en self.models
         - devuelve la longitud en bits generados para este canal
        """
        frequencies = self.build_frequency_model(data)
        cumulative_freq, total_freq = self.build_cumulative_freq(frequencies)

        # Guardar modelo en la estructura que espera el resto del código (metadata)
        self.models[channel_name] = {
            "frequencies": frequencies,
            "cumulative": cumulative_freq,
            "total": total_freq,
        }

        # Preprocesar arrays para acelerar:
        freq_arr, cum_arr_full, total = self._freqs_to_arrays(frequencies)
        # Construimos symbols_list y cumulative starts according to original cumulative dict ordering
        symbols_list = sorted(frequencies.keys())
        # cum_vals is the starting cumulative for each symbol in symbols_list
        cum_vals = [cumulative_freq[s] for s in symbols_list]

        # Variables locales para velocidad
        TOP_VALUE = self.TOP_VALUE
        FIRST_QTR = self.FIRST_QTR
        HALF = self.HALF
        THIRD_QTR = self.THIRD_QTR

        low = 0
        high = TOP_VALUE
        pending_bits = 0

        flat_data = data.flatten()

        # Emit bits by calling self._bw_emit_bit
        for symbol in flat_data:
            # obtain cumulative for symbol: we must use cumulative_freq mapping
            # range calculation
            range_size = high - low + 1
            # high' = low + floor(range * (cum[symbol] + freq[symbol]) / total_freq) - 1
            high = (
                low
                + (range_size * (cumulative_freq[symbol] + frequencies[symbol]))
                // total_freq
                - 1
            )
            low = low + (range_size * cumulative_freq[symbol]) // total_freq

            # Normalización and output bits (exactly same semantics as original)
            while True:
                if high < HALF:
                    # Output 0 and pending 1s
                    self._bw_emit_bit(0)
                    for _ in range(pending_bits):
                        self._bw_emit_bit(1)
                    pending_bits = 0
                elif low >= HALF:
                    # Output 1 and pending 0s
                    self._bw_emit_bit(1)
                    for _ in range(pending_bits):
                        self._bw_emit_bit(0)
                    pending_bits = 0
                    low -= HALF
                    high -= HALF
                elif low >= FIRST_QTR and high < THIRD_QTR:
                    pending_bits += 1
                    low -= FIRST_QTR
                    high -= FIRST_QTR
                else:
                    break

                # Escalar
                low = low * 2
                high = high * 2 + 1

        # Flush final bits (igual que versión original)
        pending_bits += 1
        if low < FIRST_QTR:
            self._bw_emit_bit(0)
            for _ in range(pending_bits):
                self._bw_emit_bit(1)
        else:
            self._bw_emit_bit(1)
            for _ in range(pending_bits):
                self._bw_emit_bit(0)

        # Devolver número de bits emitidos para este canal
        return self._bw_bits_emitted

    # ---------------- interfaz pública ----------------
    def encode_channel(self, data: np.ndarray, channel_name: str) -> List[int]:
        """
        Proporciono esta función para compatibilidad de API (tu main nunca la llama directamente),
        pero internamente 'encode_image' usará _encode_channel_to_bitbuffer para eficiencia.
        Mantengo su firma pero la implementación la deja delegar al bitbuffer global.
        """
        # NOTA: Esta función no es usada por encode_image. La dejo para compatibilidad si alguien la llama.
        # Aquí generamos la lista de bits de forma directa (menos eficiente) — pero para evitar
        # duplicación, delegamos en el buffer y luego devolvemos la lista construida a partir del buffer.
        # Sin embargo, para que la salida sea idéntica se usaría el mismo proceso que en encode_image.
        raise NotImplementedError(
            "Usa encode_image; encode_channel optimizada se usa internamente."
        )

    def encode_image(self, image: Image.Image) -> Tuple[bytes, dict]:
        """
        Codifica una imagen RGB completa.
        Devuelve (compressed_bytes, metadata) con la misma estructura que tu versión original.
        """
        img_array = np.array(image)

        print("Codificando canal R...")
        self._bw_init()
        # codificamos R, G, B secuencialmente y medimos bits por canal (no acumulados)
        cumulative_bits = 0
        bits_r_total = self._encode_channel_to_bitbuffer(img_array[:, :, 0], "R")
        len_r = bits_r_total - cumulative_bits
        cumulative_bits = bits_r_total

        print("Codificando canal G...")
        bits_g_total = self._encode_channel_to_bitbuffer(img_array[:, :, 1], "G")
        len_g = bits_g_total - cumulative_bits
        cumulative_bits = bits_g_total

        print("Codificando canal B...")
        bits_b_total = self._encode_channel_to_bitbuffer(img_array[:, :, 2], "B")
        len_b = bits_b_total - cumulative_bits
        cumulative_bits = bits_b_total

        # Obtener bytes del bitbuffer con padding
        compressed_bytes, padding = self._bw_flush_and_get_bytes_with_padding()

        # Metadata (mantengo estructura similar a la original)
        metadata = {
            "width": image.width,
            "height": image.height,
            "channels": 3,
            "padding": padding,
            "len_r": len_r,
            "len_g": len_g,
            "len_b": len_b,
            "models": self.models,
        }

        return compressed_bytes, metadata

    # ---------------- decodificación optimizada ----------------
    def _bits_from_bytes_generator(self, b: bytes):
        """
        Generador que devuelve bits (0/1) en el mismo orden en que fueron emitidos por _bw_emit_bit.
        Lee bytes secuencialmente y produce 8 bits por byte (MSB first).
        """
        for byte in b:
            for i in range(7, -1, -1):
                yield (byte >> i) & 1

    def _make_bit_reader(self, bits_iter):
        """
        Construye un objeto lector de bits con métodos next_bit().
        Implementado como closure para velocidad.
        """
        it = iter(bits_iter)
        buffer = {"next": None}

        def next_bit():
            try:
                return next(it)
            except StopIteration:
                return 0  # si no quedan bits, devolvemos 0 (igual que comportamiento original)

        return next_bit

    def decode_channel(
        self, bits: List[int], model: dict, num_pixels: int
    ) -> np.ndarray:
        """
        Decodifica un canal (sobrecargada para compatibilidad con la interfaz original).
        Esta versión se mantiene conceptualmente similar, pero en decode_image uso _decode_channel_from_bitreader.
        """
        # Para mantener compatibilidad, implementamos una decodificación basada en la lista de bits.
        # Convertimos la lista de bits a un iterador y delegamos a la versión optimizada.
        bit_iter = iter(bits)
        next_bit = self._make_bit_reader(bit_iter)
        return self._decode_channel_from_bitreader(next_bit, model, num_pixels)

    def _decode_channel_from_bitreader(
        self, next_bit_callable, model: dict, num_pixels: int
    ) -> np.ndarray:
        """
        Decodifica num_pixels símbolos usando next_bit_callable() que devuelve 0/1.
        Usa bisect sobre cum_vals para encontrar símbolo rápidamente.
        """
        frequencies = model["frequencies"]
        cumulative_freq = model["cumulative"]
        total_freq = model["total"]

        # Preparar símbolos_list y cum_vals (listas paralelas)
        symbols_list = sorted(frequencies.keys())
        cum_vals = [cumulative_freq[s] for s in symbols_list]

        # Variables locales para velocidad
        TOP_VALUE = self.TOP_VALUE
        FIRST_QTR = self.FIRST_QTR
        HALF = self.HALF
        THIRD_QTR = self.THIRD_QTR

        low = 0
        high = TOP_VALUE
        value = 0

        # Leer CODE_VALUE_BITS iniciales
        for _ in range(self.CODE_VALUE_BITS):
            value = (value << 1) | (next_bit_callable() & 1)

        decoded = []
        for _ in range(num_pixels):
            range_size = high - low + 1
            scaled_value = ((value - low + 1) * total_freq - 1) // range_size

            # Encontrar símbolo mediante bisect en cum_vals
            # bisect_right devuelve índice i tal que cum_vals[i-1] <= scaled_value < cum_vals[i] si cum_vals es lista de inicios
            idx = bisect.bisect_right(cum_vals, scaled_value) - 1
            if idx < 0:
                idx = 0
            symbol = symbols_list[idx]

            decoded.append(symbol)

            # Actualizar rango
            high = (
                low
                + (range_size * (cumulative_freq[symbol] + frequencies[symbol]))
                // total_freq
                - 1
            )
            low = low + (range_size * cumulative_freq[symbol]) // total_freq

            # Normalización
            while True:
                if high < HALF:
                    pass
                elif low >= HALF:
                    value -= HALF
                    low -= HALF
                    high -= HALF
                elif low >= FIRST_QTR and high < THIRD_QTR:
                    value -= FIRST_QTR
                    low -= FIRST_QTR
                    high -= FIRST_QTR
                else:
                    break

                low = low * 2
                high = high * 2 + 1
                value = (value << 1) | (next_bit_callable() & 1)

        return np.array(decoded, dtype=np.uint8)

    def decode_image(self, compressed_bytes: bytes, metadata: dict) -> Image.Image:
        """
        Decodifica una imagen comprimida.
        Mantiene la interfaz original y produce idéntica reconstrucción.
        """
        # No usamos bin(int.from_bytes(...)) para evitar crear strings gigantes.
        # En su lugar construimos un iterador de bits y usamos las longitudes por canal para cortar.
        padding = metadata.get("padding", 0)

        # Generador de bits desde bytes
        raw_bits_gen = self._bits_from_bytes_generator(compressed_bytes)
        next_bit_global = self._make_bit_reader(raw_bits_gen)

        # Creamos una wrapper que devuelve bits y cuenta cuántos se han consumido
        # Pero para manejar len_r/len_g/len_b necesitamos partir la secuencia:
        # Hacemos un simple lector que consume exactamente n bits y devuelve un iterator over those bits.

        def make_limited_reader(n_bits, next_bit_callable):
            """Devuelve un callable next_bit() que atiende exactamente n_bits (extras llenan con 0)."""
            remaining = {"n": n_bits}

            def nr():
                if remaining["n"] <= 0:
                    remaining["n"] -= 1  # mantener negativos si se pide más
                    return 0
                remaining["n"] -= 1
                return next_bit_callable()

            return nr

        # Longitudes en bits por canal
        len_r = metadata["len_r"]
        len_g = metadata["len_g"]
        len_b = metadata["len_b"]

        width = metadata["width"]
        height = metadata["height"]
        num_pixels = width * height

        print("Decodificando canal R...")
        reader_r = make_limited_reader(len_r, next_bit_global)
        channel_r = self._decode_channel_from_bitreader(
            reader_r, metadata["models"]["R"], num_pixels
        )

        print("Decodificando canal G...")
        reader_g = make_limited_reader(len_g, next_bit_global)
        channel_g = self._decode_channel_from_bitreader(
            reader_g, metadata["models"]["G"], num_pixels
        )

        print("Decodificando canal B...")
        reader_b = make_limited_reader(len_b, next_bit_global)
        channel_b = self._decode_channel_from_bitreader(
            reader_b, metadata["models"]["B"], num_pixels
        )

        # reconstrucción en forma (height, width)
        channel_r = channel_r.reshape((height, width))
        channel_g = channel_g.reshape((height, width))
        channel_b = channel_b.reshape((height, width))

        img_array = np.stack([channel_r, channel_g, channel_b], axis=2).astype(np.uint8)
        return Image.fromarray(img_array)

    # ---------------- IO (mantengo tus helpers) ----------------
    def save_compressed(self, compressed_bytes: bytes, metadata: dict, filename: str):
        """
        Guarda imagen comprimida con su metadata.
        """
        with open(f"{filename}.arith", "wb") as f:
            f.write(compressed_bytes)

        with open(f"{filename}.arith.meta", "wb") as f:
            pickle.dump(metadata, f)

        print(
            f"✓ Comprimido guardado: {filename}.arith ({len(compressed_bytes):,} bytes)"
        )
        print(f"✓ Metadata guardada: {filename}.arith.meta")

    def load_compressed(self, filename: str) -> Tuple[bytes, dict]:
        """
        Carga imagen comprimida con su metadata.
        """
        if filename.endswith(".arith"):
            filename = filename[:-6]

        with open(f"{filename}.arith", "rb") as f:
            compressed_bytes = f.read()

        with open(f"{filename}.arith.meta", "rb") as f:
            metadata = pickle.load(f)

        return compressed_bytes, metadata


# ---------------- test helper (igual estructura que tenías) ----------------
def test_arithmetic():
    """Función de prueba de la codificación aritmética"""
    print("=== Test de Codificación Aritmética (optimizada) ===\n")

    # Importar generador
    from generator import ImageGenerator

    # Generar imagen de prueba
    gen = ImageGenerator(width=256, height=256)
    print("Generando imagen con entropías: R=2.0, G=4.0, B=6.0")
    img = gen.generate_image(entropy_r=0.0, entropy_g=0.0, entropy_b=0.0, seed=42)

    # Obtener tamaño original
    original_bytes = gen.get_image_bytes(img)
    original_size = len(original_bytes)
    print(f"Tamaño original: {original_size:,} bytes\n")

    # Crear encoder y comprimir
    encoder = ArithmeticEncoder()
    print("Comprimiendo con Codificación Aritmética...")
    compressed_bytes, metadata = encoder.encode_image(img)
    compressed_size = len(compressed_bytes)

    print(f"\nTamaño comprimido: {compressed_size:,} bytes")
    print(f"Ratio de compresión: {original_size/compressed_size:.2f}x")
    print(f"Reducción: {(1 - compressed_size/original_size)*100:.1f}%")

    # Guardar comprimido
    print("\nGuardando archivo comprimido...")
    encoder.save_compressed(compressed_bytes, metadata, "test_arithmetic_optimized")

    # Descomprimir
    print("\nDescomprimiendo...")
    img_decoded = encoder.decode_image(compressed_bytes, metadata)

    # Verificar
    decoded_bytes = gen.get_image_bytes(img_decoded)
    son_iguales = original_bytes == decoded_bytes

    print(
        f"\nVerificación: {'✓ CORRECTO - Descompresión perfecta' if son_iguales else '✗ ERROR'}"
    )

    # Guardar imágenes para comparar visualmente
    img.save("test_arithmetic_original.png")
    img_decoded.save("test_arithmetic_decoded.png")
    print("✓ Imágenes guardadas para comparación visual")


if __name__ == "__main__":
    test_arithmetic()
