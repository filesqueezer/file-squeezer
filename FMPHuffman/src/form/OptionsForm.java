package form;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import squeeze.Settings;

public class OptionsForm extends JDialog implements ActionListener
{

  /**
   * 
   */
  private static final long serialVersionUID = -2788834710826792572L;
  
  public OptionsForm(MainForm mainForm)
  {
    super(mainForm, "Options", ModalityType.APPLICATION_MODAL);

    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setSize(600, 500);
    //this.setMinimumSize(new Dimension(400, 300));
    setResizable(false);
    
    JLabel jLabelName = new JLabel("New Name: ");
    jTextFieldName = new JTextField(Settings.NEW_NAME, 30);
    
    jCheckBox = new JCheckBox("Retain File Timestamps");
    if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_T)
    {
      jCheckBox.setSelected(true);
    }
    else if (Settings.SETTINGS[Settings.STORE_TIMES] == Settings.STORE_TIMES_F)
    {
      jCheckBox.setSelected(false);
    }
    
    JPanel jPanelName = new JPanel(new BorderLayout());
    jPanelName.add(jLabelName, BorderLayout.WEST);
    jPanelName.add(jTextFieldName, BorderLayout.CENTER);
    jPanelName.add(jCheckBox, BorderLayout.SOUTH);
    
    JLabel jLabelBlockSize = new JLabel("Block Size: ");
    jRadioButton64KB = new JRadioButton("64KB", false);
    jRadioButton256KB = new JRadioButton("256KB", false);
    jRadioButton1MB = new JRadioButton("1MB", false);
    jRadioButton4MB = new JRadioButton("4MB", false);
    
    if (Settings.BLOCK_SIZE == 64 * 1024)
    {
      jRadioButton64KB.setSelected(true);
    }
    else if (Settings.BLOCK_SIZE == 256 * 1024)
    {
      jRadioButton256KB.setSelected(true);
    }
    else if (Settings.BLOCK_SIZE == 1024 * 1024)
    {
      jRadioButton1MB.setSelected(true);
    }
    else if (Settings.BLOCK_SIZE == 4 * 1024 * 1024)
    {
      jRadioButton4MB.setSelected(true);
    }
        
    
    ButtonGroup buttonGroupBlockSize = new ButtonGroup();
    buttonGroupBlockSize.add(jRadioButton64KB);
    buttonGroupBlockSize.add(jRadioButton256KB);
    buttonGroupBlockSize.add(jRadioButton1MB);
    buttonGroupBlockSize.add(jRadioButton4MB);
    
    JPanel jPanelBlockSize = new JPanel(new GridLayout(1, 4));
    jPanelBlockSize.add(jRadioButton64KB);
    jPanelBlockSize.add(jRadioButton256KB);    
    jPanelBlockSize.add(jRadioButton1MB);
    jPanelBlockSize.add(jRadioButton4MB);
    
    JLabel jLabelCompressionMethod = new JLabel("Compression Method: ");
    
    jRadioButtonLZWH = new JRadioButton("LZW + Huffman");
    jRadioButtonLZWH.setActionCommand("LZW + Huffman");
    jRadioButtonLZWH.addActionListener(this);
    jRadioButtonLZWA = new JRadioButton("LZW + Arithmetic (slow decompression)");
    jRadioButtonLZWA.setActionCommand("LZW + Arithmetic");
    jRadioButtonLZWA.addActionListener(this);
    

