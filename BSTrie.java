public class BSTrie {

	BSTrieNode rootNode = null;
	int backRef = 0;

	public BSTrie(int ref){
		backRef = ref;
	}
	
	public BSTrieNode find(byte toFind) {
		return rootNode.find(toFind);
	}

}
