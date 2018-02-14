import java.io.IOException;

public class ArrayTrieCompressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode();
  }

  public static void LZ78Encode() throws IOException {
    // initial setup
    int index = 1;
    int useAnotherBitOnThisIndex = 2;
    int bitsToEncode = 1;
    int previousIndex = 0;
    int currentIndex = 0;
    ArrayTrie root = new ArrayTrie();
    ArrayTrie currentAT = root;
    byte inputByte = 0;
    long bitBuffer = 0;
    int bitBufferLen = 0;

    // while there are bytes to read
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      inputByte = (byte) inputInt;
      // if the byte has not been seen before in this sequence
      // then encode the index from the end of the sequence + the new byte
      // create new ArrayTrie there and store the current index with it
      // increment index and reset
      if (currentAT.getChild(inputByte) == null) {
        // add the current index and the new byte onto the buffer
        bitBuffer = bitBuffer | (long) currentIndex << bitBufferLen
            | (long) (inputByte & 0xFF) << (bitBufferLen + bitsToEncode);
        bitBufferLen += bitsToEncode + 8;
        // while there are bytes to write in the long buffer, output them
        while (bitBufferLen >= 8) {
          System.out.write((byte) bitBuffer);
          bitBuffer = bitBuffer >>> 8;
          bitBufferLen -= 8;
        }
        currentAT.setChild(inputByte, new ArrayTrie());
        currentAT.setValue(inputByte, index++);
        if (index == useAnotherBitOnThisIndex) {
          useAnotherBitOnThisIndex *= 2;
          bitsToEncode++;
        }
        currentAT = root;
        currentIndex = 0;
      }
      // else the byte sequence must have been seen before
      // so continue at the next depth from this byte
      else {
        previousIndex = currentIndex;
        currentIndex = currentAT.getValue(inputByte);
        currentAT = currentAT.getChild(inputByte);
      }
    }
    // if we are part-way through a sequence when input ended
    // then update the buffer with the previous sequences index plus the latest byte of data
    // and flush out the whole buffer until it is empty
    if (currentAT != root) {
      bitBuffer = bitBuffer | previousIndex << bitBufferLen | inputByte << bitBufferLen + bitsToEncode;
      bitBufferLen += bitsToEncode + 8;
      while (bitBufferLen > 0) {
        System.out.write((byte) bitBuffer);
        bitBuffer = bitBuffer >>> 8;
        bitBufferLen -= 8;
      }
    }
    System.out.flush();
  }
}
