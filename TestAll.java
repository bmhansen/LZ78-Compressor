import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import java.util.Random;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestAll {

  @Test
  public void testEmpty() throws Exception {
    testByteArray(new byte[] {});
  }

  @Test
  // Does many tests on random byte sequences, where the bytes can be 0 or 1
  public void testSmallStreams() throws Exception {
    Random rand = new Random();
    for (int i = 0; i < 100000; i++) {
      byte[] randomByteArray = new byte[1024];
      for (int j = 0; j < randomByteArray.length; j++) {
        randomByteArray[j] = (byte) rand.nextInt(2);
      }
      testByteArray(randomByteArray);
    }
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
    ByteArrayInputStream in2 = new ByteArrayInputStream(outBytesBST);
    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(in2, out2);
    byte[] outBytes2 = out2.toByteArray();
    assertArrayEquals(inputByteArray, outBytes2);
  }

}
