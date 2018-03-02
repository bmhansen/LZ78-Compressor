import java.io.IOException;
import java.io.InputStream;

public class BitUnpacker {

  // bitpacked data will be read into and will queue up in this buffer
  // a long was chosen because it needs to hold a byte, (8 bits) plus a large int (up to 32bits)  
  // plus any left over bits not flushed and waiting to be read (up to 7)
  // it's a first-in first-out queue where it builds from the least significant bit
  long bitBuffer = 0;
  // the number of bits in use in the buffer
  int bitsInUse = 0;

  // number of bits needed to minimally decode the backRef
  int minBitsToDecode = 1;
  // the number of writes that have occured (also the max possible backRef)
  int numberOfReads = 0;
  // update minBitsToDecode when this matches numberOfReads
  int decodeAnotherBitOn = 2;

  // backRef and dataByte pair extracted from unpacking a stream of bytes
  int backRef = 0;
  byte dataByte = 0;
  long extractionBuffer = 0;

  // The input stream to read from
  InputStream in = null;

  // constructor that assigns the output stream to one provided
  BitUnpacker(InputStream inStream) {
    in = inStream;
  }

  // reads the input until it can unpack a backRef, data byte pair
  public boolean read() throws IOException {
    for (int inputInt = in.read(); inputInt >= 0; inputInt = in.read()) {
      // add them to the buffer
      bitBuffer = bitBuffer | (long) inputInt << bitsInUse;
      bitsInUse += 8;
      // when the buffer contains the ref and data, extract them
      if (bitsInUse >= minBitsToDecode + 8) {
        // isolate the reference from the buffer
        extractionBuffer = (bitBuffer << 64 - minBitsToDecode) >>> 64 - minBitsToDecode;
        backRef = (int) extractionBuffer;
        bitBuffer >>>= minBitsToDecode;
        bitsInUse -= minBitsToDecode;
        // isolate the data byte from the buffer
        extractionBuffer = (bitBuffer << 64 - 8) >>> 64 - 8;
        dataByte = (byte) extractionBuffer;
        bitBuffer >>>= 8;
        bitsInUse -= 8;

        numberOfReads++;
        // if the next backRef requires another bit of information, then increment minBitsToDecode
        if (numberOfReads == decodeAnotherBitOn) {
          minBitsToDecode++;
          decodeAnotherBitOn *= 2;
        }

        // backRef and the dataByte have been unpacked, time to return successfully
        return true;
      }
    }
    // end of input, return with a failure to gather a new backRef and dataByte
    return false;
  }

  public int getBackRef() {
    return backRef;
  }

  public byte getDataByte() {
    return dataByte;
  }
}
