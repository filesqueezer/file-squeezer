package form;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import squeeze.Squeeze;


/**
 * JTextFieldListener contains the code to handle the JTextField changed event in the MainForm.
 */
public class JTextFieldListener implements DocumentListener
{

  /**
   * MainForm object.
   */
  private MainForm mainForm;

  /**
   * Sets the MainForm field so that the MainForm methods can be accessed.
   * @param mainForm MainForm object.
   */
  JTextFieldListener(final MainForm mainForm)
  {
    this.mainForm = mainForm;
  }

  /**
   * Executed when the text inside JTextField is changed.
   * @param e ActionEvent object that gives source and event information.
   */
  public final void changedUpdate(final DocumentEvent e)
  {
    updated();
  }

  /**
   * Executed when the text inside JTextField is deleted.
   * @param e ActionEvent object that gives source and event information.
   */
  public final void removeUpdate(final DocumentEvent e)
  {
    updated();
  }

  /**
   * Executed when the text inside JTextField is inserted.
   * @param e ActionEvent object that gives source and event information.
   */
  public final void insertUpdate(final DocumentEvent e)
  {
    updated();
  }

  /**
   * Updated method; executed whenever a change is made to the JTextField.
   */
  public final void updated()
  {
    if (mainForm.getJTextField().equals(""))
    {
      mainForm.setJOptionsButtonEnabled(false);
      mainForm.setJButtonEnabled(false);
      mainForm.setJButton("Compress");
    }
    else
    {
      Path file = Paths.get(mainForm.getJTextField());
      if (Files.notExists(file))
      {
        mainForm.setJOptionsButtonEnabled(false);
        mainForm.setJButtonEnabled(false);
      }
      else if (Files.isRegularFile(file))
      {
        try
        {
          if (Squeeze.isCompressed(file))
          {
            mainForm.setJButtonEnabled(true);
            mainForm.setJOptionsButtonEnabled(false);
            mainForm.setJButton("Decompress");
          }
          else
          {
            mainForm.setJOptionsButtonEnabled(true);
            mainForm.setJButtonEnabled(true);
            mainForm.setJButton("Compress");
          }
        }
        catch (IOException e)
        {
        }
      }
      else if (Files.isDirectory(file))
      {
        {
          mainForm.setJOptionsButtonEnabled(true);
          mainForm.setJButtonEnabled(true);
          mainForm.setJButton("Compress");
        }
      }
    }
  }

}
