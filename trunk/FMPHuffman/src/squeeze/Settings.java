/**
 * 
 */
package squeeze;

/**
 *
 */
public class Settings
{
  /**
   * Magic Number used to identify compressed files.
   */
  public static final byte[] SQUEEZE_MAGIC_NUMBER = {'S', 'Q', 'U', 'E', 'E', 'Z', 'E', '!'};
 
  public static String NEW_NAME = new String();
  
  //T True
  //F False
  public final static int STORE_TIMES = 0;
  public final static byte STORE_TIMES_T = 'T';
  public final static byte STORE_TIMES_F = 'F';
  
  //H LZW + Huffman
  //A LZW + Arithmetic
  public final static int COMPRESSION_METHOD = 1;
  public final static byte COMPRESSION_METHOD_H = 'H';
  public final static byte COMPRESSION_METHOD_A = 'A';
  
  public static int BLOCK_SIZE = 64 * 1024;//64KB is default
  
  //U Undefined  
  public static byte[] SETTINGS = {STORE_TIMES_F, COMPRESSION_METHOD_H, 'U', 'U'};

}
