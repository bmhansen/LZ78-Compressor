import java.io.IOException;

interface Packer{
    public void write(int i, byte b) throws IOException;
    public void flush() throws IOException;
}