import java.io.IOException;
import java.util.Stack;

public class ArrayTrieDecompressor {
  public static void main(String[] args) throws IOException {
    LZ78Decode();
  }

  public static void LZ78Decode() throws IOException {
    // initial setup
    int[] indexDictionary = new int[256];
    byte[] dataDictionary = new byte[256];
    int index = 1;
    int backRef = 0;
    byte data = 0;
    Stack<Byte> byteSequence = new Stack<Byte>();

    // while there are bytes to read 0(0,""),1(0,a),2(0,b),3(1,b)
    for (int inputInt = System.in.read(); inputInt >= 0; inputInt = System.in.read()) {
      // gather the int reference index and the new data byte
      backRef = readInt(inputInt);
      data = (byte) System.in.read();
      //save new backRef and data pair at the current index location
      indexDictionary[index] = backRef;
      dataDictionary[index++] = data;
      // follow the sequence of indexes until it hits 0, 
      // pushing each data byte from each index onto a stack
      byteSequence.push(data);
      while (backRef != 0) {
        byteSequence.push(dataDictionary[backRef]);
        backRef = indexDictionary[backRef];
      }
      // write from the byte stack until it is empty
      while (!byteSequence.empty()) {
        System.out.write(byteSequence.pop().byteValue());
      }
      System.out.flush();
    }
  }

  private static int readInt(int firstByte) throws IOException {
    // TODO bitunpacking
    // returns the int index in big endian
    return (firstByte << 24 | System.in.read() << 16 | System.in.read() << 8 | System.in.read());
  }
}
