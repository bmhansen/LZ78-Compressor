public class BSTrie {

	byte word;
	int num;
	BSTrie left, right, link;

	public BSTrie() {
		word = 0;
		num = 0;
		left = null;
		right = null;
		link = null;
	}

	public BSTrie(byte w, int n) {
		word = w;
		num = n;
		left = null;
		right = null;
		link = null;
	}

	public boolBST find(byte tofind) { // if found, returns the true and the BSTrie containing the word
		// if not found, returns false and the BSTrie parent of where it should be inserted
		if (word == tofind) {
			return new boolBST(true, this);
		} else if (tofind < word) {
			if (left != null) {
				return left.find(tofind);
			} else {
				return new boolBST(false, this);
			}
		} else { // (tofind > word)
			if (right != null) {
				return right.find(tofind);
			} else {
				return new boolBST(false, this);
			}
		}
	}

	public void printLex() { // Prints out all words in the provided BSTrie and all subtrees
		if (word != null) {
			if (left != null) {
				left.printLex();
			}
			System.out.println(word);
			if (right != null) {
				right.printLex();
			}
		}
	}
}