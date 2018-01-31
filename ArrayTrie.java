public class ArrayTrie {

  private int[] value = new int[256];
  private ArrayTrie[] children = new ArrayTrie[256];

  public int getValue(byte pos) {
    return value[pos&255];
  }

  public void setValue(byte pos, int newValue) {
    value[pos&255] = newValue;
  }

  public ArrayTrie getChild(byte pos) {
    return children[pos&255];
  }

  public void setChild(byte pos, ArrayTrie newChild) {
    children[pos&255] = newChild;
  }
}