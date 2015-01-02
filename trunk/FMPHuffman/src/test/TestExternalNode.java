package test;

import static org.junit.Assert.assertEquals;
import huffman.ExternalNode;
import huffman.InternalNode;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ExternalNode.
 */
public class TestExternalNode
{

  /**
   * Test character 1.
   */
  private byte testCharacter1;

  /**
   * Test character 2.
   */
  private byte testCharacter2;

  /**
   * Test frequency 1.
   */
  private int testFrequency1;

  /**
   * Test frequency 2.
   */
  private int testFrequency2;

  /**
   * Test External Node 1.
   */
  private ExternalNode testExternalNode1;

  /**
   * Test External Node 2.
   */
  private ExternalNode testExternalNode2;

  /**
   * Initialises the test environment.
   * @throws java.lang.Exception if an exception is raised.
   */
  @Before
  public final void setUp() throws Exception
  {
    testCharacter1 = (byte) 'A';
    testFrequency1 = 2;
  }

  /**
   * Tests ExternalNode.getCharacter() method.
   */
  @Test
  public final void testGetChar()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    assertEquals(testExternalNode1.getCharacter(), new Byte(testCharacter1));
  }

  /**
   * Tests ExternalNode.getBinaryCode().getLength() method.
   */
  @Test
  public final void testGetLength()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 0, 0);
    testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2);
    new InternalNode(testExternalNode1, testExternalNode2);
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 1, 0);
    assertEquals(testExternalNode2.getBinaryCode().getLength(), 1, 0);
  }

  /**
   * Tests ExternalNode.getBinaryCode().getLength() method.
   */
  @Test
  public final void testSetLength()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    testExternalNode1.getBinaryCode().setLength(0);
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 0, 0);
  }

  /**
   * Tests ExternalNode.getBinaryCode.getLength() method.
   */
  @Test
  public final void testIncreaseLength()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    testExternalNode1.getBinaryCode().setLength(0);
    testExternalNode1.getBinaryCode().increaseLength();
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 1, 0);
    testExternalNode1.getBinaryCode().increaseLength();
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 2, 0);
  }

}
