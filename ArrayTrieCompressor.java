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
    BitPacker bp = new BitPacker();
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      inputByte = (byte) inputInt;
      // if the byte has not been seen before in this sequence
      // then encode the backRef from the end of the sequence + the new byte
      // create new ArrayTrie there and store the current backRef with it
      // increment backRef and reset
      if (currentAT.getChild(inputByte) == null) {
        // add the current index and the new input data byte onto the buffer
        bp.write(currentIndex, inputByte);

        currentAT.setChild(inputByte, new ArrayTrie());
        currentAT.setValue(inputByte, index);

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
    // then update the buffer with the previous sequences backRef plus the latest byte of data
    if (currentAT != root) {
      bp.write(previousIndex, inputByte);
    }
    // if there was any bitpacked data not sent, this flush sends it
    bp.flush();
  }
}
