import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestAll {

  @Test
  public void testEmpty() throws Exception {
    testStream(new byte[] {});
  }

  @Test
  public void testSmallStream() throws Exception {
    testStream(new byte[] {-128, 0, 127, 65, 66, 65, 67, 67, 67, 68, 65});
  }

  
  public void testStream(byte[] streamToTest) throws Exception {
    ByteArrayInputStream in = new ByteArrayInputStream(streamToTest);

    // Feed stream into ArrayTrieCompressor and BSTCompressor, verify they output the same
    ByteArrayOutputStream outAT = new ByteArrayOutputStream();
    ArrayTrieCompressor.LZ78Encode(in, outAT);
    byte[] outBytesAT = outAT.toByteArray();
    in.reset();
    ByteArrayOutputStream outBST = new ByteArrayOutputStream();
    BSTCompressor.LZ78Encode(in, outBST);
    byte[] outBytesBST = outBST.toByteArray();
    for (int i = 0; i < outBytesAT.length - 1; i++){
      System.err.println(i);
      assertEquals(outBytesAT[i], outBytesBST[i]);
    }

    // Feed the compressor's output into the Decompressor and verify it returns the original stream
    ByteArrayInputStream in2 = new ByteArrayInputStream(outBytesAT);
    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(in2, out2);
    byte[] outBytes2 = out2.toByteArray();
    assertEquals(streamToTest.length, outBytes2.length);
    for (int i = 0; i < streamToTest.length - 1; i++){
      assertEquals(streamToTest[i], outBytes2[i]);
    }
  }

}
