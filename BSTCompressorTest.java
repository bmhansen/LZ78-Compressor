import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BSTCompressorTest {

  @Test
  public void testEmpty() throws Exception{
    byte[] exampleStream = new byte[] {};
    ByteArrayInputStream testInput = new ByteArrayInputStream(exampleStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    BSTCompressor.LZ78Encode(testInput, testOutput);
    assertEquals(0, testOutput.size());
  }
  
  @Test
  public void testSingle() throws Exception{
    byte exampleByte = 65;
    ByteArrayInputStream testInput = new ByteArrayInputStream(new byte[] {exampleByte});
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    BSTCompressor.LZ78Encode(testInput, testOutput);
    assertEquals(2, testOutput.size());
    assertEquals((byte) (exampleByte << 1), testOutput.toByteArray()[0]);
    assertEquals((exampleByte & 0x80) >> 7, testOutput.toByteArray()[1]);
  }

  @Test
  public void testBuildsAndUsesDictionary() throws Exception{
    byte[] byteStream = new byte[] {65,65,66,65,66,67};
    ByteArrayInputStream testInput = new ByteArrayInputStream(byteStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    BSTCompressor.LZ78Encode(testInput, testOutput);
    assertEquals(4, testOutput.size());
    assertEquals((byte) (byteStream[0] << 1), testOutput.toByteArray()[0]);
    assertEquals((byte) (((byteStream[0] & 0x80) >> 7) | 0x2 | (byteStream[2] << 2)), testOutput.toByteArray()[1]);
    assertEquals((byte) (((byteStream[2] & 0xC0) >> 6) | 0x8 | (byteStream[5]<< 4)), testOutput.toByteArray()[2]);
    assertEquals((byte) (((byteStream[5] & 0xF0) >> 4)), testOutput.toByteArray()[3]);
  }

  @Test
  public void testStreamEndsPartwayThroughSequence() throws Exception{
    byte[] byteStream = new byte[] {65,65};
    ByteArrayInputStream testInput = new ByteArrayInputStream(byteStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    BSTCompressor.LZ78Encode(testInput, testOutput);
    assertEquals(3, testOutput.size());
    assertEquals((byte) (byteStream[0] << 1), testOutput.toByteArray()[0]);
    assertEquals((byte) (((byteStream[0] & 0x80) >> 7) | (byteStream[1] << 2)), testOutput.toByteArray()[1]);
    assertEquals((byte) (((byteStream[1] & 0xC0) >> 6)), testOutput.toByteArray()[2]);
  }

}
