import java.io.IOException;
import java.util.Stack;

public class Decompressor {
  public static void main(String[] args) throws IOException {
    LZ78Decode();
  }

  public static void LZ78Decode() throws IOException {
    // initial setup
    int[] indexDictionary = new int[256];
    byte[] dataDictionary = new byte[256];
    int index = 1;
    int encodeAnotherBitOnThisIndex = 2;
    int bitsToEncode = 1;
    int backRef = 0;
    byte data = 0;
    Stack<Byte> byteSequence = new Stack<Byte>();
    long bitBuffer = 0;
    int bitBufferLen = 0;
    long temp = 0;

    // while there are bytes to read
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      // add them to the buffer
      bitBuffer = bitBuffer | (long) inputInt << bitBufferLen;
      bitBufferLen += 8;
      // when the buffer contains the ref and data, extract them
      if (bitBufferLen >= bitsToEncode + 8) {
        // isolate the reference from the buffer
        temp = (bitBuffer << 64 - bitsToEncode) >>> 64 - bitsToEncode;
        backRef = (int) temp;
        bitBuffer = bitBuffer >>> bitsToEncode;
        bitBufferLen -= bitsToEncode;
        // isolate the data byte from the buffer
        temp = (bitBuffer << 64 - 8) >>> 64 - 8;
        data = (byte) temp;
        bitBuffer = bitBuffer >>> 8;
        bitBufferLen -= 8;
        // before adding the backRef data pair to the dictionary, check if it is full, and resize if so
        if (index == indexDictionary.length) {
          int[] tempArray = new int[indexDictionary.length * 2];
          System.arraycopy(indexDictionary, 0, tempArray, 0, indexDictionary.length);
          indexDictionary = tempArray;
          byte[] tempArray2 = new byte[dataDictionary.length * 2];
          System.arraycopy(dataDictionary, 0, tempArray2, 0, dataDictionary.length);
          dataDictionary = tempArray2;
        }
        //save new backRef and data pair to the dictionary at the current index location
        indexDictionary[index] = backRef;
        dataDictionary[index] = data;
        // follow the sequence of backRefs until it hits 0 (the root)
        // pushing each data byte from each index onto a stack so that it is ordered correctly
        byteSequence.push(data);
        while (backRef != 0) {
          byteSequence.push(dataDictionary[backRef]);
          backRef = indexDictionary[backRef];
        }
        // write from the byte stack until it is empty
        while (!byteSequence.empty()) {
          System.out.write(byteSequence.pop().byteValue());
        }
        // if the next index requires another bit of information, then increment bitsToEncode
        if (++index == encodeAnotherBitOnThisIndex) {
          encodeAnotherBitOnThisIndex *= 2;
          bitsToEncode++;
        }
      }
    }
    System.out.flush();
  }
}