    if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_H)
    {
      jRadioButtonLZWH.setSelected(true);
      jRadioButtonLZWA.setSelected(false);
      jRadioButton256KB.setEnabled(true);
      jRadioButton256KB.setEnabled(true);
      jRadioButton1MB.setEnabled(true);
      jRadioButton4MB.setEnabled(true);
    }
    else if (Settings.SETTINGS[Settings.COMPRESSION_METHOD] == Settings.COMPRESSION_METHOD_A)
    {
      jRadioButtonLZWH.setSelected(false);
      jRadioButtonLZWA.setSelected(true);
      jRadioButton64KB.setSelected(true);
      jRadioButton256KB.setEnabled(false);
      jRadioButton1MB.setEnabled(false);
      jRadioButton4MB.setEnabled(false);
    }
    
    ButtonGroup buttonGroupCompressionMethod = new ButtonGroup();
    buttonGroupCompressionMethod.add(jRadioButtonLZWH);
    buttonGroupCompressionMethod.add(jRadioButtonLZWA);
    
    JPanel jPanelCompressionMethod = new JPanel(new GridLayout(1, 4));
    jPanelCompressionMethod.add(jLabelCompressionMethod);
    jPanelCompressionMethod.add(jRadioButtonLZWH);
    jPanelCompressionMethod.add(jRadioButtonLZWA);    
 
    JPanel jPanelCenter = new JPanel(new GridLayout(4, 1));
    jPanelCenter.add(jLabelCompressionMethod);
    jPanelCenter.add(jPanelCompressionMethod);
    jPanelCenter.add(jLabelBlockSize);
    jPanelCenter.add(jPanelBlockSize);
    
    
    jButton = new JButton("OK");
    jButton.addActionListener(this);
    
    JPanel jPanelOK = new JPanel(new BorderLayout());
    jPanelOK.add(jButton, BorderLayout.CENTER);

    JPanel jPanelMain = new JPanel(new BorderLayout());
    jPanelMain.setBorder(new EmptyBorder(10, 10, 10, 10));
    
    jPanelMain.add(jPanelName, BorderLayout.NORTH);
    jPanelMain.add(jPanelCenter, BorderLayout.CENTER);
    jPanelMain.add(jPanelOK, BorderLayout.SOUTH);
    
    add(jPanelMain, BorderLayout.CENTER);
    
    getRootPane().setDefaultButton(jButton);
    
    pack();
    setLocationRelativeTo(null);
    
    setVisible(true);
  }
  
  
  JTextField jTextFieldName;
  JCheckBox jCheckBox;
  
  JLabel jLabelBlockSize;
  JRadioButton jRadioButton64KB;
  JRadioButton jRadioButton256KB;
  JRadioButton jRadioButton1MB;
  JRadioButton jRadioButton4MB;
  
  JLabel jLabelCompressionMethod;
  JRadioButton jRadioButtonLZWH;
  JRadioButton jRadioButtonLZWA;
  
  JButton jButton;

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getActionCommand().equals("LZW + Huffman"))
    {
      System.out.println("test");
      jRadioButton256KB.setEnabled(true);
      jRadioButton256KB.setEnabled(true);
      jRadioButton1MB.setEnabled(true);
      jRadioButton4MB.setEnabled(true);
    }
    else if (e.getActionCommand().equals("LZW + Arithmetic"))
    {
      jRadioButton64KB.setSelected(true);
      jRadioButton256KB.setEnabled(false);
      jRadioButton1MB.setEnabled(false);
      jRadioButton4MB.setEnabled(false);
    }
    else if (e.getActionCommand().equals("OK"))
    {
      //ApplySettings.
      if (jCheckBox.isSelected())
      {
        Settings.SETTINGS[Settings.STORE_TIMES] = Settings.STORE_TIMES_T;
      }
      else if (!jCheckBox.isSelected())
      {
        Settings.SETTINGS[Settings.STORE_TIMES] = Settings.STORE_TIMES_F;
      }
      
      if (jRadioButtonLZWH.isSelected())
      {
        Settings.SETTINGS[Settings.COMPRESSION_METHOD] = Settings.COMPRESSION_METHOD_H; 
      }
      else if (jRadioButtonLZWA.isSelected())
      {
        Settings.SETTINGS[Settings.COMPRESSION_METHOD] = Settings.COMPRESSION_METHOD_A;
      }
      
      if (jRadioButton64KB.isSelected())
      {
        Settings.BLOCK_SIZE = 64 * 1024;
      }
      else if (jRadioButton256KB.isSelected())
      {
        Settings.BLOCK_SIZE = 256 * 1024;
      }
      else if (jRadioButton1MB.isSelected())
      {
        Settings.BLOCK_SIZE = 1024 * 1024;
      }
      else if (jRadioButton4MB.isSelected())
      {
        Settings.BLOCK_SIZE = 4 * 1024 * 1024;
      }
      
      Settings.NEW_NAME = jTextFieldName.getText();
      
      dispose();
    }
  }
  
}
