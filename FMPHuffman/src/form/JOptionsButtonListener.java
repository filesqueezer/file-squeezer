package form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JOptionsButtonListener implements ActionListener
{

  private MainForm mainForm;

  /**
   * Sets the MainForm field so that the MainForm methods can be accessed.
   * @param mainForm MainForm object.
   */
  JOptionsButtonListener(final MainForm mainForm)
  {
    this.mainForm = mainForm;
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    new OptionsForm(mainForm);
  }

}
