public class ArrayTrie {

  private int[] value = new int[256];
  private ArrayTrie[] children = new ArrayTrie[256];

  public int getValue(byte pos) {
    return value[pos];
  }

  public void setValue(byte pos, int newValue) {
    value[pos] = newValue;
  }

  public ArrayTrie getChild(byte pos) {
    return children[pos];
  }

  public void setChild(byte pos, ArrayTrie newChild) {
    children[pos] = newChild;
  }
}