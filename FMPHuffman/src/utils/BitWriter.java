package utils;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class BitWriter
{
  int count = 0;
  //BufferedWriter byteWriter;
  
  ByteBuffer byteBuffer;
  
  BitSet bitSet = new BitSet(8);
  
  int index = 0;
  
  public BitWriter(ByteBuffer byteBuffer)
  {
    //this.byteWriter = byteWriter;
    this.byteBuffer = byteBuffer;
  }
  
  public void put(boolean bit)
  {
    if (index == Byte.SIZE)
    {
      byte[] b = bitSet.toByteArray();
      if (b.length == 0)
      {
        b = new byte[1];// default init to 00000000
      }
      byteBuffer.put(b[0]);
      //byteWriter.put(b[0]);
      bitSet.clear();
      index = 0;
    }
    bitSet.set(index++, bit);
    count++;
  }
  
  public int finish()
  {
    //super();
    byte[] b = bitSet.toByteArray();
    if (b.length == 0)
    {
      b = new byte[1];// default init to 00000000
    }
    byteBuffer.put(b[0]);
    return count;
    //byteWriter.put(b[0]);
  }
  
}
