package form;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import squeeze.Squeeze;


/**
 * JButtonListener contains the code to handle the JButton pressed event in the MainForm.
 */
class JButtonListener implements ActionListener
{

  /**
   * MainForm object.
   */
  private MainForm mainForm;

  /**
   * Sets the MainForm field so that the MainForm methods can be accessed.
   * @param mainForm MainForm object.
   */
  JButtonListener(final MainForm mainForm)
  {
    this.mainForm = mainForm;
  }

  /**
   * Executed when the JButton is pressed.
   * @param e ActionEvent object that gives source and event information.
   */
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    if (mainForm.getJButton().equals("Compress"))
    {
      Path file = Paths.get(mainForm.getJTextField());
      if (Files.isRegularFile(file))
      {
        StringBuilder information = new StringBuilder();
        mainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try
        {
          long[] time = {0};
          Path outputPath = Squeeze.squeeze(file, information, time);
          information.append("Compression time: " + time[0] + "ms");
          mainForm.jFileChooserRefresh();
        }
        catch (IOException e1)
        {
        }
        mainForm.setCursor(Cursor.getDefaultCursor());
        mainForm.setJTextArea(information.toString());
        mainForm.setJTextField("");
        }
      else if (Files.isDirectory(file))
      {
        StringBuilder information = new StringBuilder();
        mainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try
        {
          long[] time = {0};
          Path outputPath = Squeeze.squeeze(file, information, time);
          information.append("Compression time: " + time[0] + "ms");
          mainForm.jFileChooserRefresh();
         }
        catch (IOException e1)
        {
        }
        mainForm.setCursor(Cursor.getDefaultCursor());
        mainForm.setJTextArea(information.toString());
        mainForm.setJTextField("");
      }
    }
    else if (mainForm.getJButton().equals("Decompress"))
    {
      Path file = Paths.get(mainForm.getJTextField());
      StringBuilder information = new StringBuilder();
      mainForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      try
      {
        long[] time = {0};
        Squeeze.release(file, information, time);
        information.append("Decompression time: " + time[0] + "ms");
        mainForm.jFileChooserRefresh();
      }
      catch (IOException e1)
      {
      }
      mainForm.setCursor(Cursor.getDefaultCursor());
      mainForm.setJTextArea(information.toString());
      mainForm.setJTextField("");
      JOptionPane.showMessageDialog(null, "Known Bug: You must manually delete the squeeze.archive file before continuing.");
    }
  }

}
