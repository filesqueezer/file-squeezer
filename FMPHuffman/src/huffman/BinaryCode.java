package huffman;

import java.util.BitSet;

/**
 * Binary code class, used to represent bit patterns created by the Huffman algorithm.
 */
public class BinaryCode
{

  /**
   * Constructor for BinaryCode.
   * @param bitSet sets the bitSet field.
   * @param length sets the length field.
   */
  public BinaryCode(final BitSet bitSet, final int length)
  {
    this.bitSet = bitSet;
    this.length = length;
  }

  /**
   * bitSet field holds the bit pattern.
   */
  private BitSet bitSet;

  /**
   * Number of bits in the bitSet field.
   */
  private int length;

  /**
   * Getter for bitSet.
   * @return bitSet.
   */
  public final BitSet getBitSet()
  {
    return bitSet;
  }

  /**
   * Getter for length.
   * @return length.
   */
  public final int getLength()
  {
    return length;
  }

  /**
   * Setter for length.
   * @param length new length field value.
   */
  public final void setLength(final int length)
  {
    this.length = length;
  }

  /**
   * Increments the length field.
   */
  public final void increaseLength()
  {
    length++;
  }

  /**
   * Returns the binaryCode as a string. Very useful for debugging.
   * @return binaryCode as a string.
   */
  public final String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < length; i++)
    {
      if (bitSet.get(i))
      {
        stringBuilder.append("1");
      }
      else
      {
        stringBuilder.append("0");
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Auto Generated via eclipse. Source>Generate hashCode() and equals()...
   * @return hash value.
   */
  @Override
  public final int hashCode()
  {
    final int prime = 31;
    int result = 1;
    if (bitSet == null)
    {
      result = prime * result + 0;
    }
    else
    {
      result = prime * result + bitSet.hashCode();
    }
    result = prime * result + length;
    return result;
  }

  /**
   * Auto Generated via eclipse. Source>Generate hashCode() and equals()...
   * @param obj Object to test equality with.
   * @return boolean value indicating whether or not two objects are equal.
   */
  @Override
  public final boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    BinaryCode other = (BinaryCode) obj;
    if (bitSet == null)
    {
      if (other.bitSet != null)
      {
        return false;
      }
    }
    else if (!bitSet.equals(other.bitSet))
    {
      return false;
    }
    if (length != other.length)
    {
      return false;
    }
    return true;
  }
}
