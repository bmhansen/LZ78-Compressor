import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArrayTrieCompressorTest {

  @Test
  public void testHelloWorld() {
    String str = "Junit is working fine";
    assertEquals("Junit is working fine", str);
  }

  @Test
  public void testEmpty() throws Exception{
    ByteArrayInputStream testInput = new ByteArrayInputStream(new byte[] {});
    ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    ArrayTrieCompressor.LZ78Encode(testInput, testOutput);
    assertEquals(0, testOutput.size());

  }
}
