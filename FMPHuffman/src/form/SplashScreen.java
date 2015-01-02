package form;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashScreen extends JFrame
{
  /**
   * 
   */
  private static final long serialVersionUID = -4770108076389837085L;

  public SplashScreen()
  {
    super();
    
    Path binDirectory = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().substring(1));
    Path defaultDirectory = binDirectory.resolve("~Resources");
    if (Files.notExists(defaultDirectory))
    {
      defaultDirectory = binDirectory;
    }
    Path path = defaultDirectory.resolve("SqueezeSplash.png");
    if (Files.notExists(path))
    {
      dispose();
      return;
    }
    File file = new File(path.toString());
    //System.out.println(path.toString());
    
    BufferedImage bufferedImage = null;
    try
    {
      bufferedImage = ImageIO.read(file);
    }
    catch (IOException e1)
    {
    }

    JLabel jLabel = new JLabel();
    jLabel.setIcon(new ImageIcon(bufferedImage));
    getContentPane().add(jLabel);

    this.setUndecorated(true);
    this.setAlwaysOnTop(true);
    this.pack();
    this.setLocationRelativeTo(null);

    setVisible(true);
    try
    {
      Thread.sleep(1000);
    }
    catch (InterruptedException e)
    {
    }
    dispose();
  }
  
}
