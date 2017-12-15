# LZ78 Compressor & Decompressor with Bitpacking
This program implements LZ78 compression consisting of four main parts, an encoder, a bitpacker, which turn it into a compressed file; and a bitunpacker, and a decoder which decompress it.

## Encoder
The encoder takes any file and outputs a list of 4 byte big endian back reference to a byte sequence plus a raw byte from the input file.

## Bitpacker
The bitpacker removes excess bits from the encoded output by shortening the back reference. For example the first reference must be 0 as there is only the empty byte sequence. so we can save space only storing the raw byte. When we are 100 byte sequences deep it is possible that we have seen 100 unique byte sequences so we can store the reference in only 7 bits as the reference will never be larger than 128.

## Bitunpacker
The bitunpacker as the name suggests does the reverse of the bitpacker. Instead of removing leading empty bits it pads the references with them so that it can be interpreted easily by the Decoder

## Decoder
The decoder takes each 4 byte back reference and raw byte pair and rebuilds the original byte data.
