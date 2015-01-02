
/**
 * Utilised tutorial to implement LZW coding technique
 * CS3580 Notes by Zhiyuan Luo
 * Chapter 2 - Multimedia Transmission
 */

package lzw;

import java.io.IOException;
import java.nio.ByteBuffer;

public class LZW
{
  
  private LZW()
  {
    //Private constructor.
  }
  
  public static void compress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();

    int maxCode = 32767;//Short.MAX_VALUE;
    int nextCode = 0;
    
    for (nextCode = 0; nextCode < 256; nextCode++)
    {
      LinkedString linkedString = new LinkedString((byte) nextCode);
      linkedString.putCode(nextCode);
    }
    
    nextCode = 257;
   
    LinkedString oldLinkedString = null;
    LinkedString newLinkedString = null;
    
    while(bbInput.remaining() > 0)
    {
      byte b = bbInput.get();
      
      newLinkedString = new LinkedString(oldLinkedString, b);
      
      if (newLinkedString.getCode() == -1)
      {
        if (nextCode < maxCode)
        {
          newLinkedString.putCode(nextCode++);
        }//else reset codes??
        bbOutput.putShort(newLinkedString.getPrefixCode().shortValue());
        newLinkedString = new LinkedString(null, b);
      }
      oldLinkedString = newLinkedString;
    }
    if (newLinkedString != null)
    {
      bbOutput.putShort(newLinkedString.getCode().shortValue());
    }
    
    /*for (Entry<LinkedString, Integer> entry : LinkedString.codes.entrySet())
    {
      LinkedString key = entry.getKey();
      Integer value = entry.getValue();
      if (value > 255)
      {
        System.out.println(key + ":" + value);
      }
    }*/
    
    LinkedString.clearCodes();
    
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }
  
  public static void decompress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();
    
    int nextCode = 0;

    for (nextCode = 0; nextCode < 256; nextCode++)
    {
      LinkedString linkedString = new LinkedString((byte) nextCode);
      linkedString.putLinkedString(nextCode);
    }
    
    int next_code = 257;
    int max_code = 32767;

    LinkedString oldLinkedString = LinkedString.getLinkedString(bbInput.getShort());
    byte extraCharacter = oldLinkedString.getByte();
    bbOutput.put(extraCharacter);

    LinkedString newLinkedString = null;
    
    while(bbInput.remaining() > 0)
    {
      int newCode = bbInput.getShort();
      if (!LinkedString.containsCode(newCode))//STRCSTRCSTRC exception RARE//new_code>=next_code
      {
        bbOutput.put(oldLinkedString.toByteArray());
        bbOutput.put(extraCharacter);
        extraCharacter = oldLinkedString.getFirstByte();
      }
      else
      {
        newLinkedString = LinkedString.getLinkedString(newCode);
        bbOutput.put(newLinkedString.toByteArray());
        extraCharacter = newLinkedString.getFirstByte();
      }
      if (next_code <= max_code)
      {
        LinkedString linkedString = new LinkedString(oldLinkedString, extraCharacter);
        linkedString.putLinkedString(next_code++);
      }
      oldLinkedString = LinkedString.getLinkedString(newCode);
    }
    
    /*for (Entry<Integer, LinkedString> entry : LinkedString.strings.entrySet())
    {
      Integer key = entry.getKey();
      LinkedString value = entry.getValue();
      if (key > 250 || key == 116)
      {
        System.out.println(key + ":" + value);
      }
    }*/
    
    LinkedString.clearStrings();
    
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }
  
}