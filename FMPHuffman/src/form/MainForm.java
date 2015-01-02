package form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main form for GUI.
 */
@SuppressWarnings("serial")
public class MainForm extends JFrame
{

  /**
   * 400.
   */
  public static final int FOURHUNDRED = 400;

  /**
   * 300.
   */
  public static final int THREEHUNDRED = 300;

  /**
   * 12.
   */
  public static final int TWELVE = 12;

  /**
   * 30.
   */
  public static final int THIRTY = 30;

  /**
   * 40.
   */
  public static final int FORTY = 40;

  /**
   * Constructor initialises components and displays form.
   */
  public MainForm()
  {
    super("File Squeezer"); //setTitle
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 500);
    this.setMinimumSize(new Dimension(FOURHUNDRED, THREEHUNDRED));
    setResizable(true);

    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
    {
      e.printStackTrace();
    }
    
    Font font = new Font("Courier New", Font.PLAIN, TWELVE); //fixed-width font

    JPanel jPanel = new JPanel();
    
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
    jPanel.setLayout(gridBagLayout);
    
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = GridBagConstraints.BOTH;

    String binDirectory = new String("");
    try
    {
      binDirectory = new String(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
    }
    catch (UnsupportedEncodingException e2)
    {
      e2.printStackTrace();
    }

    String defaultDirectory = new String(binDirectory + "~SampleFiles");
    File file = new File(defaultDirectory);
    if (!(file.exists() && file.isDirectory()))
    {
      defaultDirectory = binDirectory;
    }
    
    jFileChooser = new JFileChooser(defaultDirectory);
    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    jFileChooser.setPreferredSize(new Dimension(600, 300));
    //jFileChooser.setMinimumSize(new Dimension(600, 200));
    jFileChooser.setApproveButtonText("Select");
    UIManager.put("FileChooser.cancelButtonText", "Clear Selection");
    UIManager.put("FileChooser.openButtonToolTipText", "Select");
    UIManager.put("FileChooser.cancelButtonToolTipText","Clear Selection");
    SwingUtilities.updateComponentTreeUI(jFileChooser);
    JFileChooserListener jFileChooserListener = new JFileChooserListener(this);
    jFileChooser.addActionListener(jFileChooserListener);
    
    jPanel.add(jFileChooser, gridBagConstraints);
    
    gridBagConstraints.gridy++;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    
    jTextField = new JTextField();
    jTextField.setFont(font);
    jTextField.setText("");
    JTextFieldListener jTextFieldListener = new JTextFieldListener(this);
    jTextField.getDocument().addDocumentListener(jTextFieldListener);

    jPanel.add(jTextField, gridBagConstraints);

    gridBagConstraints.gridx++;
    gridBagConstraints.fill = GridBagConstraints.NONE;

    jOptionsButton = new JButton();
    jOptionsButton.setFont(font);
    jOptionsButton.setText("Options");
    jOptionsButton.setFocusable(false);
    jOptionsButton.setEnabled(false);
    JOptionsButtonListener jOptionsButtonListener = new JOptionsButtonListener(this);
    jOptionsButton.addActionListener(jOptionsButtonListener);  
    jPanel.add(jOptionsButton, gridBagConstraints);
    
    gridBagConstraints.gridx++;
    gridBagConstraints.fill = GridBagConstraints.NONE;

    jButton = new JButton();
    jButton.setFont(font);
    jButton.setText("Compress");
    jButton.setEnabled(false);
    jButton.setFocusable(false);
    JButtonListener jButtonListener = new JButtonListener(this);
    jButton.addActionListener(jButtonListener);
    jPanel.add(jButton, gridBagConstraints);
    
    jTextArea = new JTextArea();
    jTextArea.setFont(font);
    jTextArea.setText("");
    jTextArea.setRows(8);
    //jTextArea.setColumns(FORTY);
    jTextArea.setWrapStyleWord(true);
    jTextArea.setLineWrap(true);
    jTextArea.setEditable(false);
    jTextArea.setFocusable(false);
    jTextArea.setOpaque(false);

    JScrollPane jScrollPane = new JScrollPane();
    jScrollPane.setViewportView(jTextArea);
    jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    jPanel.add(jScrollPane, gridBagConstraints);

    setLayout(new BorderLayout());
    add(jPanel, BorderLayout.CENTER);
    add(jScrollPane, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(jButton);
    
    pack();
    setLocationRelativeTo(null);

    setVisible(true);
  }

  private JFileChooser jFileChooser; 
  
  public void jFileChooserRefresh()
  {
    jFileChooser.rescanCurrentDirectory();
  }
  
  public String getJFileChooserSelectedFilePath()
  {
    return jFileChooser.getSelectedFile().getAbsolutePath();
  }
  
  private JButton jOptionsButton;
  
  /**
   * JButton on form.
   */
  private JButton jButton;

  /**
   * JTextField on form.
   */
  private JTextField jTextField;

  /**
   * JTextArea on form.
   */
  private JTextArea jTextArea;

  /**
   * Retrieves the caption of the JButton.
   * @return caption of JButton.
   */
  public final String getJButton()
  {
    return jButton.getText();
  }

  /**
   * Sets the caption of the JButton.
   * @param string caption of the JButton.
   */
  public final void setJButton(final String string)
  {
    jButton.setText(string);
  }

  /**
   * Retrieves the contents of the JTextField.
   * @return contents of the JTextField.
   */
  public final String getJTextField()
  {
    return jTextField.getText();
  }

  /**
   * Sets the contents of the JTextField.
   * @param string contents of the JTextField.
   */
  public final void setJTextField(final String string)
  {
    jTextField.setText(string);
  }

  /**
   * Appends to the JTextArea.
   * @param string to append to the JTextArea.
   */
  public final void setJTextArea(final String string)
  {
    jTextArea.append(string + "\n");
  }

  /**
   * Clears the JTextArea.
   */
  public final void clearJTextArea()
  {
    jTextArea.setText(null);
  }
  
  public final void setJButtonEnabled(boolean b)
  {
    jButton.setEnabled(b);
  }
  
  public final void setJOptionsButtonEnabled(boolean b)
  {
    jOptionsButton.setEnabled(b);
  }

}
