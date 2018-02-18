public class BitReader {

  public static void printBits(byte b) {
    for (int j = 7; j >= 0; j--) {
      System.err.print(b >> j & 1);
    }
    System.err.println();
  }

  public static void printBits(short s) {
    for (int j = 15; j >= 0; j--) {
      System.err.print(s >> j & 1);
    }
    System.err.println();
  }

  public static void printBits(int i) {
    for (int j = 31; j >= 0; j--) {
      System.err.print(i >> j & 1);
    }
    System.err.println();
  }

  public static void printBits(long l) {
    for (int j = 63; j >= 0; j--) {
      System.err.print(l >> j & 1);
    }
    System.err.println();
  }
}