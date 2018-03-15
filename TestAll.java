import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestAll {

  @Test
  public void testEmpty() throws Exception {
    testByteArray(new byte[] {});
  }

  @Test
  public void testSmallStream() throws Exception {
    testByteArray(new byte[] { -128, 0, 127, 65, 66, 65, 67, 67, 67, 68, 65 });
  }

  private void testByteArray(byte[] inputByteArray) throws Exception {
    ByteArrayInputStream in = new ByteArrayInputStream(inputByteArray);

    // Feed stream into ArrayTrieCompressor and BSTCompressor, verify both outputs are the same
    ByteArrayOutputStream outAT = new ByteArrayOutputStream();
    BitPacker packerAT = new BitPacker(outAT);
    ArrayTrieCompressor.LZ78Encode(in, packerAT);
    byte[] outBytesAT = outAT.toByteArray();
    in.reset();
    ByteArrayOutputStream outBST = new ByteArrayOutputStream();
    BitPacker packerBST = new BitPacker(outBST);
    BSTCompressor.LZ78Encode(in, packerBST);
    byte[] outBytesBST = outBST.toByteArray();
    assertArrayEquals(outBytesAT, outBytesBST);

    // Feed the compressor's output into the Decompressor, verify it returns the original stream
    ByteArrayInputStream in2 = new ByteArrayInputStream(outBytesAT);
    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(in2, out2);
    byte[] outBytes2 = out2.toByteArray();
    assertEquals(inputByteArray.length, outBytes2.length);
    for (int i = 0; i < inputByteArray.length - 1; i++) {
      assertEquals(inputByteArray[i], outBytes2[i]);
    }
  }

}
