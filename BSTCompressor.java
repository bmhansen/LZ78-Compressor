import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BSTCompressor {
  public static void main(String[] args) throws IOException {
    LZ78Encode(System.in, new BitPacker(System.out));
  }

  public static void LZ78Encode(InputStream in, Packer packer) throws IOException {
    // Initial setup

    // The root BSTrieNode which has a backRef of 0 and symbolically holds the empty byte
    BSTrieNode root = new BSTrieNode();
    // The current node Level being worked with
    BSTrieNode currentLevel = root.nextLevel;
    // The parent node of the currentLevel
    BSTrieNode parentNode = root;
    // The parent of the parent node, is only used when the stream ends midway through traversing the dictionary
    BSTrieNode grandParentNode = root;
    // The next new back reference to be created
    int nextBackRef = 1;
    // The latest byte read from input
    byte inputByte = 0;
    // The found BSTrieNode that either matches the inputbyte, or is the parent of where it should be inserted
    BSTrieNode found = null;

    // While there are bytes to read
    for (int inputInt = in.read(); inputInt >= 0; inputInt = in.read()) {
      inputByte = (byte) inputInt;

      // If there are no nodes on the next level, add the first one with this inputByte
      if (currentLevel == null) {
        parentNode.nextLevel = new BSTrieNode(inputByte, nextBackRef++);
      }

      // Since there are nodes, search through them trying to match the inputByte
      else {
        found = currentLevel.getMatchOrInsert(inputByte, nextBackRef);
        // If the inputByte was found after this sequence, search the next level
        if (found != null) {
          grandParentNode = parentNode;
          parentNode = found;
          currentLevel = found.nextLevel;
          continue;
        }
        // the inputByte was not found so it was inserted and nextBackRef now needs to be incremented
        nextBackRef++;
      }

      // Bit pack the back reference of the sequence along with the new byte
      packer.write(parentNode.backRef, inputByte);
      // Reset sequence back to the start
      currentLevel = root.nextLevel;
      parentNode = root;
    }

    // If we are part-way through a sequence when the input ends
    if (currentLevel != root.nextLevel) {
      // Bit pack the parent sequence's backRef with the last byte of data
      packer.write(grandParentNode.backRef, inputByte);
    }
    // If there was any bit packed data not sent, this flush sends it
    packer.flush();
  }
}
