package archive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import squeeze.Settings;

public class Archive
{
  /*
  public static void main(String[] args)
  {
    new Archive();
  }
  
  public Archive()//String directory)
  {
    Path binDirectory = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
    Path defaultDirectory = binDirectory.resolve("~SampleFiles");
    if (Files.notExists(defaultDirectory))
    {
      defaultDirectory = binDirectory;
    }
    
    System.out.println("binDirectory: " + binDirectory);
    System.out.println("defaultDirectory: " + defaultDirectory);
    
    Path archivePath = defaultDirectory;
    
    try
    {
      archivePath = createArchive(defaultDirectory);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    try
    {
      extractArchive(archivePath);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }*/

  public static void listFiles(Path directoryName, ArrayList<Path> arrayListPath)
  {
    try
    {
      DirectoryStream<Path>stream = Files.newDirectoryStream(directoryName);
      for (Path entry : stream)
      {
        if (Files.isDirectory(entry))
        {
          listFiles(entry, arrayListPath);
        }
        else
        {
          arrayListPath.add(entry);
        }
      }
     stream.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static Path createArchive(Path pathInput) throws IOException
  {
    Path pathOutput = pathInput;
    
    ArrayList<Path> arrayListFile = new ArrayList<Path>();
    if (Files.isDirectory(pathInput))
    {
      listFiles(pathInput, arrayListFile);
      pathOutput = pathOutput.resolve("squeeze.archive");
    }
    else
    {
      arrayListFile.add(pathInput);      
      pathOutput = pathOutput.getParent().resolve("squeeze.archive");
    }

    //System.out.println(pathOutput);
    FileChannel fileChannelOutput = FileChannel.open(pathOutput, StandardOpenOption.CREATE, StandardOpenOption.WRITE);//fileOutputStream.getChannel();
    
    for (Path fileInput : arrayListFile)
    {
      BasicFileAttributes basicFileAttributes = Files.readAttributes(fileInput, BasicFileAttributes.class);
      
      int headerSize = (Integer.SIZE >> 3) //FileNameSize in bytes
                     + fileInput.toString().length() //FileName size in bytes
                     + (Long.SIZE >> 3); //FileSize in bytes
      
      if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
      {
        headerSize += (Long.SIZE >> 3); //CreationTime
        headerSize += (Long.SIZE >> 3); //LastModifiedTime
        headerSize += (Long.SIZE >> 3); //AccessTime
      }
      
      ByteBuffer header = ByteBuffer.allocate(headerSize);

      header.putInt(fileInput.toString().length());
      header.put(fileInput.toString().getBytes());
      //System.out.println(fileInput);
      
      if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
      {
        header.putLong(basicFileAttributes.creationTime().toMillis());
        header.putLong(basicFileAttributes.lastModifiedTime().toMillis());
        header.putLong(basicFileAttributes.lastAccessTime().toMillis());
        //System.out.println(basicFileAttributes.lastModifiedTime().toString());
      }
      
      //System.out.println(Files.size(fileInput));
      header.putLong(Files.size(fileInput));

      header.flip();

      fileChannelOutput.write(header);

      FileChannel fileChannelInput = FileChannel.open(fileInput, StandardOpenOption.READ);
      fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);

      fileChannelInput.close();
    }
    fileChannelOutput.close();

    //deleteFiles
    for (Path afile : arrayListFile)
    {
      Files.delete(afile);
    }
    return pathOutput;
  }

  public static void extractArchive(Path inputPath) throws IOException
  {
    //System.out.println("EXTRACT: " + inputPath);
    FileChannel fileChannelInput = FileChannel.open(inputPath, StandardOpenOption.READ);

    long position = 0;
    while (position < fileChannelInput.size())
    {
      MappedByteBuffer mbbFileNameSize = fileChannelInput.map(FileChannel.MapMode.READ_ONLY, position, (Integer.SIZE >> 3));
      position = position + (Integer.SIZE >> 3);
      int fileNameSize = mbbFileNameSize.getInt();

      //System.out.println(position);
      //System.out.println(fileNameSize);
      
      MappedByteBuffer mbbFileName = fileChannelInput.map(FileChannel.MapMode.READ_ONLY, position, fileNameSize);
      position = position + fileNameSize;
      byte[] bFileName = new byte [fileNameSize];
      mbbFileName.get(bFileName);
      String stringfilePath = new String(bFileName);
      //DEBUG
      //stringfilePath = stringfilePath + "x";
      Path filePath = Paths.get(stringfilePath);
      //System.out.println(filePath);
      
      long creationTime = 0;
      long lastModifiedTime = 0;
      long lastAccessTime = 0;
      if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
      {
        MappedByteBuffer mbbCreationTime = fileChannelInput.map(FileChannel.MapMode.READ_ONLY, position, (Long.SIZE >> 3) * 3);
        position = position + ((Long.SIZE >> 3) * 3);
        creationTime = mbbCreationTime.getLong();
        lastModifiedTime = mbbCreationTime.getLong();
        lastAccessTime = mbbCreationTime.getLong();        
        //System.out.println(FileTime.fromMillis(creationTime).toString());
      }
      
      MappedByteBuffer mbbFileSize = fileChannelInput.map(FileChannel.MapMode.READ_ONLY, position, (Long.SIZE >> 3));
      position = position + (Long.SIZE >> 3);
      long fileSize = mbbFileSize.getLong();
      //System.out.println(fileSize);
      
      FileChannel fileChannelOutput = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      fileChannelInput.transferTo(position, fileSize, fileChannelOutput);
      position = position + fileSize;

      fileChannelOutput.close();
      
      if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
      {
        FileTime ftCreationTime = FileTime.fromMillis(creationTime);
        FileTime ftLastModifiedTime = FileTime.fromMillis(lastModifiedTime);
        FileTime ftLastAccessTime = FileTime.fromMillis(lastAccessTime);
        Files.setAttribute(filePath, "creationTime", ftCreationTime);
        Files.setAttribute(filePath, "lastModifiedTime", ftLastModifiedTime);
        Files.setAttribute(filePath, "lastAccessTime", ftLastAccessTime);
      }

    }
    fileChannelInput.close();
  }

}
