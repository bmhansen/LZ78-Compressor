import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BSTCompressorTest {

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
    //assertEquals((byte) (exampleByte << 1), testOutput.toByteArray()[0]);
    //assertEquals((exampleByte & 0x80) >> 7, testOutput.toByteArray()[1]);
  }

  @Test
  public void testBuildsAndUsesDictionary() throws Exception {
    byte[] in = new byte[] { 65, 65, 66, 65, 66, 67 };
    byte[] out = new byte[] { 0, 65, 1, 66, 2, 67 };
    assertArrayProducedFromArray(out, in);

    //assertEquals((byte) (byteStream[0] << 1), testOutput.toByteArray()[0]);
    //assertEquals((byte) (((byteStream[0] & 0x80) >> 7) | 0x2 | (byteStream[2] << 2)), testOutput.toByteArray()[1]);
    //assertEquals((byte) (((byteStream[2] & 0xC0) >> 6) | 0x8 | (byteStream[5] << 4)), testOutput.toByteArray()[2]);
    //assertEquals((byte) (((byteStream[5] & 0xF0) >> 4)), testOutput.toByteArray()[3]);
  }

  @Test
  public void testStreamEndsPartwayThroughSeenSequence() throws Exception {
    byte[] in = new byte[] { 65, 65 };
    byte[] out = new byte[] { 0, 65, 0, 65 };
    assertArrayProducedFromArray(out, in);
    //assertEquals((byte) (exampleByte << 1), testOutput.toByteArray()[0]);
    //assertEquals((byte) (((exampleByte & 0x80) >> 7) | (exampleByte << 2)), testOutput.toByteArray()[1]);
    //assertEquals((byte) (((exampleByte & 0xC0) >> 6)), testOutput.toByteArray()[2]);
  }

  private void assertArrayProducedFromArray(byte[] expectedOutputByteArray, byte[] inputByteArray) throws Exception {
    ByteArrayInputStream testInput = new ByteArrayInputStream(inputByteArray);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    FakePacker packer = new FakePacker(testOutput);
    BSTCompressor.LZ78Encode(testInput, packer);
    assertEquals(expectedOutputByteArray.length, testOutput.size());
    assertArrayEquals(expectedOutputByteArray, testOutput.toByteArray());
  }

}
