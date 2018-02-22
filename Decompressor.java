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
    int backRef = 0;
    byte dataByte = 0;
    Stack<Byte> byteSequence = new Stack<Byte>();

    BitUnpacker bu = new BitUnpacker();

    // while there are bytes to read
    while (bu.read()) {
      // gather the backRef dataByte pairs
      backRef = bu.getBackRef();
      dataByte = bu.getDataByte();
      // before adding the backRef dataByte pair to the dictionary, check if it is full, and resize if so
      if (index == indexDictionary.length) {
        int[] tempArray = new int[indexDictionary.length * 2];
        System.arraycopy(indexDictionary, 0, tempArray, 0, indexDictionary.length);
        indexDictionary = tempArray;
        byte[] tempArray2 = new byte[dataDictionary.length * 2];
        System.arraycopy(dataDictionary, 0, tempArray2, 0, dataDictionary.length);
        dataDictionary = tempArray2;
      }
      //save new backRef and dataByte pair to the dictionary at the current index location
      indexDictionary[index] = backRef;
      dataDictionary[index] = dataByte;
      // follow the sequence of backRefs until it hits 0 (the root)
      // pushing each dataByte from each index onto a stack so that it is ordered correctly
      byteSequence.push(dataByte);
      while (backRef != 0) {
        byteSequence.push(dataDictionary[backRef]);
        backRef = indexDictionary[backRef];
      }
      // write from the byte stack until it is empty
      while (!byteSequence.empty()) {
        System.out.write(byteSequence.pop().byteValue());
      }
    }
    System.out.flush();
  }
}
