package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import squeeze.Squeeze;
import form.MainForm;
import form.SplashScreen;

/**
 * Main class containing the main method.
 */
public final class Main
{

  /**
   * Private constructor.
   */
  private Main()
  {
  }

  /**
   * Main method creates new instance of MainForm.
   * @param args command line arguments
   */
  public static void main(final String[] args)
  {
    if (args.length == 0)
    {
      new SplashScreen();
      new MainForm();
    }
    else
    {
      StringBuilder information = new StringBuilder();
      Path file = Paths.get(args[0]);
      if (Files.notExists(file))
      {
        information.append("Parameter is invalid path.");
      }
      else if (Files.isRegularFile(file))
      {
        try
        {
          if (Squeeze.isCompressed(file))
          {
            long[] time = {0};
            Squeeze.release(file, information, time);
            information.append("Compression time: " + time[0] + "ms");
          }
          else
          {
            long[] time = {0};
            Path outputPath = Squeeze.squeeze(file, information, time);
            information.append("Compression time: " + time[0] + "ms");
          }
        }
        catch (IOException e)
        {
        }
      }
      else if (Files.isDirectory(file))
      {
        {
          long[] time = {0};
          try
          {
            Path outputPath = Squeeze.squeeze(file, information, time);
          }
          catch (IOException e)
          {
          }
          information.append("Compression time: " + time[0] + "ms");
        }
      }
      System.out.println(information.toString());
    }
  }

}
