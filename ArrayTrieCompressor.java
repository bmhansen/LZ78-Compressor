import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArrayTrieCompressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode(System.in, new BitPacker(System.out));
  }

  public static void LZ78Encode(InputStream in, Packer packer) throws IOException {
    // Initial setup

    // The root ArrayTrie used to store all byte sequences as back references
    ArrayTrie root = new ArrayTrie();
    // The current ArrayTrie being worked with
    ArrayTrie currentAT = root;
    // The next new back reference to be created
    int newBackRef = 1;
    // The current depth's back reference
    // Corresponds to the current byte sequence (0 being root, the empty sequence)
    int currentBackRef = 0;
    // The parent of the current depth's back reference
    int parentBackRef = 0;
    // The latest byte read from input
    byte inputByte = 0;

    // While there are bytes to read
    for (int inputInt = in.read(); inputInt >= 0; inputInt = in.read()) {
      inputByte = (byte) inputInt;

      // If the input byte has been seen after this sequence
      if (currentAT.getChild(inputByte) != null) {
        // continue at the next depth
        parentBackRef = currentBackRef;
        currentBackRef = currentAT.getValue(inputByte);
        currentAT = currentAT.getChild(inputByte);
        continue;
      }

      // If the input byte has not been seen after this sequence
      if (currentAT.getChild(inputByte) == null) {
        // Bit pack the back reference of the sequence along with the new byte
        packer.write(currentBackRef, inputByte);

        // Create a new ArrayTrie for the sequence not seen before and assign it a new back reference
        currentAT.setChild(inputByte, new ArrayTrie());
        currentAT.setValue(inputByte, newBackRef++);

        // Reset the sequence back to the root
        currentAT = root;
        currentBackRef = 0;
      }
    }
    // If we are part-way through a sequence when the input ends
    if (currentAT != root) {
      // Bit pack the parent sequence's backRef with the last byte of data
      packer.write(parentBackRef, inputByte);
    }
    // If there was any bit packed data not sent, this flush sends it
    packer.flush();
  }
}
