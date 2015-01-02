package arithmetic;

/**
 * Utilised tutorial to implement arithmetic coding technique
 * The Data Compression Book 2nd Edition (1996) by Mark Nelson
 * Chapter 5 - Arithmetic Coding
 */

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TreeMap;
import java.util.Map.Entry;

import utils.BitReader;
import utils.BitWriter;

public class Arithmetic
{

  private TreeMap<Byte, Range> rangeTable = new TreeMap<Byte, Range>();
  
  public Arithmetic()
  {
  }
  
  public void add(final byte b)
  {
    Range range = rangeTable.get(b);
    if (range == null)
    {
      range = new Range(1);
      rangeTable.put(b, range);
    }
    else
    {
      range.inc();
    }
  }
  
  public void add(final byte b, long count)
  {
    Range range = new Range(count);
    rangeTable.put(b, range);
  }
  
  public TreeMap<Byte, Range> set()
  {
    char old = 0;
    for (Entry<Byte, Range> entry : rangeTable.entrySet())
    {
      Range range = entry.getValue();
      old = range.set(old);
    }
    return rangeTable;
  }

  //Size of bbInput must not exceed 2^16 - 1
  public static void compress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();    
    
    Arithmetic arithmetic = new Arithmetic();
 
    while(bbInput.remaining() > 0)
    {
      byte b = bbInput.get();
      arithmetic.add(b);
    }
    
    TreeMap<Byte, Range> rangeTable = arithmetic.set();
    
    //System.out.println(rangeTable.size());
    
    bbOutput.putInt(rangeTable.size());
    for (Entry<Byte, Range> e : rangeTable.entrySet())
    {
      bbOutput.put(e.getKey().byteValue());
      bbOutput.putLong(e.getValue().count);
      //System.out.println((char) e.getKey().byteValue() + ":" + e.getValue().count + " " + (int) e.getValue().high + " " + (int) e.getValue().low);
    }
    
    int rangeTableCount = bbInput.position();
    bbOutput.putInt(rangeTableCount);
    
    if (rangeTableCount > (Math.pow(2,16)))
    {
      information.append("FATAL ERROR: Cannot compress block larger than 2^16 - 1");
      return;
    }
    
    bbInput.position(0);
    
    //System.out.println(rangeTableCount);

    char low = 0;
    char high = 0xFFFF;
    short underflow_bits = 0;

    BitWriter bitWriter = new BitWriter(bbOutput);

    //Range testRange = arithmetic.rangeTable.get((byte)'B');
    //System.out.println((char)'B' + " " + testRange.count + " " + (int) testRange.high + " " + (int) testRange.low);
    
    while(bbInput.remaining() > 0)
    {
      byte b = bbInput.get();
      Range range = rangeTable.get(b);
      
      long diff;
      diff = (long) (high - low) + 1;
      char temphigh = (char) ((diff * range.high) / rangeTableCount - 1);
      high = (char) (low + temphigh);
      char templow = (char) ((diff * range.low) / rangeTableCount);
      low = (char) (low + templow);
      
      boolean shiftingComplete = false;
      while (!shiftingComplete)
      {
        // TEST1: Test if most significant bit match, shift them out.
        if ((high & 0x8000) == (low & 0x8000))
        {
          bitWriter.put((high & 0x8000) != 0);//!=
          while (underflow_bits > 0)
          {
            bitWriter.put((~high & 0x8000) != 0);
            underflow_bits--;
          }
          low <<= 1;
          high <<= 1;
          high |= 1;
        }
        // TEST2: Test for potential underflow if most significant digits don't match and
        // second most significant digits are one apart.
        else if ((low & 0x4000) != 0 && (!((high & 0x4000) != 0)))
        {
          underflow_bits += 1;
          low &= 0x3fff;
          high |= 0x4000;
          
          low <<= 1;
          high <<= 1;
          high |= 1;
        }
        // ELSE: COMPLETE
        else
        {
          shiftingComplete = true;
        }
      }
    }
    
    bitWriter.put((low & 0x4000) != 0);
    underflow_bits++;
    while (underflow_bits-- > 0)
    {
      bitWriter.put((~low & 0x4000) != 0);
    }

