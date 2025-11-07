"""
Programa principal de compresión de imágenes
Analiza todos los algoritmos y genera archivos comprimidos para cada uno
"""

from generator import ImageGenerator
from huffman_static import HuffmanStaticEncoder
from huffman_blocks import HuffmanBlockEncoder
from arithmetic_coding import ArithmeticEncoder
from lzw import LZWEncoder
import argparse
import sys
import os


def compress_with_all_algorithms(img, base_filename="output"):
    """
    Comprime la imagen con todos los algoritmos disponibles y genera archivos.

    Args:
        img: Imagen PIL RGB
        base_filename: Nombre base para los archivos de salida

    Returns:
        Lista de tuplas (nombre_algoritmo, tamaño_bytes, ratio, archivo_bin)
    """
    gen = ImageGenerator()

    # Obtener tamaño original
    original_bytes = gen.get_image_bytes(img)
    original_size = len(original_bytes)

    # Guardar imagen raw
    raw_filename = f"{base_filename}_raw.bin"
    with open(raw_filename, "wb") as f:
        f.write(original_bytes)

    print(f"✓ Guardado: {raw_filename} ({original_size:,} bytes)")

    results = []

    # 1. Huffman Estático
    print("\nComprimiendo con Huffman Estático...")
    try:
        encoder_huf = HuffmanStaticEncoder()
        compressed_huf, metadata_huf = encoder_huf.encode_image(img)
        size_huf = len(compressed_huf)
        ratio_huf = original_size / size_huf

        # Guardar archivos
        huf_filename = f"{base_filename}_huffman_static.bin"
        with open(huf_filename, "wb") as f:
            f.write(compressed_huf)

        # Descomprimir y guardar PNG
        img_decoded = encoder_huf.decode_image(compressed_huf, metadata_huf)
        img_decoded.save(f"{base_filename}_huffman_static_decompressed.png")

        results.append(("Huffman Estático", size_huf, ratio_huf, huf_filename))
        print(f"  ✓ Guardado: {huf_filename} ({size_huf:,} bytes)")
        print(
            f"    Ratio: {ratio_huf:.2f}x | Reducción: {(1 - size_huf/original_size)*100:.1f}%"
        )
    except Exception as e:
        print(f"  ✗ Error: {e}")

    # 2. Codificación Aritmética
    print("\nComprimiendo con Codificación Aritmética...")
    try:
        encoder_arith = ArithmeticEncoder()
        compressed_arith, metadata_arith = encoder_arith.encode_image(img)
        size_arith = len(compressed_arith)
        ratio_arith = original_size / size_arith

        # Guardar archivos
        arith_filename = f"{base_filename}_arithmetic.bin"
        with open(arith_filename, "wb") as f:
            f.write(compressed_arith)

        # Descomprimir y guardar PNG
        img_decoded = encoder_arith.decode_image(compressed_arith, metadata_arith)
        img_decoded.save(f"{base_filename}_arithmetic_decompressed.png")

        results.append(("Aritmética", size_arith, ratio_arith, arith_filename))
        print(f"  ✓ Guardado: {arith_filename} ({size_arith:,} bytes)")
        print(
            f"    Ratio: {ratio_arith:.2f}x | Reducción: {(1 - size_arith/original_size)*100:.1f}%"
        )
    except Exception as e:
        print(f"  ✗ Error: {e}")

    # 3. LZW
    print("\nComprimiendo con LZW...")
    try:
        encoder_lzw = LZWEncoder(max_dict_size=4096)
        compressed_lzw, metadata_lzw = encoder_lzw.encode_image(img)
        size_lzw = len(compressed_lzw)
        ratio_lzw = original_size / size_lzw

        # Guardar archivos
        lzw_filename = f"{base_filename}_lzw.bin"
        with open(lzw_filename, "wb") as f:
            f.write(compressed_lzw)

        # Descomprimir y guardar PNG
        img_decoded = encoder_lzw.decode_image(compressed_lzw, metadata_lzw)
        img_decoded.save(f"{base_filename}_lzw_decompressed.png")

        results.append(("LZW", size_lzw, ratio_lzw, lzw_filename))
        print(f"  ✓ Guardado: {lzw_filename} ({size_lzw:,} bytes)")
        print(
            f"    Ratio: {ratio_lzw:.2f}x | Reducción: {(1 - size_lzw/original_size)*100:.1f}%"
        )
    except Exception as e:
        print(f"  ✗ Error: {e}")

    # 4. Huffman por Bloques (diferentes tamaños)
    print("\nComprimiendo con Huffman por Bloques...")

    for block_size in [8, 16, 32]:
        try:
            print(f"  Bloques {block_size}x{block_size}...")
            encoder_blocks = HuffmanBlockEncoder(block_size=block_size)
            compressed_blocks, metadata_blocks = encoder_blocks.encode_image(img)
            size_blocks = len(compressed_blocks)
            ratio_blocks = original_size / size_blocks

            # Guardar archivos
            blocks_filename = (
                f"{base_filename}_huffman_blocks_{block_size}x{block_size}.bin"
            )
            with open(blocks_filename, "wb") as f:
                f.write(compressed_blocks)

            # Descomprimir y guardar PNG
            img_decoded = encoder_blocks.decode_image(
                compressed_blocks, metadata_blocks
            )
            img_decoded.save(
                f"{base_filename}_huffman_blocks_{block_size}x{block_size}_decompressed.png"
            )

            results.append(
                (
                    f"Huffman Bloques {block_size}x{block_size}",
                    size_blocks,
                    ratio_blocks,
                    blocks_filename,
                )
            )
            print(f"    ✓ Guardado: {blocks_filename} ({size_blocks:,} bytes)")
            print(
                f"      Ratio: {ratio_blocks:.2f}x | Reducción: {(1 - size_blocks/original_size)*100:.1f}%"
            )
        except Exception as e:
            print(f"    ✗ Error: {e}")

    return results, original_size


