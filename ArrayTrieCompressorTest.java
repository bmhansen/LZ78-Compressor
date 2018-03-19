import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArrayTrieCompressorTest {

  @Test
  public void testEmpty() throws Exception {
    byte[] in = new byte[] {};
    byte[] out = new byte[] {};
    assertArrayProducedFromArray(out, in);
  }

  @Test
  public void testAllSingleInputs() throws Exception {
    for (int byteValueToTest = -128; byteValueToTest <= 127; byteValueToTest++) {
      byte[] in = new byte[] { (byte) byteValueToTest };
      byte[] out = new byte[] { 0, (byte) byteValueToTest };
      assertArrayProducedFromArray(out, in);
    }
  }

  @Test
  public void testBuildsAndUsesDictionary() throws Exception {
    byte[] in = new byte[] { 65, 65, 66, 65, 66, 67, 66, 66, 66 };
    byte[] out = new byte[] { 0, 65, 1, 66, 2, 67, 0, 66, 4, 66 };
    assertArrayProducedFromArray(out, in);
  }

  @Test
  public void testStreamEndsPartwayThroughDictionarySequence() throws Exception {
    byte[] in = new byte[] { 65, 65, 66, 65, 66 };
    byte[] out = new byte[] { 0, 65, 1, 66, 1, 66 };
    assertArrayProducedFromArray(out, in);
  }

  private void assertArrayProducedFromArray(byte[] expectedOutputByteArray, byte[] inputByteArray) throws Exception {
    ByteArrayInputStream testInput = new ByteArrayInputStream(inputByteArray);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    FakePacker packer = new FakePacker(testOutput);
    ArrayTrieCompressor.LZ78Encode(testInput, packer);
    assertEquals(expectedOutputByteArray.length, testOutput.size());
    assertArrayEquals(expectedOutputByteArray, testOutput.toByteArray());
  }

}
