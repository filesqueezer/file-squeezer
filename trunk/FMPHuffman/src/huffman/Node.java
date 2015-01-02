package huffman;

/**
 * Abstract node class representing nodes in a Huffman tree.
 */
public abstract class Node implements Comparable<Object>
{
  /**
   * Constructor for the Node.
   * @param d value of the Node (used in comparator).
   */
  Node(final int frequency)
  {
    this.frequency = frequency;
  }

  /**
   * Frequency field.
   */
  private int frequency;

  /**
   * Getter for frequency field.
   * @return frequency field.
   */
  public final int getFrequency()
  {
    return frequency;
  }

  /**
   * Increments the frequency field by 1.
   */
  public final void increaseFrequency()
  {
    frequency++;
  }

  public final void setFrequency(int frequency)
  {
    this.frequency = frequency;
  }
  
  /**
   * Abstract method for adding a bit. Both InternalNode and ExternalNode must implement it.
   * @param bit to add.
   */
  public abstract void addBit(int bit);

  /**
   * Comparator used to sort by lowest frequency.
   * @param obj other object.
   * @return the frequency of this object minus the frequency of the object being compared.
   */
  public final int compareTo(final Object obj)
  {
    Node that = (Node) obj;
    return Integer.compare(this.frequency, that.frequency); //Sort by lowest frequency.
  }

}
