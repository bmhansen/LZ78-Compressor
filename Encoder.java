import java.net.*;
import java.io.*;
import java.util.*;

public class Encoder {

	public static void main(String[] args) throws Exception {
		int dictionaryIndex = 1;
		BSTrie root = new BSTrie((byte) 0, 0);
		BSTrie current = root;
		BoolBST temp = null;
		BSTrie temp2 = null;
		int i = 0;
		byte inputByte = 0;

		InputStream input = System.in;
		PrintStream output = System.out;

		while ((i = input.read()) != -1) { // while there are bytes to read
			inputByte = (byte) i;
			if (current.link != null) { // if it has a link to search for matches in
				temp = current.link.find(inputByte);// search for a match
				temp2 = current;
				if (temp.bool) { // if it finds a match, repeat and read in another byte to see if it too matches
					current = temp.bst;
				} else { // if it didnt find a match, add it
					if (inputByte < temp.bst.word) { // if it goes on left tree add left
						temp.bst.left = new BSTrie(inputByte, dictionaryIndex);
						dictionaryIndex++;
						output.write(outputFormat(current.num, inputByte));
						current = root;
					} else { // else add to right tree
						temp.bst.right = new BSTrie(inputByte, dictionaryIndex);
						dictionaryIndex++;
						output.write(outputFormat(current.num, inputByte));
						current = root;
					}
				}
			} else { // doesn't have link so add it as one
				current.link = new BSTrie(inputByte, dictionaryIndex);
				dictionaryIndex++;
				output.write(outputFormat(current.num, inputByte));
				current = root;
			}
		}
		if (current != root) { // covers the case where input ends halfway through encoding
			output.write(outputFormat(temp2.num, current.word));
		}
	}

	public static byte[] outputFormat(int number, byte in) {
		byte[] toWrite = new byte[5];
		toWrite[0] = (byte) (number >> 24);
		toWrite[1] = (byte) (number >> 16);
		toWrite[2] = (byte) (number >> 8);
		toWrite[3] = (byte) (number);
		toWrite[4] = in;
		return toWrite;
	}
}