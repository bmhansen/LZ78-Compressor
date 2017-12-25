import java.net.*;
import java.io.*;
import java.util.*;

public class Bitpacker {

	public static void main(String[] args) throws Exception {
		int dictionaryIndex = 0;

		InputStream input = System.in;
		PrintStream output = System.out;

		int i = 0;
		int addAnotherBit = 2;
		int numberOfBits = 1;
		int toOutputBits = 0;
		int toOutput = 0;

		int ref = 0;
		byte data = 0;

		while ((i = input.read()) != -1 || dictionaryIndex < 2) {
			ref = ((byte) i & 0xFF) << 24 | ((byte) input.read() & 0xFF) << 16 | ((byte) input.read() & 0xFF) << 8
					| ((byte) input.read() & 0xFF);
			data = (byte) input.read();

			System.out.println(ref + " " + data);

			if (dictionaryIndex == addAnotherBit) {
				System.out.println(addAnotherBit + " " + numberOfBits + " " + dictionaryIndex);
				addAnotherBit = addAnotherBit * 2;
				numberOfBits++;
			}

			ref = ref << (32 - numberOfBits + toOutputBits);
			toOutput = toOutput | ref;
			toOutputBits = toOutputBits + numberOfBits;
			while (toOutputBits > 8) {
				output.write(toOutput >> 24);
				System.out.println(toOutput >> 24);
				toOutput = toOutput << 8;
				toOutputBits = toOutputBits - 8;
			}
			toOutput = toOutput | (data << (24 - toOutputBits));
			output.write(toOutput >> 24);
			System.out.println(toOutput >> 24);
			toOutput = toOutput << 8;

			dictionaryIndex++;
		}
	}
}
