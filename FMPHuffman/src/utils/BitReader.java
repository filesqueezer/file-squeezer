package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class BitReader
{
  
  BitSet bitSet = new BitSet(8);
  
  int index = Byte.SIZE;
  
  ByteBuffer byteBuffer;
  
  byte[] b = new byte[1];
  
  public BitReader(ByteBuffer byteBuffer)
  {
    this.byteBuffer = byteBuffer;
  }

  //Used by arithmetic
  //returns 0 or 1
  // -1 in case of error.
  public int get() throws IOException
  {
    if (index == Byte.SIZE)
    {
      if (byteBuffer.remaining() > 0)
      {
        byteBuffer.get(b);
      }
      else
      {
        // No data remains in the buffer but we must continue returning 0 at least 16 times to
        // shift all the data through the char-typed code.
        return 0;
      }
      bitSet = BitSet.valueOf(b);
      index = 0;
    }
    boolean bit = bitSet.get(index++);
    if (bit)
    {
      return 1;
    }
    else
    {
      return 0;
    }
  }
  
  //Used by Huffman
  //returns bits read
  // parameter bit passes bit value
  public int get(boolean[] bit) throws IOException
  {
    if (index == Byte.SIZE)
    {
      if (byteBuffer.remaining() > 0)
      {
        byteBuffer.get(b);
      }
      else
      {
        return -1;
      }
      bitSet = BitSet.valueOf(b);
      index = 0;
    }
    bit[0] = bitSet.get(index++);
    return 0;
  }
  
  public void finalize()
  {
    //super();
    bitSet.clear();
    index = 0;
  }
  
  
}
