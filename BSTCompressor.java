import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BSTCompressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode(System.in, System.out);
  }

  public static void LZ78Encode(InputStream in, OutputStream out) throws IOException {
    // Initial setup

    // The root BST used to store all byte sequences as back references
    BSTrie root = new BSTrie(0);
    // The current byte sequence depth being worked with
    BSTrie currentDepth = root;
    // The parent byte sequence of the currentDepth byte sequence
    BSTrie parentDepth = root;
    // An instance of the BitPacker class is created to be used in bit packing the results of encoding
    BitPacker bp = new BitPacker(out);
    // The next new back reference to be created
    int newBackRef = 1;
    // The latest byte read from input
    byte inputByte = 0;
    // The found BSTrieNode that either matches the inputbyte, or is the parent of where it should be inserted
    BSTrieNode found = null;

    // While there are bytes to read
    for (int inputInt = in.read(); inputInt >= 0; inputInt = in.read()) {
      inputByte = (byte) inputInt;

      // Check there are any nodes on this BSTrie to search
      if (currentDepth.rootNode != null) {
        // Search the BSTrie for any nodes that match the inputByte
        found = currentDepth.find(inputByte);
        // If the inputByte has been seen after this sequence, search the next depth
        if (inputByte == found.dataByte) {
          parentDepth = currentDepth;
          currentDepth = found.nextDepth;
          continue;
        }
        // The byte needs to be inserted on the left BSTrieNode
        else if (inputByte < found.dataByte) {
          found.left = new BSTrieNode(inputByte, newBackRef++);
        }
        // the byte needs to be inserted on the right BSTrieNode
        else {
          found.right = new BSTrieNode(inputByte, newBackRef++);
        }
      }
      // Else this means there are no nodes on this BSTrie, add the first one with this inputByte
      else {
        currentDepth.rootNode = new BSTrieNode(inputByte, newBackRef++);
      }

      // Bit pack the back reference of the sequence along with the new byte
      bp.write(currentDepth.backRef, inputByte);
      // Reset sequence back to the root
      currentDepth = root;
    }

    // If we are part-way through a sequence when the input ends
    if (currentDepth != root) {
      // Bit pack the parent sequence's backRef with the last byte of data
      bp.write(parentDepth.backRef, inputByte);
    }
    // If there was any bit packed data not sent, this flush sends it
    bp.flush();
  }
}
