package test;

import huffman.ExternalNode;
import huffman.InternalNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test class for InternalNode.
 */
public class TestInternalNode
{

  /**
   * 3.
   */
  public static final int THREE = 3;

  /**
   * 4.
   */
  public static final int FOUR = 4;

  /**
   * Test Character 1 to 4.
   */
  private byte testCharacter1, testCharacter2, testCharacter3, testCharacter4;

  /**
   * Test Frequency 1 to 4.
   */
  private int testFrequency1, testFrequency2, testFrequency3, testFrequency4;

  /**
   * Test ExternalNode 1 to 4.
   */
  private ExternalNode testExternalNode1, testExternalNode2, testExternalNode3, testExternalNode4;

  /**
   * Test InternalNode 1 to 2.
   */
  private InternalNode testInternalNode1, testInternalNode2;

  /**
   * Initialises the test environment.
   * @throws java.lang.Exception if an exception is raised.
   */
  @Before
  public final void setUp() throws Exception
  {
    testCharacter1 = (byte) 'A';
    testCharacter2 = (byte) 'B';
    testCharacter3 = (byte) 'C';
    testCharacter4 = (byte) 'D';

    testFrequency1 = 1;
    testFrequency2 = 2;
    testFrequency3 = THREE;
    testFrequency4 = FOUR;
  }

  /**
   * Tests the combination of two external nodes.
   */
  @Test
  public final void testConstructorTwoExternal()
  {
    try
    {
      testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
      testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2);
      testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2);
    }
    catch (Exception exception)
    {
      Assert.fail("Unexpected exception raised: " + exception.getMessage());
    }
  }

  /**
   * Tests the combination of one internal and one external node.
   */
  @Test
  public final void testConstructorOneExternalOneInternal()
  {
    try
    {
      testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
      testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2);
      testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2);
      testExternalNode3 = new ExternalNode(testCharacter3, testFrequency3);
      testInternalNode2 = new InternalNode(testInternalNode1, testExternalNode3);
    }
    catch (Exception exception)
    {
      Assert.fail("Unexpected exception raised: " + exception.getMessage());
    }
  }

  /**
   * Tests the combination of two internal nodes.
   */
  @Test
  public final void testConstructorTwoInternal()
  {
    try
    {
      testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
      testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2);
      testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2);
      testExternalNode3 = new ExternalNode(testCharacter3, testFrequency3);
      testExternalNode4 = new ExternalNode(testCharacter4, testFrequency4);
      testInternalNode2 = new InternalNode(testExternalNode3, testExternalNode4);
      new InternalNode(testInternalNode1, testInternalNode2);
    }
    catch (Exception exception)
    {
      Assert.fail("Unexpected exception raised: " + exception.getMessage());
    }
  }

}
