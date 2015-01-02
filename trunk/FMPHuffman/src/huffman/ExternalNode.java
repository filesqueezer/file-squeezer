package huffman;

import java.util.BitSet;

/**
 * External Node class representing external nodes in a huffman tree.
 */
public class ExternalNode extends Node implements Comparable<Object>
{
  /**
   * Constructor sets the byte value of the node and initialises the Huffman bit pattern.
   * @param b byte value.
   * @param frequency usually 1, set to 0 in null byte scenario.
   */
  public ExternalNode(final Byte b, final int frequency)
  {
    super(frequency);
    this.character = b;
    binaryCode = new BinaryCode(new BitSet(EIGHT), 0);
  }

  /**
   * 8.
   */
  private static final  int EIGHT = 8;

  /**
   * Byte value.
   */
  private Byte character;

  /**
   * Corresponding Huffman binaryCode.
   */
  private BinaryCode binaryCode;

  /**
   * Getter for character.
   * @return character.
   */
  public final Byte getCharacter()
  {
    return character;
  }

  /**
   * Getter for binaryCode.
   * @return binaryCode.
   */
  public final BinaryCode getBinaryCode()
  {
    return binaryCode;
  }

  /**
   * Adds one bit to the binaryCode.
   * @param bit to add can be 0 or 1.
   */
  @Override
  public final void addBit(final int bit)
  {
    for (int i = binaryCode.getLength(); i >= 0; i--)
    {
      binaryCode.getBitSet().set(i + 1, binaryCode.getBitSet().get(i));
    }
    if (bit == 1)
    {
      binaryCode.getBitSet().set(0, true);
    }
    else
    {
      binaryCode.getBitSet().set(0, false);
    }
    binaryCode.increaseLength();
  }

}
