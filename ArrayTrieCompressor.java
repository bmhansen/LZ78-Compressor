import java.io.IOException;

public class LZ78Compressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode();
  }

  public static void LZ78Encode() throws IOException {
    // initial setup
    int index = 1;
    int lastSeenIndex = 0;
    ArrayTrie root = new ArrayTrie();
    ArrayTrie currentAT = root;

    // while there are bytes to read
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      byte inputByte = (byte) inputInt;
      // if the byte has not been seen before in this sequence
      // then encode the index from the end of the sequence + the new byte
      // create new ArrayTrie there and store the current index with it
      // increment index and reset
      if (currentAT.getChild(inputByte) == null) {
        outputIntByte(lastSeenIndex, inputByte);
        currentAT.setChild(inputByte, new ArrayTrie());
        currentAT.setValue(inputByte, index++);
        currentAT = root;
        lastSeenIndex = 0;
      }
      // else the byte sequence must have been seen before
      // so continue at the next depth from this byte
      else {
        lastSeenIndex = currentAT.getValue(inputByte);
        currentAT = currentAT.getChild(inputByte);
      }
    }
  }

  private static void outputIntByte(int outInt, byte outByte) {
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