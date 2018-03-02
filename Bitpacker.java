import java.io.IOException;
import java.io.OutputStream;

public class BitPacker {

  // bitpacked data will be written to and will queue up in this buffer
  // a long was chosen because it needs to hold a byte, (8 bits) plus a large int (up to 32bits)  
  // plus any left over bits not flushed and waiting to be output (up to 7)
  // it's a first-in first-out queue where it builds from the least significant bit
  long bitBuffer = 0;
  // the number of bits in use in the buffer
  int bitsInUse = 0;

  // number of bits needed to minimally encode the backRef
  int minBitsToEncode = 1;
  // the number of writes that have occured (also the max possible backRef)
  int numberOfWrites = 0;
  // update minBitsToEncode when this matches numberOfWrites
  int encodeAnotherBitOn = 2;
  // The output stream to write to
  OutputStream out = null;

  // constructor that assigns the output stream to one provided
  BitPacker(OutputStream outStream) {
    out = outStream;
  }

  public void write(int i, byte b) throws IOException {
    // adds the backRef onto the buffer, bitpacks it to the minimal number of bits needed to encode it
    bitBuffer |= (long) (i & 0xFFFFFFFF) << bitsInUse;
    bitsInUse += minBitsToEncode;
    // adds the new data byte onto the buffer, no bitpacking as all 8 bits are needed
    bitBuffer |= (long) (b & 0xFF) << bitsInUse;
    bitsInUse += 8;

    numberOfWrites++;
    // if the next backRef requires another bit of information, then increment minBitsToEncode
    if (numberOfWrites == encodeAnotherBitOn) {
      minBitsToEncode++;
      encodeAnotherBitOn *= 2;
    }

    // while there is at least 8 bits of data to write in the buffer, output it in bytes
    while (bitsInUse >= 8) {
      output();
    }
  }

  // force flushes all bits in the buffer until empty
  // pads with zeros on the most significant bits if necessary
  public void flush() throws IOException {
    while (bitsInUse > 0) {
      output();
    }
    out.flush();
  }

  // outputs a byte from the buffer onto the output stream
  private void output() throws IOException {
    out.write((byte) bitBuffer);
    bitBuffer >>>= 8;
    bitsInUse -= 8;
  }
}