    bitWriter.finish();
    
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }
  
  //Size of bbOutput must not exceed 2^16 - 1
  public static void decompress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();
    
    Arithmetic arithmetic = new Arithmetic();
    
    int rangeTableSize = bbInput.getInt();
    
    for (int count = 0; count < rangeTableSize; count++)
    {
      byte b = bbInput.get();
      long i = bbInput.getLong();
      arithmetic.add(b, i);
    }
    
    arithmetic.set();
    
    long rangeTableCountFinal = bbInput.getInt();
    long rangeTableCount = rangeTableCountFinal;
    
    //System.out.println(rangeTableCountFinal);
    
    if (rangeTableCountFinal > (Math.pow(2,16) - 1))
    {
      information.append("FATAL ERROR: Cannot decompress block to larger than 2^16 - 1");
      return;
    }
    
    BitReader bitReader = new BitReader(bbInput);
    
    char low = 0;
    char high = 0xFFFF;
    char code = 0;    
    for (int i = 0 ; i < 16 ; i++ )
    {
      code <<= 1;
      code += bitReader.get();
    }
    
    while (rangeTableCount-- != 0)
    {
      long diff = (long) ( high - low ) + 1;
      long tempTempCount = ((long) ( code - low ) + 1 );
      char count = (char) ((tempTempCount *  rangeTableCountFinal - 1) / diff );
      
      //System.out.println("Values:" + (long) diff + " " + (int) count);
      
      byte b = 0;
      Range range = null;
      for (Entry<Byte, Range> entry : arithmetic.rangeTable.entrySet())
      {
        if (entry.getValue().low <= count && entry.getValue().high > count)
        {
          b = entry.getKey().byteValue();
          range = entry.getValue();
        }
      }
      
      diff = (long)( high - low ) + 1;
      char tempHigh = (char) ((diff * range.high) / rangeTableCountFinal - 1);
      high = (char) (low + tempHigh);
      char tempLow = (char) ((diff * range.low) / rangeTableCountFinal);
      low = (char) (low + tempLow);
      
      boolean shiftingComplete = false;
      while (!shiftingComplete)
      {
        // TEST1: if most significant bits match, bits auto shift out.
        if ((high & 0x8000) == (low & 0x8000))
        {
          low <<= 1;
          high <<= 1;
          high |= 1;
          
          code <<= 1;
          code += bitReader.get();
        }
        // TEST2: if underflow threat shift out 2nd MSdigit
        else if ((low & 0x4000) == 0x4000 && (high & 0x4000) == 0)
        {
          code ^= 0x4000;
          low &= 0x3fff;
          high |= 0x4000;          
          
          low <<= 1;
          high <<= 1;
          high |= 1;
          
          code <<= 1;
          code += bitReader.get();
        }
        // ELSE: COMPLETE
        else
        {
          shiftingComplete = true;
        }
      }
      bbOutput.put(b);
      //System.out.println((char)b);
    }
    
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }
  
  /*
  public static void main(String[] args)
  {
    try
    {
      compressFile();
      decompressFile();
    }
    catch (IOException e)
    {
    }
  }
  
  public static void compressFile() throws IOException
  {
    Path pathIn = Paths.get("C:\\Users\\Jack\\Documents\\workspace\\FMPHuffman\\bin\\~SampleFiles\\SmallTest.txt");
    Path pathOut = Paths.get("C:\\Users\\Jack\\Documents\\workspace\\FMPHuffman\\bin\\~SampleFiles\\SmallTest.txt.ari");

    FileChannel fileChannelIn = FileChannel.open(pathIn, StandardOpenOption.READ);
    FileChannel fileChannelOut = FileChannel.open(pathOut, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

    ByteBuffer bbInput = ByteBuffer.allocate((int) fileChannelIn.size());
    fileChannelIn.read(bbInput);
    bbInput.flip();
    
    ByteBuffer bbOutput = ByteBuffer.allocate(Settings.BLOCK_SIZE * 2);

    StringBuilder information = new StringBuilder();
    
    compress(bbInput, bbOutput, information);

    bbOutput.flip();    
    fileChannelOut.write(bbOutput);
    fileChannelOut.close();
  }
  
  public static void decompressFile() throws IOException
  {
    Path pathIn = Paths.get("C:\\Users\\Jack\\Documents\\workspace\\FMPHuffman\\bin\\~SampleFiles\\SmallTest.txt.ari");
    Path pathOut = Paths.get("C:\\Users\\Jack\\Documents\\workspace\\FMPHuffman\\bin\\~SampleFiles\\SmallTest.txtx");
    
    FileChannel fileChannelIn = FileChannel.open(pathIn, StandardOpenOption.READ);
    FileChannel fileChannelOut = FileChannel.open(pathOut, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

    ByteBuffer bbInput = ByteBuffer.allocate((int) fileChannelIn.size());
    fileChannelIn.read(bbInput);
    bbInput.flip();
    
    ByteBuffer bbOutput = ByteBuffer.allocate(Settings.BLOCK_SIZE * 2);

    StringBuilder information = new StringBuilder();
    
    decompress(bbInput, bbOutput, information);

    bbOutput.flip();    
    fileChannelOut.write(bbOutput);
    fileChannelOut.close();
  }*/
 
}
