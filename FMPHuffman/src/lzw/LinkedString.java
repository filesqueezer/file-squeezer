package lzw;

import java.util.HashMap;

public class LinkedString
{
  
  public static HashMap<LinkedString, Integer> codes = new HashMap<LinkedString, Integer>();
  
  public static HashMap<Integer, LinkedString> strings = new HashMap<Integer, LinkedString>();
  
  public static void clearCodes()
  {
    codes.clear();
  }
  
  public static void clearStrings()
  {
    strings.clear();
  }
  
  private int prefixCode;
  
  private byte b;
  
  private byte firstByte;
  
  private int index;
  
  private int code;
  
  public Integer getCode()
  {
    return code;
  }
  
  public LinkedString(byte b)
  {
    this.prefixCode = -1;
    this.b = b;
    this.firstByte = b;
    index = 0;
  }
  
  public static boolean containsCode(int newCode)
  {
    return strings.containsKey(newCode);
  }
  
  public static boolean containsLinkedString(LinkedString linkedString)
  {
    return codes.containsKey(linkedString);
  }
  
  public static LinkedString getLinkedString(int code)
  {
    return strings.get(code);
  }
  
  public void putLinkedString(int code)
  {
    this.code = code;
    strings.put(code, this);
  }

  public void putCode(int code)
  {
    this.code = code;
    codes.put(this, code);
  }
  
  public LinkedString(LinkedString oldCode, byte b)
  {
    this.b = b;
    if (oldCode == null)
    {
      this.prefixCode = -1;
      this.firstByte = b;
      index = 0;
    }
    else
    {
      this.prefixCode = oldCode.code;
      this.firstByte = oldCode.firstByte;
      index = oldCode.index + 1;
    }
    Integer code = codes.get(this);
    if (code == null)
    {
      this.code = -1;
    }
    else
    {
      this.code = code;
    }
  }
  
  public LinkedString(int index, int prefixCode, byte b, byte firstByte)
  {
    this.prefixCode = prefixCode;
    this.b = b;
    this.firstByte = firstByte;
    this.index = index;
  }
  
  public int getIndex()
  {
    return index;
  }
  

  public LinkedString()
  {
  }



  public LinkedString(LinkedString linkedString)
  {
    this.prefixCode = linkedString.prefixCode;
    this.b = linkedString.b;
    this.firstByte = linkedString.firstByte;
    this.index = linkedString.index;
    this.code = linkedString.code;
  }

  public Integer getPrefixCode()
  {
    return prefixCode;
  }
  
  
  public void setPrefixCode(int prefixCode)
  {
    this.prefixCode = prefixCode;
  }
    
  public byte getByte()
  {
    return b;
  }
  
  public void setByte(byte b)
  {
    this.b = b;
  }
  
  public byte getFirstByte()
  {
    return firstByte;
  }
  
  public byte[] toByteArray()
  {
    byte[] byteArray = new byte[getIndex() + 1];
    toByteArrayRecursion(byteArray);
    return byteArray;
  }
  
  public int toByteArrayRecursion(byte[] byteArray)
  {
    if (prefixCode == -1)
    {
      byteArray[0] = b;
      return 0;
    }
    else
    {
      byteArray[index] = b;
      return LinkedString.getLinkedString(prefixCode).toByteArrayRecursion(byteArray);
    }
  }
  
  /*
  byte[] b = new byte[getIndex() + 1];
  while(getPrefixCode() != -1)
  {
    b[getIndex()] = getByte();
    newLinkedString = LinkedString.getLinkedString(newLinkedString.getPrefixCode());
  }
  bytearray[0] = newLinkedString.getByte();*/
  
  public String toString()
  {
    return "Code" + code + " PrefixCode:" + prefixCode + " Byte:" + (char) b + " FirstByte:" + (char) firstByte;
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + b;
    result = prime * result + prefixCode;
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LinkedString other = (LinkedString) obj;
    if (b != other.b)
      return false;
    if (prefixCode != other.prefixCode)
      return false;
    return true;
  }





  
}
