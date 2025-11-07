import heapq
import pickle
from PIL import Image
import numpy as np
from io import BytesIO


class HuffmanBlockEncoder:
    def __init__(self, block_size=256):
        self.block_size = block_size

    # ---------- Funciones internas de Huffman ----------
    def _build_frequency_table(self, data):
        freq = {}
        for val in data:
            freq[val] = freq.get(val, 0) + 1
        return freq

    def _build_huffman_tree(self, freq_table):
        heap = [[weight, [symbol, ""]] for symbol, weight in freq_table.items()]
        heapq.heapify(heap)
        if len(heap) == 1:
            # Caso extremo: solo un símbolo
            weight, [symbol, _] = heap[0]
            return {symbol: "0"}
        while len(heap) > 1:
            low = heapq.heappop(heap)
            high = heapq.heappop(heap)
            for pair in low[1:]:
                pair[1] = "0" + pair[1]
            for pair in high[1:]:
                pair[1] = "1" + pair[1]
            heapq.heappush(heap, [low[0] + high[0]] + low[1:] + high[1:])
        huffman_tree = sorted(heapq.heappop(heap)[1:], key=lambda p: (len(p[1]), p))
        return {symbol: code for symbol, code in huffman_tree}

    def _encode_data(self, data, huffman_tree):
        return "".join(huffman_tree[val] for val in data)

    def _decode_data(self, bitstring, huffman_tree):
        rev_tree = {v: k for k, v in huffman_tree.items()}
        decoded = []
        current = ""
        for bit in bitstring:
            current += bit
            if current in rev_tree:
                decoded.append(rev_tree[current])
                current = ""
        return np.array(decoded, dtype=np.uint8)

    # ---------- Conversión a bits / bytes ----------
    def _bits_to_bytes(self, bits):
        padding = 8 - len(bits) % 8 if len(bits) % 8 != 0 else 0
        bits += "0" * padding
        b = bytearray()
        for i in range(0, len(bits), 8):
            b.append(int(bits[i : i + 8], 2))
        return bytes([padding]) + bytes(b)

    def _bytes_to_bits(self, b):
        padding = b[0]
        bits = "".join(f"{byte:08b}" for byte in b[1:])
        return bits[:-padding] if padding > 0 else bits

    # ---------- Codificación por bloques ----------
    def encode_image(self, img):
        img_np = np.array(img)
        h, w, _ = img_np.shape
        block_size = self.block_size

        compressed_streams = []
        metadata = {
            "block_size": block_size,
            "width": w,
            "height": h,
            "blocks": [],
        }

        for y in range(0, h, block_size):
            for x in range(0, w, block_size):
                block = img_np[y : y + block_size, x : x + block_size]
                block_info = {"coords": (x, y), "lengths": {}, "trees": {}}

                for i, channel in enumerate(["R", "G", "B"]):
                    channel_data = block[:, :, i].flatten()
                    freq_table = self._build_frequency_table(channel_data)
                    tree = self._build_huffman_tree(freq_table)
                    bitstring = self._encode_data(channel_data, tree)
                    block_bytes = self._bits_to_bytes(bitstring)

                    compressed_streams.append(block_bytes)
                    block_info["lengths"][channel] = len(block_bytes)
                    block_info["trees"][channel] = tree

                metadata["blocks"].append(block_info)

        compressed_data = b"".join(compressed_streams)
        return compressed_data, metadata

    # ---------- Decodificación ----------
    def decode_image(self, compressed_data, metadata):
        block_size = metadata["block_size"]
        width = metadata["width"]
        height = metadata["height"]

        img_np = np.zeros((height, width, 3), dtype=np.uint8)
        data_stream = BytesIO(compressed_data)

        for block_info in metadata["blocks"]:
            x, y = block_info["coords"]
            block_decoded = np.zeros((block_size, block_size, 3), dtype=np.uint8)

            for i, channel in enumerate(["R", "G", "B"]):
                length = block_info["lengths"][channel]
                tree = block_info["trees"][channel]
                block_bytes = data_stream.read(length)
                bits = self._bytes_to_bits(block_bytes)
                decoded_flat = self._decode_data(bits, tree)

                # Ajustar tamaño por si el bloque no es completo (borde)
                h_block = min(block_size, height - y)
                w_block = min(block_size, width - x)
                decoded_flat = decoded_flat[: h_block * w_block]
                block_decoded[:h_block, :w_block, i] = decoded_flat.reshape(
                    (h_block, w_block)
                )

            img_np[y : y + block_decoded.shape[0], x : x + block_decoded.shape[1]] = (
                block_decoded
            )

        return Image.fromarray(img_np, mode="RGB")
