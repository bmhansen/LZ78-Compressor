import java.net.*;
import java.io.*;
import java.util.*;

public class Decoder {

	public static void main(String[] args) throws Exception {

		InputStream input = System.in;
		PrintStream output = System.out;

		int dictionaryIndex = 1;
		int i = 0;
		int ref = 0;
		byte data = 0;
		tuple[] dictionary = new tuple[256];
		tuple[] temp = dictionary;

		while ((i = input.read()) != -1) {
			ref = ((byte) i & 0xFF) << 24 | ((byte) input.read() & 0xFF) << 16 | ((byte) input.read() & 0xFF) << 8
					| ((byte) input.read() & 0xFF);
			data = (byte) input.read();

			if (dictionary.length == dictionaryIndex) { //if dictionary needs extending then double the length
				temp = new tuple[dictionaryIndex * 2];
				System.arraycopy(dictionary, 0, temp, 0, dictionaryIndex);
				dictionary = temp;
			}

			// adds tuple to dictionary and then prints it and all its children
			dictionary[dictionaryIndex] = new tuple(ref, data);
			outputData(dictionaryIndex, dictionary, output);
			dictionaryIndex++;
		}
	}

	public static void outputData(int refNum, tuple[] dict, PrintStream out) throws Exception {
		// calls itself until the reference is the root aka 0, and prints out all data on the path to the root
		if (refNum != 0) {
			outputData(dict[refNum].ref, dict, out);
			out.write(dict[refNum].data);
		}
	}
}