import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.ArrayList;

public class Decompressor {
  public static void main(String[] args) throws IOException {
    LZ78Decode(System.in, System.out);
  }

  public static void LZ78Decode(InputStream in, OutputStream out) throws IOException {
    // Initial setup

    // The dictionary of back reference values
    ArrayList<Integer> backRefDictionary = new ArrayList<Integer>();
    // The dictionary of byte values which correspond to the back references at the same position
    ArrayList<Byte> byteDictionary = new ArrayList<Byte>();
    // The back reference gathered from input
    int inputBackRef = 0;
    // The data byte gathered from input
    byte inputDataByte = 0;
    // The sequence of bytes to be written is stored in a stack to help reverse their order
    Stack<Byte> byteSequence = new Stack<Byte>();
    // An instance of the BitUnpacker class is created to be used in bit unpacking the input
    BitUnpacker bu = new BitUnpacker(in);

    // while there are bytes to read
    while (bu.read()) {
      // gather the back reference, data byte pairs
      inputBackRef = bu.getBackRef();
      inputDataByte = bu.getDataByte();
      // save the new backRef and dataByte pair to the dictionary
      backRefDictionary.add(inputBackRef);
      byteDictionary.add(inputDataByte);

      // push the input data byte onto the stack to be output
      byteSequence.push(inputDataByte);
      // follow the sequence of back references until it hits 0 (the root)
      while (inputBackRef != 0) {
        // pushing each data byte from each back reference onto a stack
        byteSequence.push(byteDictionary.get(inputBackRef - 1));
        inputBackRef = backRefDictionary.get(inputBackRef - 1);
      }
      // write from the byte stack until it is empty
      while (!byteSequence.empty()) {
        out.write(byteSequence.pop().byteValue());
      }
    }
    // flush in case any bytes have not been sent on the output
    out.flush();
  }
}
