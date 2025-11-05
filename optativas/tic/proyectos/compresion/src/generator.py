"""
Generador de imágenes con entropía controlable
Fuente Emisora - Proyecto Compresión de Imágenes
"""

import numpy as np
from PIL import Image
from typing import Tuple, Optional
import math
import json
import os


class ImageGenerator:
    """
    Clase para generar imágenes con niveles de entropía controlables
    por canal (R, G, B) de forma independiente.
    """
    
    def __init__(self, width: int = 256, height: int = 256):
        """
        Inicializa el generador de imágenes.
        
        Args:
            width: Ancho de la imagen en píxeles
            height: Alto de la imagen en píxeles
        """
        self.width = width
        self.height = height
    
    def calculate_entropy(self, data: np.ndarray) -> float:
        """
        Calcula la entropía de Shannon H de un canal de imagen.
        
        H = -Σ p(x) * log2(p(x))
        
        Args:
            data: Array numpy con los datos del canal (valores 0-255)
            
        Returns:
            Entropía en bits por símbolo
        """
        # Aplanar el array si es 2D
        flat_data = data.flatten()
        
        # Calcular histograma (frecuencias de cada valor 0-255)
        hist, _ = np.histogram(flat_data, bins=256, range=(0, 256))
        
        # Calcular probabilidades (eliminar ceros para evitar log(0))
        probabilities = hist[hist > 0] / len(flat_data)
        
        # Calcular entropía
        entropy = -np.sum(probabilities * np.log2(probabilities))
        
        return entropy
    
    def generate_channel_with_entropy(self, target_entropy: float, seed: Optional[int] = None) -> np.ndarray:
        """
        Genera un canal de imagen con una entropía objetivo específica.
        
        Args:
            target_entropy: Entropía objetivo (0.0 a 8.0 bits)
                          - 0.0: Un solo valor (entropía mínima)
                          - 8.0: Ruido uniforme (entropía máxima)
            seed: Semilla para reproducibilidad
            
        Returns:
            Array numpy de forma (height, width) con valores 0-255
        """
        if seed is not None:
            np.random.seed(seed)
        
        # Limitar entropía al rango válido
        target_entropy = max(0.0, min(8.0, target_entropy))
        
        # Calcular número de valores únicos necesarios
        # H = log2(n) para distribución uniforme
        num_unique_values = int(2 ** target_entropy)
        num_unique_values = max(1, min(256, num_unique_values))
        
        if target_entropy == 0.0:
            # Entropía mínima: imagen constante
            channel = np.full((self.height, self.width), 128, dtype=np.uint8)
        
        elif target_entropy >= 7.9:  # Casi 8.0
            # Entropía máxima: ruido uniforme puro
            channel = np.random.randint(0, 256, size=(self.height, self.width), dtype=np.uint8)
        
        else:
            # Entropía intermedia: distribución controlada
            # Seleccionar valores uniformemente distribuidos en el rango 0-255
            values = np.linspace(0, 255, num_unique_values, dtype=np.uint8)
            
            # Generar imagen seleccionando aleatoriamente de estos valores
            indices = np.random.randint(0, len(values), size=(self.height, self.width))
            channel = values[indices]
        
        return channel
    
    def generate_image(self, 
                      entropy_r: float, 
                      entropy_g: float, 
                      entropy_b: float,
                      seed: Optional[int] = None) -> Image.Image:
        """
        Genera una imagen RGB con entropías controlables por canal.
        
        Args:
            entropy_r: Entropía objetivo para el canal Rojo (0.0 - 8.0)
            entropy_g: Entropía objetivo para el canal Verde (0.0 - 8.0)
            entropy_b: Entropía objetivo para el canal Azul (0.0 - 8.0)
            seed: Semilla para reproducibilidad
            
        Returns:
            Imagen PIL RGB
        """
        # Generar cada canal con su entropía específica
        if seed is not None:
            np.random.seed(seed)
        
        channel_r = self.generate_channel_with_entropy(entropy_r, seed)
        channel_g = self.generate_channel_with_entropy(entropy_g, seed + 1 if seed else None)
        channel_b = self.generate_channel_with_entropy(entropy_b, seed + 2 if seed else None)
        
        # Combinar canales en imagen RGB
        img_array = np.stack([channel_r, channel_g, channel_b], axis=2)
        
        # Convertir a imagen PIL
        image = Image.fromarray(img_array, mode='RGB')
        
        return image
    
    def get_image_stats(self, image: Image.Image) -> dict:
        """Comparar tamaños:
- Original: 196,608 bytes
- Huffman: X bytes → ratio, bpp, eficiencia
- Aritmética: Y bytes → ratio, bpp, eficiencia
- LZW: Z bytes → ratio, bpp, eficiencia
        Calcula estadísticas de una imagen, incluyendo entropía por canal.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Diccionario con estadísticas de cada canal
        """
        img_array = np.array(image)
        
        stats = {
            'R': {
                'entropy': self.calculate_entropy(img_array[:, :, 0]),
                'mean': np.mean(img_array[:, :, 0]),
                'std': np.std(img_array[:, :, 0]),
                'unique_values': len(np.unique(img_array[:, :, 0]))
            },
            'G': {
                'entropy': self.calculate_entropy(img_array[:, :, 1]),
                'mean': np.mean(img_array[:, :, 1]),
                'std': np.std(img_array[:, :, 1]),
                'unique_values': len(np.unique(img_array[:, :, 1]))
            },
            'B': {
                'entropy': self.calculate_entropy(img_array[:, :, 2]),
                'mean': np.mean(img_array[:, :, 2]),
                'std': np.std(img_array[:, :, 2]),
                'unique_values': len(np.unique(img_array[:, :, 2]))
            },
            'image_size': {
                'width': image.width,
                'height': image.height,
                'total_pixels': image.width * image.height
            }
        }
        
        return stats
    
    def get_image_bytes(self, image: Image.Image) -> bytes:
        """
        Obtiene los bytes RGB sin comprimir de una imagen.
        
        Args:
            image: Imagen PIL RGB
            
        Returns:
            Bytes del array RGB (height * width * 3 bytes)
        """
        img_array = np.array(image)
        return img_array.tobytes()
    
    def bytes_to_image(self, data: bytes, width: int, height: int) -> Image.Image:
        """
        Convierte bytes RAW de vuelta a imagen PIL.
        
        Args:
            data: Bytes del array RGB
            width: Ancho de la imagen
            height: Alto de la imagen
            
        Returns:
            Imagen PIL RGB
        """
        img_array = np.frombuffer(data, dtype=np.uint8).reshape(height, width, 3)
        return Image.fromarray(img_array)
    
    def save_raw(self, image: Image.Image, filename: str):
        """
        Guarda imagen como archivo RAW binario con metadata JSON separada.
        
        Crea dos archivos:
        - filename.raw: datos binarios puros
        - filename.raw.json: metadata (dimensiones, entropías, etc.)
        
        Args:
            image: Imagen PIL RGB a guardar
            filename: Nombre del archivo (sin extensión o con .raw)
        """
        # Asegurar que el filename termina en .raw
        if not filename.endswith('.raw'):
            filename = filename + '.raw'
        
        # Obtener datos y estadísticas
        img_array = np.array(image)
        stats = self.get_image_stats(image)
        
        # Guardar bytes RAW
        img_array.tofile(filename)
        
        # Crear metadata
        metadata = {
            'width': image.width,
            'height': image.height,
            'channels': 3,
            'dtype': 'uint8',
            'total_bytes': img_array.nbytes,
            'entropy_R': stats['R']['entropy'],
            'entropy_G': stats['G']['entropy'],
            'entropy_B': stats['B']['entropy'],
        }
        
        # Guardar metadata como JSON
        metadata_filename = filename + '.json'
        with open(metadata_filename, 'w') as f:
            json.dump(metadata, f, indent=2)
        
        print(f"✓ Imagen RAW guardada: {filename} ({metadata['total_bytes']:,} bytes)")
        print(f"✓ Metadata guardada: {metadata_filename}")
    
    def load_raw(self, filename: str) -> Image.Image:
        """
        Carga imagen desde archivo RAW usando su metadata JSON.
        
        Args:
            filename: Nombre del archivo RAW (con o sin extensión .raw)
            
        Returns:
            Imagen PIL RGB
        """
        # Asegurar que el filename termina en .raw
        if not filename.endswith('.raw'):
            filename = filename + '.raw'
        
        # Cargar metadata
        metadata_filename = filename + '.json'
        if not os.path.exists(metadata_filename):
            raise FileNotFoundError(f"No se encontró el archivo de metadata: {metadata_filename}")
        
        with open(metadata_filename, 'r') as f:
            metadata = json.load(f)
        
        # Cargar bytes RAW
        img_array = np.fromfile(filename, dtype=np.uint8)
        
        # Reshape según metadata
        img_array = img_array.reshape(metadata['height'], metadata['width'], metadata['channels'])
        
        # Convertir a imagen PIL
        image = Image.fromarray(img_array)
        
        print(f"✓ Imagen RAW cargada: {filename}")
        print(f"  Dimensiones: {metadata['width']}x{metadata['height']}")
        print(f"  Entropías: R={metadata['entropy_R']:.2f}, G={metadata['entropy_G']:.2f}, B={metadata['entropy_B']:.2f}")
        
        return image

    def load_image_from_file(self, filename: str, resize_to: Optional[Tuple[int, int]] = None) -> Image.Image:
        """
        Carga una imagen desde cualquier formato (PNG, JPEG, BMP, etc.).
        
        Args:
            filename: Ruta del archivo de imagen
            resize_to: Opcional, tupla (width, height) para redimensionar
            
        Returns:
            Imagen PIL RGB
        """
        # Cargar imagen
        img = Image.open(filename)
        
        # Convertir a RGB si es necesario (por si es RGBA, escala de grises, etc.)
        if img.mode != 'RGB':
            print(f"  Convirtiendo de {img.mode} a RGB")
            img = img.convert('RGB')
        
        original_size = img.size
        
        # Redimensionar si se especifica
        if resize_to:
            img = img.resize(resize_to, Image.Resampling.LANCZOS)
            print(f"✓ Imagen cargada: {filename}")
            print(f"  Dimensiones originales: {original_size[0]}x{original_size[1]}")
            print(f"  Redimensionada a: {resize_to[0]}x{resize_to[1]}")
        else:
            print(f"✓ Imagen cargada: {filename}")
            print(f"  Dimensiones: {original_size[0]}x{original_size[1]}")
        
        # Mostrar estadísticas
        stats = self.get_image_stats(img)
        print(f"  Entropías: R={stats['R']['entropy']:.2f}, G={stats['G']['entropy']:.2f}, B={stats['B']['entropy']:.2f}")
        
        return img