def main():
    parser = argparse.ArgumentParser(
        description="Programa de compresión de imágenes con múltiples algoritmos",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Ejemplos de uso:
  # Comprimir una imagen real:
  python main.py --image foto.png

  # Generar y comprimir imagen sintética con entropías específicas:
  python main.py --entropy-r 2.0 --entropy-g 4.0 --entropy-b 6.0
        """,
    )

    # Grupo para imagen real
    parser.add_argument("--image", type=str, help="Ruta a la imagen a comprimir")

    # Grupo para imagen sintética
    parser.add_argument(
        "--entropy-r", type=float, help="Entropía del canal R (0.0-8.0)"
    )
    parser.add_argument(
        "--entropy-g", type=float, help="Entropía del canal G (0.0-8.0)"
    )
    parser.add_argument(
        "--entropy-b", type=float, help="Entropía del canal B (0.0-8.0)"
    )

    # Opciones adicionales
    parser.add_argument(
        "--output",
        type=str,
        default="output",
        help="Nombre base para los archivos de salida (default: output)",
    )
    parser.add_argument(
        "--resize",
        type=int,
        nargs=2,
        metavar=("WIDTH", "HEIGHT"),
        help="Redimensionar imagen a WIDTHxHEIGHT (ej: --resize 256 256)",
    )

    args = parser.parse_args()

    # Validar que se proporcione imagen o entropías
    if args.image and (
        args.entropy_r is not None
        or args.entropy_g is not None
        or args.entropy_b is not None
    ):
        parser.error("No se pueden especificar --image y --entropy-* al mismo tiempo")

    if not args.image and not (
        args.entropy_r is not None
        and args.entropy_g is not None
        and args.entropy_b is not None
    ):
        parser.error(
            "Debe especificar --image o los tres parámetros --entropy-r, --entropy-g, --entropy-b"
        )

    print("=" * 80)
    print("PROGRAMA DE COMPRESIÓN DE IMÁGENES")
    print("=" * 80)

    gen = ImageGenerator()

    # Cargar o generar imagen
    if args.image:
        print(f"\nCargando imagen: {args.image}")
        if not os.path.exists(args.image):
            print(f"✗ Error: El archivo {args.image} no existe")
            sys.exit(1)

        resize_to = tuple(args.resize) if args.resize else None
        img = gen.load_image_from_file(args.image, resize_to=resize_to)
    else:
        print(f"\nGenerando imagen sintética:")
        print(f"  Entropía R: {args.entropy_r:.2f}")
        print(f"  Entropía G: {args.entropy_g:.2f}")
        print(f"  Entropía B: {args.entropy_b:.2f}")

        img = gen.generate_image(
            entropy_r=args.entropy_r,
            entropy_g=args.entropy_g,
            entropy_b=args.entropy_b,
            seed=42,
        )

        # Guardar la imagen original generada
        img.save(f"{args.output}_original.png")
        print(f"✓ Imagen original guardada: {args.output}_original.png")

    # Mostrar estadísticas de la imagen
    stats = gen.get_image_stats(img)
    print("\n" + "-" * 80)
    print("ESTADÍSTICAS DE LA IMAGEN")
    print("-" * 80)
    print(f"Dimensiones: {img.width}x{img.height} ({img.width * img.height:,} píxeles)")
    print(f"Tamaño sin comprimir: {len(gen.get_image_bytes(img)):,} bytes")
    print(f"\nEntropías por canal:")
    for channel in ["R", "G", "B"]:
        print(f"  {channel}: {stats[channel]['entropy']:.4f} bits/símbolo")
        print(f"     Valores únicos: {stats[channel]['unique_values']}")

    # Comprimir con todos los algoritmos
    print("\n" + "=" * 80)
    print("COMPRIMIENDO CON TODOS LOS ALGORITMOS")
    print("=" * 80)

    results, original_size = compress_with_all_algorithms(img, args.output)

    # Resumen comparativo
    print("\n" + "=" * 80)
    print("RESUMEN COMPARATIVO")
    print("=" * 80)

    # Ordenar por ratio (mejor primero)
    results.sort(key=lambda x: x[2], reverse=True)

    print(
        f"\n{'Algoritmo':<30} {'Archivo':<40} {'Tamaño':>15} {'Ratio':>10} {'Reducción':>12}"
    )
    print("-" * 110)
    for name, size, ratio, filename in results:
        reduction = (1 - size / original_size) * 100
        basename = os.path.basename(filename)
        print(
            f"{name:<30} {basename:<40} {size:>15,} {ratio:>9.2f}x {reduction:>11.1f}%"
        )

    # Mejor algoritmo
    best_name, best_size, best_ratio, best_file = results[0]
    print("\n" + "=" * 80)
    print(f"🏆 MEJOR ALGORITMO: {best_name}")
    print(f"   Archivo: {os.path.basename(best_file)}")
    print(
        f"   Ratio: {best_ratio:.2f}x | Tamaño: {best_size:,} bytes | Reducción: {(1 - best_size/original_size)*100:.1f}%"
    )
    print("=" * 80)

    print(f"\n✓ Todos los archivos generados con el prefijo: {args.output}_*")
    print("  Puedes ver los tamaños de los archivos .bin en el explorador de archivos")


if __name__ == "__main__":
    main()
