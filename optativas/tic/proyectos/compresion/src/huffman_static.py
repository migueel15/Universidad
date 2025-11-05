"""
Implementación de Huffman Estático
Compresión de imágenes por canales RGB
"""

import numpy as np
from PIL import Image
from typing import Dict, Tuple, Optional
import pickle
from collections import Counter
import heapq


class HuffmanNode:
    """Nodo del árbol de Huffman"""
    
    def __init__(self, symbol: Optional[int] = None, frequency: int = 0, 
                 left=None, right=None):
        self.symbol = symbol  # Valor del píxel (0-255) o None si es nodo interno
        self.frequency = frequency
        self.left = left
        self.right = right
    
    def __lt__(self, other):
        """Comparación para la cola de prioridad"""
        return self.frequency < other.frequency
    
    def is_leaf(self):
        """Verifica si es un nodo hoja"""
        return self.left is None and self.right is None


class HuffmanStaticEncoder:
    """
    Encoder de Huffman estático que trabaja por canales RGB.
    Cada canal tiene su propio árbol de Huffman.
    """
    
    def __init__(self):
        self.trees = {}  # Árboles de Huffman por canal (R, G, B)
        self.codes = {}  # Códigos de Huffman por canal
    
    def build_frequency_table(self, data: np.ndarray) -> Dict[int, int]:
        """
        Construye tabla de frecuencias de los valores en el canal.
        
        Args:
            data: Array numpy con valores del canal (0-255)
            
        Returns:
            Diccionario {valor: frecuencia}
        """
        flat_data = data.flatten()
        return dict(Counter(flat_data))
    
    def build_huffman_tree(self, frequencies: Dict[int, int]) -> HuffmanNode:
        """
        Construye el árbol de Huffman a partir de las frecuencias.
        
        Args:
            frequencies: Diccionario {símbolo: frecuencia}
            
        Returns:
            Raíz del árbol de Huffman
        """
        # Crear nodos hoja para cada símbolo
        heap = []
        for symbol, freq in frequencies.items():
            node = HuffmanNode(symbol=symbol, frequency=freq)
            heapq.heappush(heap, node)
        
        # Si solo hay un símbolo, crear árbol con un nodo adicional
        if len(heap) == 1:
            node = heapq.heappop(heap)
            root = HuffmanNode(frequency=node.frequency, left=node)
            return root
        
        # Construir árbol combinando nodos de menor frecuencia
        while len(heap) > 1:
            left = heapq.heappop(heap)
            right = heapq.heappop(heap)
            
            merged = HuffmanNode(
                frequency=left.frequency + right.frequency,
                left=left,
                right=right
            )
            heapq.heappush(heap, merged)
        
        return heap[0]
    
    def generate_codes(self, root: HuffmanNode, code: str = "", 
                      codes: Optional[Dict[int, str]] = None) -> Dict[int, str]:
        """
        Genera los códigos de Huffman recorriendo el árbol.
        
        Args:
            root: Raíz del árbol de Huffman
            code: Código actual (cadena de bits)
            codes: Diccionario de códigos acumulados
            
        Returns:
            Diccionario {símbolo: código_binario}
        """
        if codes is None:
            codes = {}
        
        if root is None:
            return codes
        
        # Si es hoja, guardar código
        if root.is_leaf():
            codes[root.symbol] = code if code else "0"
            return codes
        
        # Recorrer izquierda (0) y derecha (1)
        self.generate_codes(root.left, code + "0", codes)
        self.generate_codes(root.right, code + "1", codes)
        
        return codes
    
    def encode_channel(self, data: np.ndarray, channel_name: str) -> str:
        """
        Codifica un canal usando Huffman.
        
        Args:
            data: Array numpy del canal (0-255)
            channel_name: Nombre del canal ('R', 'G', 'B')
            
        Returns:
            Cadena de bits codificada
        """
        # Construir tabla de frecuencias
        frequencies = self.build_frequency_table(data)
        
        # Construir árbol de Huffman
        tree = self.build_huffman_tree(frequencies)
        self.trees[channel_name] = tree
        
        # Generar códigos
        codes = self.generate_codes(tree)
        self.codes[channel_name] = codes
        
        # Codificar datos
        flat_data = data.flatten()
        encoded_bits = ''.join(codes[pixel] for pixel in flat_data)
        
        return encoded_bits
    
    def encode_image(self, image: Image.Image) -> Tuple[bytes, dict]:
        """
        Codifica una imagen RGB completa usando Huffman por canal.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Tupla (datos_comprimidos_bytes, metadata)
        """
        img_array = np.array(image)
        
        # Codificar cada canal
        encoded_r = self.encode_channel(img_array[:, :, 0], 'R')
        encoded_g = self.encode_channel(img_array[:, :, 1], 'G')
        encoded_b = self.encode_channel(img_array[:, :, 2], 'B')
        
        # Concatenar todos los bits
        all_bits = encoded_r + encoded_g + encoded_b
        
        # Convertir bits a bytes
        # Añadir padding si es necesario
        padding = (8 - len(all_bits) % 8) % 8
        all_bits += '0' * padding
        
        # Convertir a bytes
        compressed_bytes = int(all_bits, 2).to_bytes(len(all_bits) // 8, byteorder='big')
        
        # Metadata necesaria para descomprimir
        metadata = {
            'width': image.width,
            'height': image.height,
            'channels': 3,
            'padding': padding,
            'len_r': len(encoded_r),
            'len_g': len(encoded_g),
            'len_b': len(encoded_b),
            'trees': self.trees,
            'codes': self.codes
        }
        
        return compressed_bytes, metadata
    
    def decode_channel(self, bits: str, tree: HuffmanNode, num_pixels: int) -> np.ndarray:
        """
        Decodifica un canal usando el árbol de Huffman.
        
        Args:
            bits: Cadena de bits codificada
            tree: Árbol de Huffman
            num_pixels: Número de píxeles a decodificar
            
        Returns:
            Array numpy con valores decodificados
        """
        decoded = []
        current = tree
        
        for bit in bits:
            # Navegar por el árbol
            if bit == '0':
                current = current.left
            else:
                current = current.right
            
            # Si llegamos a una hoja, guardar símbolo
            if current.is_leaf():
                decoded.append(current.symbol)
                current = tree
                
                # Si ya tenemos todos los píxeles, terminar
                if len(decoded) == num_pixels:
                    break
        
        return np.array(decoded, dtype=np.uint8)
    
    def decode_image(self, compressed_bytes: bytes, metadata: dict) -> Image.Image:
        """
        Decodifica una imagen comprimida con Huffman.
        
        Args:
            compressed_bytes: Datos comprimidos
            metadata: Metadata con información de la compresión
            
        Returns:
            Imagen PIL RGB decodificada
        """
        # Convertir bytes a bits
        all_bits = bin(int.from_bytes(compressed_bytes, byteorder='big'))[2:]
        all_bits = all_bits.zfill(len(compressed_bytes) * 8)
        
        # Quitar padding
        if metadata['padding'] > 0:
            all_bits = all_bits[:-metadata['padding']]
        
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
        
        channel_r = self.decode_channel(bits_r, metadata['trees']['R'], num_pixels)
        channel_g = self.decode_channel(bits_g, metadata['trees']['G'], num_pixels)
        channel_b = self.decode_channel(bits_b, metadata['trees']['B'], num_pixels)
        
        # Reconstruir imagen
        channel_r = channel_r.reshape(height, width)
        channel_g = channel_g.reshape(height, width)
        channel_b = channel_b.reshape(height, width)
        
        img_array = np.stack([channel_r, channel_g, channel_b], axis=2)
        
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
        with open(f"{filename}.huf", 'wb') as f:
            f.write(compressed_bytes)
        
        # Guardar metadata con pickle
        with open(f"{filename}.huf.meta", 'wb') as f:
            pickle.dump(metadata, f)
        
        print(f"✓ Comprimido guardado: {filename}.huf ({len(compressed_bytes):,} bytes)")
        print(f"✓ Metadata guardada: {filename}.huf.meta")
    
    def load_compressed(self, filename: str) -> Tuple[bytes, dict]:
        """
        Carga imagen comprimida con su metadata.
        
        Args:
            filename: Nombre del archivo (sin extensión o con .huf)
            
        Returns:
            Tupla (compressed_bytes, metadata)
        """
        if filename.endswith('.huf'):
            filename = filename[:-4]
        
        # Cargar bytes comprimidos
        with open(f"{filename}.huf", 'rb') as f:
            compressed_bytes = f.read()
        
        # Cargar metadata
        with open(f"{filename}.huf.meta", 'rb') as f:
            metadata = pickle.load(f)
        
        return compressed_bytes, metadata


def test_huffman():
    """Función de prueba del codificador Huffman"""
    print("=== Test de Huffman Estático ===\n")
    
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
    encoder = HuffmanStaticEncoder()
    print("Comprimiendo con Huffman...")
    compressed_bytes, metadata = encoder.encode_image(img)
    compressed_size = len(compressed_bytes)
    
    print(f"Tamaño comprimido: {compressed_size:,} bytes")
    print(f"Ratio de compresión: {original_size/compressed_size:.2f}x")
    print(f"Reducción: {(1 - compressed_size/original_size)*100:.1f}%")
    
    # Mostrar códigos por canal
    print("\nEstadísticas por canal:")
    for channel in ['R', 'G', 'B']:
        codes = encoder.codes[channel]
        avg_len = sum(len(code) for code in codes.values()) / len(codes)
        print(f"  Canal {channel}: {len(codes)} símbolos únicos, long. media código: {avg_len:.2f} bits")
    
    # Guardar comprimido
    print("\nGuardando archivo comprimido...")
    encoder.save_compressed(compressed_bytes, metadata, 'test_huffman')
    
    # Descomprimir
    print("\nDescomprimiendo...")
    img_decoded = encoder.decode_image(compressed_bytes, metadata)
    
    # Verificar
    decoded_bytes = gen.get_image_bytes(img_decoded)
    son_iguales = original_bytes == decoded_bytes
    
    print(f"Verificación: {'✓ CORRECTO - Descompresión perfecta' if son_iguales else '✗ ERROR'}")
    
    # Guardar imágenes para comparar visualmente
    img.save('test_huffman_original.png')
    img_decoded.save('test_huffman_decoded.png')
    print("\n✓ Imágenes guardadas para comparación visual")


if __name__ == "__main__":
    test_huffman()
