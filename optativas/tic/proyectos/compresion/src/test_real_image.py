"""
Script para probar algoritmos de compresión con imágenes reales
"""

from generator import ImageGenerator
from huffman_static import HuffmanStaticEncoder
from huffman_blocks import HuffmanBlockEncoder
from arithmetic_coding import ArithmeticEncoder
from lzw import LZWEncoder
import sys


def test_image(image_path: str, resize_to=(256, 256)):
    """
    Prueba todos los algoritmos de compresión con una imagen real.

    Args:
        image_path: Ruta a la imagen
        resize_to: Tamaño al que redimensionar (opcional)
    """
    print("=" * 70)
    print(f"ANALIZANDO IMAGEN: {image_path}")
    print("=" * 70)

    # Cargar imagen
    gen = ImageGenerator()
    img = gen.load_image_from_file(image_path, resize_to=resize_to)

    # Obtener tamaño original
    original_bytes = gen.get_image_bytes(img)
    original_size = len(original_bytes)

    print(
        f"\nTamaño sin comprimir: {original_size:,} bytes ({original_size/1024:.1f} KB)"
    )

    # Lista para resultados
    results = []

    # 1. Huffman Estático
    print("\n" + "-" * 70)
    print("1. Huffman Estático")
    print("-" * 70)
    try:
        encoder_huf = HuffmanStaticEncoder()
        compressed_huf, metadata_huf = encoder_huf.encode_image(img)
        size_huf = len(compressed_huf)
        ratio_huf = original_size / size_huf
        results.append(("Huffman Estático", size_huf, ratio_huf))
        print(f"✓ Comprimido: {size_huf:,} bytes")
        print(f"  Ratio: {ratio_huf:.2f}x")
        print(f"  Reducción: {(1 - size_huf/original_size)*100:.1f}%")
    except Exception as e:
        print(f"✗ Error: {e}")

    # 2. Codificación Aritmética
    print("\n" + "-" * 70)
    print("2. Codificación Aritmética")
    print("-" * 70)
    try:
        encoder_arith = ArithmeticEncoder()
        compressed_arith, metadata_arith = encoder_arith.encode_image(img)
        size_arith = len(compressed_arith)
        ratio_arith = original_size / size_arith
        results.append(("Aritmética", size_arith, ratio_arith))
        print(f"✓ Comprimido: {size_arith:,} bytes")
        print(f"  Ratio: {ratio_arith:.2f}x")
        print(f"  Reducción: {(1 - size_arith/original_size)*100:.1f}%")
    except Exception as e:
        print(f"✗ Error: {e}")

    # 3. LZW
    print("\n" + "-" * 70)
    print("3. LZW")
    print("-" * 70)
    try:
        encoder_lzw = LZWEncoder(max_dict_size=4096)
        compressed_lzw, metadata_lzw = encoder_lzw.encode_image(img)
        size_lzw = len(compressed_lzw)
        ratio_lzw = original_size / size_lzw
        results.append(("LZW", size_lzw, ratio_lzw))
        print(f"✓ Comprimido: {size_lzw:,} bytes")
        print(f"  Ratio: {ratio_lzw:.2f}x")
        print(f"  Reducción: {(1 - size_lzw/original_size)*100:.1f}%")
    except Exception as e:
        print(f"✗ Error: {e}")

    # 4. Huffman por Bloques (diferentes tamaños)
    print("\n" + "-" * 70)
    print("4. Huffman por Bloques")
    print("-" * 70)

    for block_size in [8, 16, 32]:
        try:
            print(f"\n  Bloques {block_size}x{block_size}:")
            encoder_blocks = HuffmanBlockEncoder(block_size=block_size)
            compressed_blocks, metadata_blocks = encoder_blocks.encode_image(img)
            size_blocks = len(compressed_blocks)
            ratio_blocks = original_size / size_blocks
            results.append(
                (
                    f"Huffman Bloques {block_size}x{block_size}",
                    size_blocks,
                    ratio_blocks,
                )
            )
            print(f"  ✓ Comprimido: {size_blocks:,} bytes")
            print(f"    Ratio: {ratio_blocks:.2f}x")
            print(f"    Reducción: {(1 - size_blocks/original_size)*100:.1f}%")
        except Exception as e:
            print(f"  ✗ Error: {e}")

    # Resumen comparativo
    print("\n" + "=" * 70)
    print("RESUMEN COMPARATIVO")
    print("=" * 70)

    # Ordenar por ratio (mejor primero)
    results.sort(key=lambda x: x[2], reverse=True)

    print(f"\n{'Algoritmo':<30} {'Tamaño':>15} {'Ratio':>10} {'Reducción':>12}")
    print("-" * 70)
    for name, size, ratio in results:
        reduction = (1 - size / original_size) * 100
        print(f"{name:<30} {size:>15,} {ratio:>9.2f}x {reduction:>11.1f}%")

    # Mejor algoritmo
    best_name, best_size, best_ratio = results[0]
    print("\n" + "=" * 70)
    print(f"🏆 MEJOR: {best_name}")
    print(f"   Ratio: {best_ratio:.2f}x | Tamaño: {best_size:,} bytes")
    print("=" * 70)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python3 test_real_image.py <ruta_imagen> [ancho] [alto]")
        print("\nEjemplo:")
        print("  python3 test_real_image.py mi_foto.jpg")
        print("  python3 test_real_image.py paisaje.png 256 256")
        print("\nPara probar, primero copia una imagen a este directorio:")
        print("  Puedes usar cualquier imagen JPEG, PNG, BMP, etc.")
        sys.exit(1)

    image_path = sys.argv[1]

    # Tamaño de redimensionado (opcional)
    if len(sys.argv) >= 4:
        width = int(sys.argv[2])
        height = int(sys.argv[3])
        resize_to = (width, height)
    else:
        resize_to = (256, 256)  # Por defecto

    test_image(image_path, resize_to)
