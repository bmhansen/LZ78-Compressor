public class BSTrieNode {
	byte dataByte;
	int backRef;
	BSTrieNode left;
	BSTrieNode right;
	BSTrieNode nextLevel;

	public BSTrieNode() {
	}

	public BSTrieNode(byte dataByte, int backRef) {
		this.dataByte = dataByte;
		this.backRef = backRef;
	}

	// searches this BSTrieNode and all subtries (left and right)
	// returns either the BST that matches toFind, or returns the parent of where it should be inserted
	public BSTrieNode getMatchOrInsert(byte toFind, int backRef) {
		// if this BSTrie matches the byte then return it
		if (dataByte == toFind) {
			return this;
		}
		// if the dataByte is less than toFind
		else if (toFind < dataByte) {
			// if there is a left subtrie, search it
			if (left != null) {
				return left.getMatchOrInsert(toFind, backRef);
			}
			// left subtrie not found, so insert it here and return null
			else {
				this.left = new BSTrieNode(toFind, backRef);
				return null;
			}
		}
		// else the dataByte must be greater than toFind
		else {
			// if there is a right subtrie, search it
			if (right != null) {
				return right.getMatchOrInsert(toFind, backRef);
			}
			// right subtrie not found, so insert it here and return null
			else {
				this.right = new BSTrieNode(toFind, backRef);
				return null;
			}
		}
	}
}
