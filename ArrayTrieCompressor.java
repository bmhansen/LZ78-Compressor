import java.io.IOException;

public class ArrayTrieCompressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode();
  }

  public static void LZ78Encode() throws IOException {
    // initial setup
    int index = 1;
    int previousIndex = 0;
    int currentIndex = 0;
    ArrayTrie root = new ArrayTrie();
    ArrayTrie currentAT = root;
    byte inputByte = 0;

    // while there are bytes to read
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      inputByte = (byte) inputInt;
      // if the byte has not been seen before in this sequence
      // then encode the index from the end of the sequence + the new byte
      // create new ArrayTrie there and store the current index with it
      // increment index and reset
      if (currentAT.getChild(inputByte) == null) {
        outputIntByte(currentIndex, inputByte);
        currentAT.setChild(inputByte, new ArrayTrie());
        currentAT.setValue(inputByte, index++);
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
    // then output the previous sequences index plus the latest byte of data
    if (currentAT != root){
      outputIntByte(previousIndex, inputByte);
    }
  }

  private static void outputIntByte(int outInt, byte outByte) {
    // TODO bitpacking compression
    // outputs outInt converted to big endian bytes, followed by outByte
    byte[] outputByteArray = new byte[5];
    outputByteArray[0] = (byte) (outInt >> 24);
    outputByteArray[1] = (byte) (outInt >> 16);
    outputByteArray[2] = (byte) (outInt >> 8);
    outputByteArray[3] = (byte) outInt;
    outputByteArray[4] = outByte;
    System.out.write(outputByteArray, 0, 5);
  }
}