# Función auxiliar para pruebas rápidas
def test_generator():
    """Función de prueba del generador"""
    print("=== Test del Generador de Imágenes ===\n")
    
    # Crear generador
    gen = ImageGenerator(width=256, height=256)
    
    # Generar imagen con diferentes entropías
    print("Generando imagen con entropías: R=2.0, G=4.0, B=7.0")
    img = gen.generate_image(entropy_r=0.0, entropy_g=0.0, entropy_b=0.0, seed=42)
    
    # Obtener estadísticas
    stats = gen.get_image_stats(img)
    
    # Mostrar resultados
    for channel in ['R', 'G', 'B']:
        print(f"\nCanal {channel}:")
        print(f"  Entropía: {stats[channel]['entropy']:.4f} bits")
        print(f"  Media: {stats[channel]['mean']:.2f}")
        print(f"  Desv. estándar: {stats[channel]['std']:.2f}")
        print(f"  Valores únicos: {stats[channel]['unique_values']}")
    
    # Test nuevos métodos
    print("\n" + "="*50)
    print("=== Test de Métodos RAW ===\n")
    
    # 1. Obtener bytes
    img_bytes = gen.get_image_bytes(img)
    print(f"1. Bytes obtenidos: {len(img_bytes):,} bytes")
    
    # 2. Guardar como RAW con metadata
    print("\n2. Guardando como RAW...")
    gen.save_raw(img, 'test_image.raw')
    
    # 3. Cargar desde RAW
    print("\n3. Cargando desde RAW...")
    img_loaded = gen.load_raw('test_image.raw')
    
    # 4. Verificar que son iguales
    img_bytes_loaded = gen.get_image_bytes(img_loaded)
    son_iguales = img_bytes == img_bytes_loaded
    print(f"\n4. Verificación: {'✓ CORRECTO - Las imágenes son idénticas' if son_iguales else '✗ ERROR'}")
    
    # 5. Guardar también como PNG para visualización
    img.save('test_image.png')
    print(f"\n5. ✓ Imagen también guardada como 'test_image.png' para visualización")


if __name__ == "__main__":
    test_generator()
