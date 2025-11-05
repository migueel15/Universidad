"""
Implementación de Codificación Aritmética
Compresión de imágenes por canales RGB
Usa aritmética entera para mayor precisión
"""

import numpy as np
from PIL import Image
from typing import Dict, Tuple, List
import pickle
from collections import Counter


class ArithmeticEncoder:
    """
    Encoder de Codificación Aritmética para imágenes RGB.
    Trabaja por canales independientes usando aritmética entera.
    """
    
    def __init__(self):
        self.models = {}  # Modelos de probabilidad por canal
        # Constantes para aritmética entera
        self.CODE_VALUE_BITS = 32
        self.TOP_VALUE = (1 << self.CODE_VALUE_BITS) - 1
        self.FIRST_QTR = self.TOP_VALUE // 4 + 1
        self.HALF = 2 * self.FIRST_QTR
        self.THIRD_QTR = 3 * self.FIRST_QTR
    
    def build_frequency_model(self, data: np.ndarray) -> Dict[int, int]:
        """
        Construye el modelo de frecuencias.
        
        Args:
            data: Array numpy del canal (valores 0-255)
            
        Returns:
            Diccionario {símbolo: frecuencia}
        """
        flat_data = data.flatten()
        frequencies = dict(Counter(flat_data))
        return frequencies
    
    def build_cumulative_freq(self, frequencies: Dict[int, int]) -> Tuple[Dict[int, int], int]:
        """
        Construye tabla de frecuencias acumuladas.
        
        Args:
            frequencies: Diccionario {símbolo: frecuencia}
            
        Returns:
            Tupla (cumulative_freq, total)
        """
        symbols = sorted(frequencies.keys())
        cumulative = {}
        total = 0
        
        for symbol in symbols:
            cumulative[symbol] = total
            total += frequencies[symbol]
        
        return cumulative, total
    
    def encode_channel(self, data: np.ndarray, channel_name: str) -> List[int]:
        """
        Codifica un canal usando codificación aritmética con enteros.
        
        Args:
            data: Array numpy del canal (0-255)
            channel_name: Nombre del canal ('R', 'G', 'B')
            
        Returns:
            Lista de bits (0s y 1s) codificados
        """
        # Construir modelo
        frequencies = self.build_frequency_model(data)
        cumulative_freq, total_freq = self.build_cumulative_freq(frequencies)
        
        # Guardar modelo
        self.models[channel_name] = {
            'frequencies': frequencies,
            'cumulative': cumulative_freq,
            'total': total_freq
        }
        
        # Inicializar codificador
        low = 0
        high = self.TOP_VALUE
        pending_bits = 0
        output_bits = []
        
        # Procesar cada símbolo
        flat_data = data.flatten()
        
        for symbol in flat_data:
            # Calcular nuevo rango
            range_size = high - low + 1
            high = low + (range_size * (cumulative_freq[symbol] + frequencies[symbol])) // total_freq - 1
            low = low + (range_size * cumulative_freq[symbol]) // total_freq
            
            # Normalización y output de bits
            while True:
                if high < self.HALF:
                    # Output 0
                    output_bits.append(0)
                    for _ in range(pending_bits):
                        output_bits.append(1)
                    pending_bits = 0
                elif low >= self.HALF:
                    # Output 1
                    output_bits.append(1)
                    for _ in range(pending_bits):
                        output_bits.append(0)
                    pending_bits = 0
                    low -= self.HALF
                    high -= self.HALF
                elif low >= self.FIRST_QTR and high < self.THIRD_QTR:
                    # Incrementar pending bits
                    pending_bits += 1
                    low -= self.FIRST_QTR
                    high -= self.FIRST_QTR
                else:
                    break
                
                # Escalar
                low = 2 * low
                high = 2 * high + 1
        
        # Flush final
        pending_bits += 1
        if low < self.FIRST_QTR:
            output_bits.append(0)
            for _ in range(pending_bits):
                output_bits.append(1)
        else:
            output_bits.append(1)
            for _ in range(pending_bits):
                output_bits.append(0)
        
        return output_bits
    
    def encode_image(self, image: Image.Image) -> Tuple[bytes, dict]:
        """
        Codifica una imagen RGB completa.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Tupla (datos_comprimidos_bytes, metadata)
        """
        img_array = np.array(image)
        
        print("Codificando canal R...")
        bits_r = self.encode_channel(img_array[:, :, 0], 'R')
        
        print("Codificando canal G...")
        bits_g = self.encode_channel(img_array[:, :, 1], 'G')
        
        print("Codificando canal B...")
        bits_b = self.encode_channel(img_array[:, :, 2], 'B')
        
        # Concatenar bits
        all_bits = bits_r + bits_g + bits_b
        
        # Convertir lista de bits a string
        bits_string = ''.join(str(b) for b in all_bits)
        
        # Añadir padding
        padding = (8 - len(bits_string) % 8) % 8
        bits_string += '0' * padding
        
        # Convertir a bytes
        compressed_bytes = int(bits_string, 2).to_bytes(len(bits_string) // 8, byteorder='big')
        
        # Metadata
        metadata = {
            'width': image.width,
            'height': image.height,
            'channels': 3,
            'padding': padding,
            'len_r': len(bits_r),
            'len_g': len(bits_g),
            'len_b': len(bits_b),
            'models': self.models
        }
        
        return compressed_bytes, metadata
    
    def decode_channel(self, bits: List[int], model: dict, num_pixels: int) -> np.ndarray:
        """
        Decodifica un canal.
        
        Args:
            bits: Lista de bits codificados
            model: Modelo de frecuencias
            num_pixels: Número de píxeles a decodificar
            
        Returns:
            Array numpy con valores decodificados
        """
        frequencies = model['frequencies']
        cumulative_freq = model['cumulative']
        total_freq = model['total']
        
        # Crear mapeo inverso
        symbols_list = sorted(frequencies.keys())
        
        # Inicializar decodificador
        low = 0
        high = self.TOP_VALUE
        value = 0
        
        # Leer bits iniciales
        bit_index = 0
        for i in range(self.CODE_VALUE_BITS):
            value = 2 * value
            if bit_index < len(bits):
                value += bits[bit_index]
                bit_index += 1
        
        # Decodificar símbolos
        decoded = []
        
        for _ in range(num_pixels):
            # Encontrar símbolo
            range_size = high - low + 1
            scaled_value = ((value - low + 1) * total_freq - 1) // range_size
            
            # Buscar símbolo correspondiente
            symbol = None
            for s in symbols_list:
                if cumulative_freq[s] <= scaled_value < cumulative_freq[s] + frequencies[s]:
                    symbol = s
                    break
            
            if symbol is None:
                # Si no encontramos símbolo, usar el último
                symbol = symbols_list[-1]
            
            decoded.append(symbol)
            
            # Actualizar rango
            high = low + (range_size * (cumulative_freq[symbol] + frequencies[symbol])) // total_freq - 1
            low = low + (range_size * cumulative_freq[symbol]) // total_freq
            
            # Normalización
            while True:
                if high < self.HALF:
                    # No hacer nada
                    pass
                elif low >= self.HALF:
                    value -= self.HALF
                    low -= self.HALF
                    high -= self.HALF
                elif low >= self.FIRST_QTR and high < self.THIRD_QTR:
                    value -= self.FIRST_QTR
                    low -= self.FIRST_QTR
                    high -= self.FIRST_QTR
                else:
                    break
                
                # Escalar
                low = 2 * low
                high = 2 * high + 1
                value = 2 * value
                if bit_index < len(bits):
                    value += bits[bit_index]
                    bit_index += 1
        
        return np.array(decoded, dtype=np.uint8)
    
    def decode_image(self, compressed_bytes: bytes, metadata: dict) -> Image.Image:
        """
        Decodifica una imagen comprimida.
        
        Args:
            compressed_bytes: Datos comprimidos
            metadata: Metadata con información de la compresión
            
        Returns:
            Imagen PIL RGB decodificada
        """
        # Convertir bytes a bits
        all_bits_string = bin(int.from_bytes(compressed_bytes, byteorder='big'))[2:]
        all_bits_string = all_bits_string.zfill(len(compressed_bytes) * 8)
        
        # Quitar padding
        if metadata['padding'] > 0:
            all_bits_string = all_bits_string[:-metadata['padding']]
        
        # Convertir a lista de enteros
        all_bits = [int(b) for b in all_bits_string]
        
        # Separar bits por canal
        len_r = metadata['len_r']
        len_g = metadata['len_g']
        len_b = metadata['len_b']
        
        bits_r = all_bits[:len_r]
        bits_g = all_bits[len_r:len_r + len_g]
        bits_b = all_bits[len_r + len_g:len_r + len_g + len_b]
        
        # Decodificar cada canal
        width = metadata['width']
        height = metadata['height']
        num_pixels = width * height
        
        print("Decodificando canal R...")
        channel_r = self.decode_channel(bits_r, metadata['models']['R'], num_pixels)
        
        print("Decodificando canal G...")
        channel_g = self.decode_channel(bits_g, metadata['models']['G'], num_pixels)
        
        print("Decodificando canal B...")
        channel_b = self.decode_channel(bits_b, metadata['models']['B'], num_pixels)
        
        # Reconstruir imagen
        channel_r = channel_r.reshape(height, width)
        channel_g = channel_g.reshape(height, width)
        channel_b = channel_b.reshape(height, width)
        
        img_array = np.stack([channel_r, channel_g, channel_b], axis=2)
        
        return Image.fromarray(img_array)
    
    def save_compressed(self, compressed_bytes: bytes, metadata: dict, filename: str):
        """
        Guarda imagen comprimida con su metadata.
        """
        with open(f"{filename}.arith", 'wb') as f:
            f.write(compressed_bytes)
        
        with open(f"{filename}.arith.meta", 'wb') as f:
            pickle.dump(metadata, f)
        
        print(f"✓ Comprimido guardado: {filename}.arith ({len(compressed_bytes):,} bytes)")
        print(f"✓ Metadata guardada: {filename}.arith.meta")
    
    def load_compressed(self, filename: str) -> Tuple[bytes, dict]:
        """
        Carga imagen comprimida con su metadata.
        """
        if filename.endswith('.arith'):
            filename = filename[:-6]
        
        with open(f"{filename}.arith", 'rb') as f:
            compressed_bytes = f.read()
        
        with open(f"{filename}.arith.meta", 'rb') as f:
            metadata = pickle.load(f)
        
        return compressed_bytes, metadata


def test_arithmetic():
    """Función de prueba de la codificación aritmética"""
    print("=== Test de Codificación Aritmética ===\n")
    
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
    encoder.save_compressed(compressed_bytes, metadata, 'test_arithmetic')
    
    # Descomprimir
    print("\nDescomprimiendo...")
    img_decoded = encoder.decode_image(compressed_bytes, metadata)
    
    # Verificar
    decoded_bytes = gen.get_image_bytes(img_decoded)
    son_iguales = original_bytes == decoded_bytes
    
    print(f"\nVerificación: {'✓ CORRECTO - Descompresión perfecta' if son_iguales else '✗ ERROR'}")
    
    # Guardar imágenes para comparar visualmente
    img.save('test_arithmetic_original.png')
    img_decoded.save('test_arithmetic_decoded.png')
    print("✓ Imágenes guardadas para comparación visual")


if __name__ == "__main__":
    test_arithmetic()
