import java.io.IOException;
import java.io.OutputStream;

public class FakePacker implements Packer {
    // The output stream to write to
    OutputStream out = null;

    // constructor that assigns the output stream to one provided
    FakePacker(OutputStream outStream) {
    out = outStream;
    }

    // writes two bytes to the output stream, i cast to a byte followed by b
    public void write(int i, byte b) throws IOException {
        out.write(new byte[] {(byte)i, b});
    }
    public void flush() throws IOException {
        out.flush();
    }
}