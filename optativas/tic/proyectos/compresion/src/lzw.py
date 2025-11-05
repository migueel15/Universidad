"""
Implementación de LZW (Lempel-Ziv-Welch)
Compresión de imágenes usando diccionario dinámico
"""

import numpy as np
from PIL import Image
from typing import List, Tuple
import pickle


class LZWEncoder:
    """
    Encoder LZW que trabaja sobre la secuencia completa de bytes RGB.
    Construye un diccionario dinámico durante la compresión.
    """
    
    def __init__(self, max_dict_size: int = 4096):
        """
        Inicializa el encoder LZW.
        
        Args:
            max_dict_size: Tamaño máximo del diccionario (por defecto 2^12 = 4096)
        """
        self.max_dict_size = max_dict_size
    
    def encode(self, data: bytes) -> List[int]:
        """
        Codifica datos usando LZW.
        
        Args:
            data: Bytes a comprimir
            
        Returns:
            Lista de códigos (enteros)
        """
        # Inicializar diccionario con todos los bytes posibles (0-255)
        dictionary = {bytes([i]): i for i in range(256)}
        dict_size = 256
        
        # Variables para el encoding
        current_sequence = b""
        encoded = []
        
        for byte in data:
            byte_seq = bytes([byte])
            combined = current_sequence + byte_seq
            
            if combined in dictionary:
                # La secuencia combinada ya está en el diccionario
                current_sequence = combined
            else:
                # Output el código de la secuencia actual
                encoded.append(dictionary[current_sequence])
                
                # Añadir nueva secuencia al diccionario si hay espacio
                if dict_size < self.max_dict_size:
                    dictionary[combined] = dict_size
                    dict_size += 1
                
                # Empezar nueva secuencia
                current_sequence = byte_seq
        
        # Output el último código
        if current_sequence:
            encoded.append(dictionary[current_sequence])
        
        return encoded
    
    def decode(self, encoded: List[int]) -> bytes:
        """
        Decodifica datos usando LZW.
        
        Args:
            encoded: Lista de códigos
            
        Returns:
            Bytes decodificados
        """
        # Inicializar diccionario con todos los bytes posibles
        dictionary = {i: bytes([i]) for i in range(256)}
        dict_size = 256
        
        # Obtener primer código
        decoded = bytearray()
        
        if not encoded:
            return bytes(decoded)
        
        previous_code = encoded[0]
        decoded.extend(dictionary[previous_code])
        
        for code in encoded[1:]:
            if code in dictionary:
                # Código está en el diccionario
                entry = dictionary[code]
            elif code == dict_size:
                # Caso especial: código no está todavía en el diccionario
                entry = dictionary[previous_code] + dictionary[previous_code][:1]
            else:
                raise ValueError(f"Código inválido: {code}")
            
            decoded.extend(entry)
            
            # Añadir nueva entrada al diccionario si hay espacio
            if dict_size < self.max_dict_size:
                dictionary[dict_size] = dictionary[previous_code] + entry[:1]
                dict_size += 1
            
            previous_code = code
        
        return bytes(decoded)
    
    def codes_to_bytes(self, codes: List[int]) -> bytes:
        """
        Convierte lista de códigos a bytes usando longitud variable.
        
        Args:
            codes: Lista de códigos
            
        Returns:
            Bytes comprimidos
        """
        # Calcular bits necesarios para representar el código máximo
        max_code = max(codes) if codes else 0
        bits_per_code = max(9, max_code.bit_length())
        
        # Convertir códigos a bits
        all_bits = ''
        for code in codes:
            all_bits += format(code, f'0{bits_per_code}b')
        
        # Añadir padding
        padding = (8 - len(all_bits) % 8) % 8
        all_bits += '0' * padding
        
        # Convertir a bytes
        compressed_bytes = int(all_bits, 2).to_bytes(len(all_bits) // 8, byteorder='big')
        
        return compressed_bytes, bits_per_code, padding
    
    def bytes_to_codes(self, compressed_bytes: bytes, bits_per_code: int, 
                      padding: int, num_codes: int) -> List[int]:
        """
        Convierte bytes de vuelta a lista de códigos.
        
        Args:
            compressed_bytes: Bytes comprimidos
            bits_per_code: Bits usados por código
            padding: Bits de padding añadidos
            num_codes: Número de códigos originales
            
        Returns:
            Lista de códigos
        """
        # Convertir a bits
        all_bits = bin(int.from_bytes(compressed_bytes, byteorder='big'))[2:]
        all_bits = all_bits.zfill(len(compressed_bytes) * 8)
        
        # Quitar padding
        if padding > 0:
            all_bits = all_bits[:-padding]
        
        # Extraer códigos
        codes = []
        for i in range(0, len(all_bits), bits_per_code):
            if i + bits_per_code <= len(all_bits):
                code_bits = all_bits[i:i + bits_per_code]
                code = int(code_bits, 2)
                codes.append(code)
                if len(codes) == num_codes:
                    break
        
        return codes
    
    def encode_image(self, image: Image.Image) -> Tuple[bytes, dict]:
        """
        Codifica una imagen RGB completa usando LZW.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Tupla (datos_comprimidos_bytes, metadata)
        """
        # Obtener bytes de la imagen
        img_array = np.array(image)
        img_bytes = img_array.tobytes()
        
        print(f"Codificando {len(img_bytes):,} bytes con LZW...")
        
        # Codificar con LZW
        codes = self.encode(img_bytes)
        
        print(f"Generados {len(codes):,} códigos")
        
        # Convertir códigos a bytes
        compressed_bytes, bits_per_code, padding = self.codes_to_bytes(codes)
        
        # Metadata
        metadata = {
            'width': image.width,
            'height': image.height,
            'channels': 3,
            'original_size': len(img_bytes),
            'num_codes': len(codes),
            'bits_per_code': bits_per_code,
            'padding': padding,
            'max_dict_size': self.max_dict_size
        }
        
        return compressed_bytes, metadata
    
    def decode_image(self, compressed_bytes: bytes, metadata: dict) -> Image.Image:
        """
        Decodifica una imagen comprimida con LZW.
        
        Args:
            compressed_bytes: Datos comprimidos
            metadata: Metadata con información de la compresión
            
        Returns:
            Imagen PIL RGB decodificada
        """
        print(f"Decodificando {len(compressed_bytes):,} bytes...")
        
        # Convertir bytes a códigos
        codes = self.bytes_to_codes(
            compressed_bytes,
            metadata['bits_per_code'],
            metadata['padding'],
            metadata['num_codes']
        )
        
        print(f"Recuperados {len(codes):,} códigos")
        
        # Decodificar con LZW
        decoded_bytes = self.decode(codes)
        
        # Verificar tamaño
        if len(decoded_bytes) != metadata['original_size']:
            print(f"⚠ Advertencia: tamaño decodificado ({len(decoded_bytes)}) != original ({metadata['original_size']})")
        
        # Reconstruir imagen
        img_array = np.frombuffer(decoded_bytes[:metadata['original_size']], dtype=np.uint8)
        img_array = img_array.reshape(metadata['height'], metadata['width'], metadata['channels'])
        
        return Image.fromarray(img_array)
    
    def save_compressed(self, compressed_bytes: bytes, metadata: dict, filename: str):
        """
        Guarda imagen comprimida con su metadata.
        
        Args:
            compressed_bytes: Datos comprimidos
            metadata: Metadata de la compresión
            filename: Nombre del archivo (sin extensión)
        """
        # Guardar bytes comprimidos
        with open(f"{filename}.lzw", 'wb') as f:
            f.write(compressed_bytes)
        
        # Guardar metadata
        with open(f"{filename}.lzw.meta", 'wb') as f:
            pickle.dump(metadata, f)
        
        print(f"✓ Comprimido guardado: {filename}.lzw ({len(compressed_bytes):,} bytes)")
        print(f"✓ Metadata guardada: {filename}.lzw.meta")
    
    def load_compressed(self, filename: str) -> Tuple[bytes, dict]:
        """
        Carga imagen comprimida con su metadata.
        
        Args:
            filename: Nombre del archivo (sin extensión o con .lzw)
            
        Returns:
            Tupla (compressed_bytes, metadata)
        """
        if filename.endswith('.lzw'):
            filename = filename[:-4]
        
        # Cargar bytes comprimidos
        with open(f"{filename}.lzw", 'rb') as f:
            compressed_bytes = f.read()
        
        # Cargar metadata
        with open(f"{filename}.lzw.meta", 'rb') as f:
            metadata = pickle.load(f)
        
        return compressed_bytes, metadata


def test_lzw():
    """Función de prueba del algoritmo LZW"""
    print("=== Test de LZW ===\n")
    
    # Importar generador
    from generator import ImageGenerator
    
    # Generar imagen de prueba
    gen = ImageGenerator(width=2048, height=2048)
    print("Generando imagen con entropías: R=2.0, G=4.0, B=6.0")
    img = gen.generate_image(entropy_r=2.0, entropy_g=4.0, entropy_b=6.0, seed=42)
    
    # Obtener tamaño original
    original_bytes = gen.get_image_bytes(img)
    original_size = len(original_bytes)
    print(f"Tamaño original: {original_size:,} bytes\n")
    
    # Crear encoder y comprimir
    encoder = LZWEncoder(max_dict_size=4096)
    compressed_bytes, metadata = encoder.encode_image(img)
    compressed_size = len(compressed_bytes)
    
    print(f"\nTamaño comprimido: {compressed_size:,} bytes")
    print(f"Ratio de compresión: {original_size/compressed_size:.2f}x")
    print(f"Reducción: {(1 - compressed_size/original_size)*100:.1f}%")
    print(f"Bits por código: {metadata['bits_per_code']}")
    print(f"Número de códigos: {metadata['num_codes']:,}")
    print(f"Tamaño diccionario usado: {min(metadata['num_codes'] + 256, encoder.max_dict_size)}")
    
    # Guardar comprimido
    print("\nGuardando archivo comprimido...")
    encoder.save_compressed(compressed_bytes, metadata, 'test_lzw')
    
    # Descomprimir
    print("\nDescomprimiendo...")
    img_decoded = encoder.decode_image(compressed_bytes, metadata)
    
    # Verificar
    decoded_bytes = gen.get_image_bytes(img_decoded)
    son_iguales = original_bytes == decoded_bytes
    
    print(f"\nVerificación: {'✓ CORRECTO - Descompresión perfecta' if son_iguales else '✗ ERROR'}")
    
    # Guardar imágenes para comparar visualmente
    img.save('test_lzw_original.png')
    img_decoded.save('test_lzw_decoded.png')
    print("✓ Imágenes guardadas para comparación visual")


if __name__ == "__main__":
    test_lzw()
