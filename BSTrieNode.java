public class BSTrieNode {
	byte dataByte = 0;
	BSTrieNode left = null;
	BSTrieNode right = null;
	BSTrie nextDepth = null;

	public BSTrieNode(){}

	public BSTrieNode(byte dByte, int backRef) {
    dataByte = dByte;
    nextDepth = new BSTrie(backRef);
  }

  // searches this BSTrieNode and all subtries (left and right)
  // returns either the BST that matches toFind, or returns the parent of where it should be inserted
	public BSTrieNode find(byte toFind) {
		// if this BSTrie matches the byte then return it
		if (dataByte == toFind) {
			return this;
		}
		// if the dataByte is less than toFind
		else if (toFind < dataByte) {
			// if there is a left subtrie, search it
			if (left != null) {
				return left.find(toFind);
			}
			// left subtrie not found, so return where it should be inserted
			else {
				return this;
			}
		}
		// else the dataByte must be greater than toFind
		else {
			// if there is a right subtrie, search it
			if (right != null) {
				return right.find(toFind);
			}
			// right subtrie not found, so return where it should be inserted
			else {
				return this;
			}
		}
	}
}
