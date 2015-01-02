package squeeze;

import huffman.Huffman;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import archive.Archive;
import arithmetic.Arithmetic;

import lzw.LZW;

public class Squeeze
{
  
  /*public static void main (String[] args)
  {
    String path = "C:\\Users\\Jack\\Documents\\workspace\\FMPHuffman\\bin\\~SampleFiles\\";
    
    path = path + "test.bmp";
    
    StringBuilder information = new StringBuilder();
    try
    {
      Path inputPath = Paths.get(path);
      long[] time = {0};
      Path outputPath = Squeeze.squeeze(inputPath, information, time);
      information.append("Squeezing time: " + time[0] + "ms" + '\n');
      time[0] = 0;
      Squeeze.release(outputPath, information, time);
      information.append("Releasing time: " + time[0] + "ms" + '\n');
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }
    System.out.println(information.toString());
  }*/
  
  public static Path squeeze(Path pathInput, StringBuilder information, long[] time) throws IOException
  {
    information.append("Squeezing " + pathInput + '\n');
    
    Path pathOutput = Paths.get(pathInput.toString() + ".sqz");;

    Path pathOutputTemp = pathOutput;
    if (!Settings.NEW_NAME.equals(""))
    {
      pathOutputTemp = Paths.get(pathInput.getParent().toString() + "\\" + Settings.NEW_NAME + ".sqz");
    }    
    information.append("to " + pathOutputTemp + '\n');
    
    information.append("Settings: " + '\n');
    if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
    {
      information.append(" - Retain Timestamps" + '\n');
    }
    else
    {
      information.append(" - Do Not Retain Timestamps" + '\n');
    }
    if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_H)
    {
      information.append(" - LZW + Huffman" + '\n');
    }
    else if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_A)
    {
      information.append(" - LZW + Arithmetic" + '\n');
    }
    information.append(" - Block Size " + (Settings.BLOCK_SIZE / 1024) + "KB" + '\n');
   
    FileChannel fileChannelOutput = FileChannel.open(pathOutput, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    
    pathInput = Archive.createArchive(pathInput);
    
    ByteBuffer bbMagicNumber = ByteBuffer.wrap(Settings.SQUEEZE_MAGIC_NUMBER);
    fileChannelOutput.write(bbMagicNumber);
    
    ByteBuffer bbSettings = ByteBuffer.wrap(Settings.SETTINGS);
    fileChannelOutput.write(bbSettings);
    
    ByteBuffer bbDefaultBlockSize = ByteBuffer.allocate(Integer.SIZE >> 3);
    bbDefaultBlockSize.putInt(Settings.BLOCK_SIZE);
    bbDefaultBlockSize.flip();
    fileChannelOutput.write(bbDefaultBlockSize);
    
    FileChannel fileChannelInput = FileChannel.open(pathInput, StandardOpenOption.READ);
    ByteBuffer bbInputBlock = ByteBuffer.allocateDirect(Settings.BLOCK_SIZE);//mapped?
    boolean EOF = false;
    while(!EOF)
    {
      bbInputBlock.clear();
      int read = fileChannelInput.read(bbInputBlock);
      if (read == -1)
      {
        EOF = true;
      }
      else
      {
        bbInputBlock.flip();
        
        ByteBuffer bbOutputBlockLZW = ByteBuffer.allocate(Settings.BLOCK_SIZE * 2);        
        LZW.compress(bbInputBlock, bbOutputBlockLZW, information, time);
        bbOutputBlockLZW.flip();
        
        if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_H)
        {
          ByteBuffer bbOutputBlockHuffman = ByteBuffer.allocate(Settings.BLOCK_SIZE * 2);
          Huffman.compress(bbOutputBlockLZW, bbOutputBlockHuffman, information, time);
          bbOutputBlockHuffman.flip();
          
          ByteBuffer bbBlockSize = ByteBuffer.allocate(Integer.SIZE >> 3);
          bbBlockSize.putInt(bbOutputBlockHuffman.limit());
          bbBlockSize.flip();
          fileChannelOutput.write(bbBlockSize);
          //System.out.println(bbOutputBlockLZW.limit());
          
          fileChannelOutput.write(bbOutputBlockHuffman);
        }
        else if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_A)
        {
          ByteBuffer bbOutputBlockArithmetic = ByteBuffer.allocate(Settings.BLOCK_SIZE * 2);        
          Arithmetic.compress(bbOutputBlockLZW, bbOutputBlockArithmetic, information, time);
          bbOutputBlockArithmetic.flip();
          
          ByteBuffer bbBlockSize = ByteBuffer.allocate(Integer.SIZE >> 3);
          bbBlockSize.putInt(bbOutputBlockArithmetic.limit());
          bbBlockSize.flip();
          fileChannelOutput.write(bbBlockSize);
          //System.out.println(bbOutputBlockLZW.limit());
          
          fileChannelOutput.write(bbOutputBlockArithmetic);
        }
      }
    }
    
    fileChannelInput.close();
    fileChannelOutput.close();
    
    //Delete archive
    Files.delete(pathInput);
    
    //Move to specified path
    Files.move(pathOutput, pathOutputTemp);
    
    return pathOutputTemp;
  }
  
  public static void release(Path pathInput, StringBuilder information, long[] time) throws IOException
  {
    information.append("Releasing " + pathInput + '\n');
    

    FileChannel fileChannelInput = FileChannel.open(pathInput, StandardOpenOption.READ);
    if (!isCompressed(fileChannelInput))
    {
      information.append("FATAL ERROR: Selected File Not Compressed!");
      return;
    }
    
    ByteBuffer bbSettings = ByteBuffer.allocate(Settings.SETTINGS.length);
    fileChannelInput.read(bbSettings);
    bbSettings.flip();
    bbSettings.get(Settings.SETTINGS);
    
    information.append("Settings: " + '\n');
    if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
    {
      information.append(" - Retain Timestamps" + '\n');
    }
    else
    {
      information.append(" - Do Not Retain Timestamps" + '\n');
    }
    if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_H)
    {
      information.append(" - LZW + Huffman" + '\n');
    }
    else if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_A)
    {
      information.append(" - LZW + Arithmetic" + '\n');
    }
    
    ByteBuffer bbDefaultBlockSize = ByteBuffer.allocate(Integer.SIZE >> 3);
    fileChannelInput.read(bbDefaultBlockSize);
    bbDefaultBlockSize.flip();
    int defaultBlockSize = bbDefaultBlockSize.getInt();
    //System.out.println("DefaultBlockSize: " + defaultBlockSize);
    
    information.append(" - Block Size " + (defaultBlockSize / 1024) + "KB" + '\n');
    
    Path pathOutput = Paths.get(pathInput.getParent() + "\\squeeze.archive");
    FileChannel fileChannelOutput = FileChannel.open(pathOutput, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    
    boolean EOF = false;
    while(!EOF)
    {
      ByteBuffer bbBlockSize = ByteBuffer.allocate(Integer.SIZE >> 3);
      int read = fileChannelInput.read(bbBlockSize);
      if (read == -1)
      {
        EOF = true;
      }
      else
      {
        bbBlockSize.flip();
        int blockSize = bbBlockSize.getInt();
        //System.out.println("BlockSize: " + blockSize);
        ByteBuffer bbInputBlock = ByteBuffer.allocate(blockSize);

        read = fileChannelInput.read(bbInputBlock);
        if (read == -1)
        {
          EOF = true;
        }
        else
        {
          bbInputBlock.flip();

          if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_H)
          {
            ByteBuffer bbOutputBlockHuffman = ByteBuffer.allocate(defaultBlockSize * 2);
            Huffman.decompress(bbInputBlock, bbOutputBlockHuffman, information, time);
            bbOutputBlockHuffman.flip();
            
            ByteBuffer bbOutputBlockLZW = ByteBuffer.allocate(defaultBlockSize * 2);
            LZW.decompress(bbOutputBlockHuffman, bbOutputBlockLZW, information, time);
            bbOutputBlockLZW.flip();
            
            fileChannelOutput.write(bbOutputBlockLZW);
          }
          else if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_A)
          {
            ByteBuffer bbOutputBlockArithmetic = ByteBuffer.allocate(defaultBlockSize * 2);
            Arithmetic.decompress(bbInputBlock, bbOutputBlockArithmetic, information, time);
            bbOutputBlockArithmetic.flip();
            
            ByteBuffer bbOutputBlockLZW = ByteBuffer.allocate(defaultBlockSize * 2);
            LZW.decompress(bbOutputBlockArithmetic, bbOutputBlockLZW, information, time);
            bbOutputBlockLZW.flip();
            
            fileChannelOutput.write(bbOutputBlockLZW);
          }
         
        }
      }
    }
    
    fileChannelInput.close();
    fileChannelOutput.close();
    
    Archive.extractArchive(pathOutput);
    
    //Files.delete(pathOutput);
    //Files.delete(pathInput);
  }
  
  public static boolean isCompressed(final Path pathInput) throws IOException
  {
    FileChannel fileChannelInput = FileChannel.open(pathInput, StandardOpenOption.READ);
    boolean result = isCompressed(fileChannelInput);
    fileChannelInput.close();
    return result;
  }  
    
  public static boolean isCompressed(final FileChannel fileChannelInput) throws IOException
  {
    ByteBuffer bbMagicNumberRef = ByteBuffer.wrap(Settings.SQUEEZE_MAGIC_NUMBER);
    ByteBuffer bbMagicNumber = ByteBuffer.allocate(Settings.SQUEEZE_MAGIC_NUMBER.length);
    
    fileChannelInput.read(bbMagicNumber);
    bbMagicNumber.flip();

    return bbMagicNumberRef.equals(bbMagicNumber);
  }
}
