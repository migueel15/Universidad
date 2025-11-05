"""
Implementación de Huffman por Bloques (Algoritmo Inventivo)
Divide la imagen en chunks y aplica Huffman a cada chunk independientemente
"""

import numpy as np
from PIL import Image
from typing import Dict, Tuple, List, Optional
import pickle
from collections import Counter
import heapq


class HuffmanNode:
    """Nodo del árbol de Huffman"""
    
    def __init__(self, symbol: Optional[int] = None, frequency: int = 0, 
                 left=None, right=None):
        self.symbol = symbol
        self.frequency = frequency
        self.left = left
        self.right = right
    
    def __lt__(self, other):
        return self.frequency < other.frequency
    
    def is_leaf(self):
        return self.left is None and self.right is None


class HuffmanBlockEncoder:
    """
    Encoder de Huffman por bloques que divide la imagen en chunks.
    Cada chunk tiene su propio árbol de Huffman optimizado localmente.
    """
    
    def __init__(self, block_size: int = 16):
        """
        Inicializa el encoder.
        
        Args:
            block_size: Tamaño del bloque (block_size x block_size píxeles)
        """
        self.block_size = block_size
        self.trees = {}  # Árboles por bloque y canal
        self.codes = {}  # Códigos por bloque y canal
    
    def build_frequency_table(self, data: np.ndarray) -> Dict[int, int]:
        """Construye tabla de frecuencias"""
        flat_data = data.flatten()
        return dict(Counter(flat_data))
    
    def build_huffman_tree(self, frequencies: Dict[int, int]) -> HuffmanNode:
        """Construye el árbol de Huffman"""
        heap = []
        for symbol, freq in frequencies.items():
            node = HuffmanNode(symbol=symbol, frequency=freq)
            heapq.heappush(heap, node)
        
        if len(heap) == 1:
            node = heapq.heappop(heap)
            root = HuffmanNode(frequency=node.frequency, left=node)
            return root
        
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
        """Genera los códigos de Huffman"""
        if codes is None:
            codes = {}
        
        if root is None:
            return codes
        
        if root.is_leaf():
            codes[root.symbol] = code if code else "0"
            return codes
        
        self.generate_codes(root.left, code + "0", codes)
        self.generate_codes(root.right, code + "1", codes)
        
        return codes
    
    def split_into_blocks(self, channel: np.ndarray) -> List[Tuple[int, int, np.ndarray]]:
        """
        Divide un canal en bloques.
        
        Args:
            channel: Array 2D del canal (height x width)
            
        Returns:
            Lista de tuplas (row_idx, col_idx, bloque)
        """
        height, width = channel.shape
        blocks = []
        
        for i in range(0, height, self.block_size):
            for j in range(0, width, self.block_size):
                # Extraer bloque (puede ser más pequeño en los bordes)
                block = channel[i:i+self.block_size, j:j+self.block_size]
                blocks.append((i, j, block))
        
        return blocks
    
    def encode_block(self, block: np.ndarray, block_id: str) -> Tuple[str, HuffmanNode, Dict[int, str]]:
        """
        Codifica un bloque individual con Huffman.
        
        Args:
            block: Array 2D del bloque
            block_id: Identificador único del bloque
            
        Returns:
            Tupla (bits_codificados, árbol, códigos)
        """
        # Construir tabla de frecuencias del bloque
        frequencies = self.build_frequency_table(block)
        
        # Si el bloque es constante (un solo valor)
        if len(frequencies) == 1:
            symbol = list(frequencies.keys())[0]
            tree = HuffmanNode(symbol=symbol, frequency=frequencies[symbol])
            codes = {symbol: "0"}
            encoded_bits = "0" * block.size
            return encoded_bits, tree, codes
        
        # Construir árbol de Huffman específico para este bloque
        tree = self.build_huffman_tree(frequencies)
        codes = self.generate_codes(tree)
        
        # Codificar el bloque
        flat_block = block.flatten()
        encoded_bits = ''.join(codes[pixel] for pixel in flat_block)
        
        return encoded_bits, tree, codes
    
    def encode_channel(self, channel: np.ndarray, channel_name: str) -> Tuple[List[str], dict]:
        """
        Codifica un canal completo dividido en bloques.
        
        Args:
            channel: Array 2D del canal
            channel_name: Nombre del canal ('R', 'G', 'B')
            
        Returns:
            Tupla (lista_de_bits_por_bloque, metadata_bloques)
        """
        blocks = self.split_into_blocks(channel)
        encoded_blocks = []
        blocks_metadata = {}
        
        print(f"  Codificando {len(blocks)} bloques...")
        
        for idx, (row, col, block) in enumerate(blocks):
            block_id = f"{channel_name}_{row}_{col}"
            
            encoded_bits, tree, codes = self.encode_block(block, block_id)
            encoded_blocks.append(encoded_bits)
            
            # Guardar árbol y códigos del bloque
            self.trees[block_id] = tree
            self.codes[block_id] = codes
            
            # Metadata del bloque
            blocks_metadata[idx] = {
                'position': (row, col),
                'shape': block.shape,
                'bits_length': len(encoded_bits),
                'tree_id': block_id
            }
        
        return encoded_blocks, blocks_metadata
    
    def encode_image(self, image: Image.Image) -> Tuple[bytes, dict]:
        """
        Codifica una imagen RGB completa usando Huffman por bloques.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Tupla (datos_comprimidos_bytes, metadata)
        """
        img_array = np.array(image)
        
        print("Codificando canal R...")
        encoded_r, metadata_r = self.encode_channel(img_array[:, :, 0], 'R')
        
        print("Codificando canal G...")
        encoded_g, metadata_g = self.encode_channel(img_array[:, :, 1], 'G')
        
        print("Codificando canal B...")
        encoded_b, metadata_b = self.encode_channel(img_array[:, :, 2], 'B')
        
        # Concatenar todos los bits
        all_bits = ''.join(encoded_r) + ''.join(encoded_g) + ''.join(encoded_b)
        
        # Añadir padding
        padding = (8 - len(all_bits) % 8) % 8
        all_bits += '0' * padding
        
        # Convertir a bytes
        compressed_bytes = int(all_bits, 2).to_bytes(len(all_bits) // 8, byteorder='big')
        
        # Metadata
        metadata = {
            'width': image.width,
            'height': image.height,
            'channels': 3,
            'block_size': self.block_size,
            'padding': padding,
            'blocks_metadata': {
                'R': metadata_r,
                'G': metadata_g,
                'B': metadata_b
            },
            'trees': self.trees,
            'codes': self.codes,
            'total_bits': {
                'R': sum(len(bits) for bits in encoded_r),
                'G': sum(len(bits) for bits in encoded_g),
                'B': sum(len(bits) for bits in encoded_b)
            }
        }
        
        return compressed_bytes, metadata
    
    def decode_block(self, bits: str, tree: HuffmanNode, block_shape: Tuple[int, int]) -> np.ndarray:
        """
        Decodifica un bloque usando su árbol de Huffman.
        
        Args:
            bits: Cadena de bits del bloque
            tree: Árbol de Huffman del bloque
            block_shape: Forma del bloque (height, width)
            
        Returns:
            Array numpy del bloque decodificado
        """
        num_pixels = block_shape[0] * block_shape[1]
        decoded = []
        current = tree
        
        for bit in bits:
            if bit == '0':
                current = current.left
            else:
                current = current.right
            
            if current.is_leaf():
                decoded.append(current.symbol)
                current = tree
                
                if len(decoded) == num_pixels:
                    break
        
        decoded_array = np.array(decoded, dtype=np.uint8)
        return decoded_array.reshape(block_shape)
    
    def decode_channel(self, bits: str, blocks_metadata: dict, 
                      channel_shape: Tuple[int, int], channel_name: str) -> np.ndarray:
        """
        Decodifica un canal completo desde bloques.
        
        Args:
            bits: Cadena de bits del canal
            blocks_metadata: Metadata de los bloques
            channel_shape: Forma del canal (height, width)
            channel_name: Nombre del canal
            
        Returns:
            Array numpy del canal decodificado
        """
        # Crear array vacío para el canal
        channel = np.zeros(channel_shape, dtype=np.uint8)
        
        # Decodificar cada bloque
        bit_offset = 0
        
        for idx in sorted(blocks_metadata.keys()):
            block_meta = blocks_metadata[idx]
            row, col = block_meta['position']
            block_shape = block_meta['shape']
            bits_length = block_meta['bits_length']
            tree_id = block_meta['tree_id']
            
            # Extraer bits del bloque
            block_bits = bits[bit_offset:bit_offset + bits_length]
            bit_offset += bits_length
            
            # Decodificar bloque
            tree = self.trees[tree_id]
            decoded_block = self.decode_block(block_bits, tree, block_shape)
            
            # Colocar bloque en el canal
            channel[row:row+block_shape[0], col:col+block_shape[1]] = decoded_block
        
        return channel
    
    def decode_image(self, compressed_bytes: bytes, metadata: dict) -> Image.Image:
        """
        Decodifica una imagen comprimida con Huffman por bloques.
        
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
        len_r = metadata['total_bits']['R']
        len_g = metadata['total_bits']['G']
        len_b = metadata['total_bits']['B']
        
        bits_r = all_bits[:len_r]
        bits_g = all_bits[len_r:len_r + len_g]
        bits_b = all_bits[len_r + len_g:len_r + len_g + len_b]
        
        # Restaurar árboles
        self.trees = metadata['trees']
        
        # Decodificar cada canal
        width = metadata['width']
        height = metadata['height']
        
        print("Decodificando canal R...")
        channel_r = self.decode_channel(bits_r, metadata['blocks_metadata']['R'], 
                                       (height, width), 'R')
        
        print("Decodificando canal G...")
        channel_g = self.decode_channel(bits_g, metadata['blocks_metadata']['G'], 
                                       (height, width), 'G')
        
        print("Decodificando canal B...")
        channel_b = self.decode_channel(bits_b, metadata['blocks_metadata']['B'], 
                                       (height, width), 'B')
        
        # Reconstruir imagen
        img_array = np.stack([channel_r, channel_g, channel_b], axis=2)
        
        return Image.fromarray(img_array)
    
    def save_compressed(self, compressed_bytes: bytes, metadata: dict, filename: str):
        """Guarda imagen comprimida con su metadata"""
        with open(f"{filename}.hufblk", 'wb') as f:
            f.write(compressed_bytes)
        
        with open(f"{filename}.hufblk.meta", 'wb') as f:
            pickle.dump(metadata, f)
        
        print(f"✓ Comprimido guardado: {filename}.hufblk ({len(compressed_bytes):,} bytes)")
        print(f"✓ Metadata guardada: {filename}.hufblk.meta")
    
    def load_compressed(self, filename: str) -> Tuple[bytes, dict]:
        """Carga imagen comprimida con su metadata"""
        if filename.endswith('.hufblk'):
            filename = filename[:-7]
        
        with open(f"{filename}.hufblk", 'rb') as f:
            compressed_bytes = f.read()
        
        with open(f"{filename}.hufblk.meta", 'rb') as f:
            metadata = pickle.load(f)
        
        return compressed_bytes, metadata


def test_huffman_blocks():
    """Función de prueba de Huffman por bloques"""
    print("=== Test de Huffman por Bloques ===\n")
    
    # Importar generador
    from generator import ImageGenerator
    
    # Generar imagen de prueba
    gen = ImageGenerator(width=2048, height=2048)
    print("Generando imagen con entropías: R=8.0, G=4.0, B=6.0")
    img = gen.generate_image(entropy_r=2.0, entropy_g=4.0, entropy_b=6.0, seed=42)
    gen.save_raw(img,"test_raw.bin")
    
    # Obtener tamaño original
    original_bytes = gen.get_image_bytes(img)
    original_size = len(original_bytes)
    print(f"Tamaño original: {original_size:,} bytes\n")
    
    # Probar con diferentes tamaños de bloque
    # block_sizes = [8, 16, 32, 64]
    block_sizes = [128]

    # for block_size in block_sizes:
    #     print(f"\n{'='*60}")
    #     print(f"Probando con bloques de {block_size}x{block_size}")
    #     print('='*60)
    #
    #     # Crear encoder y comprimir
    #     encoder = HuffmanBlockEncoder(block_size=block_size)
    #     compressed_bytes, metadata = encoder.encode_image(img)
    #     compressed_size = len(compressed_bytes)
    #
    #     print(f"\nTamaño comprimido: {compressed_size:,} bytes")
    #     print(f"Ratio de compresión: {original_size/compressed_size:.2f}x")
    #     print(f"Reducción: {(1 - compressed_size/original_size)*100:.1f}%")
    #
    #     # Información de bloques
    #     num_blocks_per_channel = len(metadata['blocks_metadata']['R'])
    #     total_blocks = num_blocks_per_channel * 3
    #     print(f"Bloques por canal: {num_blocks_per_channel}")
    #     print(f"Total de bloques: {total_blocks}")
    #     print(f"Árboles de Huffman creados: {len(metadata['trees'])}")
    
    # Probar compresión y descompresión completa con tamaño óptimo
    print(f"\n{'='*60}")
    print(f"Test completo con bloques de {block_sizes[0]}")
    print('='*60)
    
    encoder = HuffmanBlockEncoder(block_size=block_sizes[0])
    compressed_bytes, metadata = encoder.encode_image(img)
    compressed_size = len(compressed_bytes)
    
    print(f"\nTamaño comprimido: {compressed_size:,} bytes")
    print(f"Ratio de compresión: {original_size/compressed_size:.2f}x")
    
    # Guardar comprimido
    print("\nGuardando archivo comprimido...")
    encoder.save_compressed(compressed_bytes, metadata, 'test_huffman_blocks')
    
    # Descomprimir
    print("\nDescomprimiendo...")
    img_decoded = encoder.decode_image(compressed_bytes, metadata)
    
    # Verificar
    decoded_bytes = gen.get_image_bytes(img_decoded)
    son_iguales = original_bytes == decoded_bytes
    
    print(f"\nVerificación: {'✓ CORRECTO - Descompresión perfecta' if son_iguales else '✗ ERROR'}")
    
    # Guardar imágenes para comparar visualmente
    img.save('test_huffman_blocks_original.png')
    img_decoded.save('test_huffman_blocks_decoded.png')
    print("✓ Imágenes guardadas para comparación visual")


if __name__ == "__main__":
    test_huffman_blocks()
