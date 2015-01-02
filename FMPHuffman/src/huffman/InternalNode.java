package huffman;

/**
 * Internal Node class representing internal nodes in a huffman tree.
 */
public class InternalNode extends Node
{

  /**
   * Constructor for InternalNode class, created from two child nodes.
   * @param leftNode Left child node.
   * @param rightNode Right child node.
   */
  public InternalNode(final Node leftNode, final Node rightNode)
  {
    super(leftNode.getFrequency() + rightNode.getFrequency());
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    leftNode.addBit(0);
    rightNode.addBit(1);
  }

  /**
   * Left child node.
   */
  private Node leftNode;

  /**
   * Right child node.
   */
  private Node rightNode;

  /**
   * Adds bits to the child node.
   * @param bit to add.
   */
  @Override
  public final void addBit(final int bit)
  {
    leftNode.addBit(bit);
    rightNode.addBit(bit);
  }

}
