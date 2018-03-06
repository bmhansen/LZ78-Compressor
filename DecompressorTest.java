import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DecompressorTest {

  @Test
  public void testEmpty() throws Exception {
    byte[] exampleStream = new byte[] {};
    ByteArrayInputStream testInput = new ByteArrayInputStream(exampleStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(testInput, testOutput);
    assertEquals(0, testOutput.size());
  }

  @Test
  public void testSingle() throws Exception {
    byte exampleByte = 65;
    byte[] exampleStream = new byte[] { 
      (byte) (exampleByte << 1), 
      (byte) ((exampleByte & 0x80) >> 7)
    };
    ByteArrayInputStream testInput = new ByteArrayInputStream(exampleStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(testInput, testOutput);
    assertEquals(1, testOutput.size());
    assertEquals(exampleByte, testOutput.toByteArray()[0]);
  }

  @Test
  public void testBuildsAndUsesDictionary() throws Exception{
    byte exampleByte = 65;
    byte[] exampleStream = new byte[] {
      (byte) (exampleByte << 1), 
      (byte) (((exampleByte & 0x80) >> 7) | 0x2 | (exampleByte << 2)), 
      (byte) ((exampleByte & 0xC0) >> 6)
    };
    ByteArrayInputStream testInput = new ByteArrayInputStream(exampleStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(testInput, testOutput);
    assertEquals(3, testOutput.size());
    assertEquals(exampleByte, testOutput.toByteArray()[0]);
    assertEquals(exampleByte, testOutput.toByteArray()[1]);
    assertEquals(exampleByte, testOutput.toByteArray()[2]);
  }

  @Test
  public void testStreamEndsPartwayThroughSequence() throws Exception{
    byte exampleByte = 65;
    byte[] exampleStream = new byte[] {
      (byte) (exampleByte << 1), 
      (byte) (((exampleByte & 0x80) >> 7) | (exampleByte << 2)), 
      (byte) ((exampleByte & 0xC0) >> 6)
    };
    ByteArrayInputStream testInput = new ByteArrayInputStream(exampleStream);
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    Decompressor.LZ78Decode(testInput, testOutput);
    assertEquals(2, testOutput.size());
    assertEquals(exampleByte, testOutput.toByteArray()[0]);
    assertEquals(exampleByte, testOutput.toByteArray()[1]);
  }

}
