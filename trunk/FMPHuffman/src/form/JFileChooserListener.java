package form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

public class JFileChooserListener implements ActionListener
{

  /**
   * MainForm object.
   */
  private MainForm mainForm;

  /**
   * Sets the MainForm field so that the MainForm methods can be accessed.
   * @param mainForm MainForm object.
   */
  public JFileChooserListener(MainForm mainForm)
  {
    this.mainForm = mainForm;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION))
    {
      mainForm.setJTextField(mainForm.getJFileChooserSelectedFilePath());
    }
    if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
    {
      mainForm.setJTextField("");
    }
  }

}
