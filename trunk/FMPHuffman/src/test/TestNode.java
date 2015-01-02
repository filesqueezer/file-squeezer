package test;

import static org.junit.Assert.assertEquals;
import huffman.ExternalNode;
import huffman.InternalNode;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for Node.
 */
public class TestNode
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
   * 9.
   */
  public static final int NINE = 9;

  /**
   * Test ExternalNode 1 to 4.
   */
  private ExternalNode testExternalNode1, testExternalNode2, testExternalNode3, testExternalNode4;

  /**
   * Test Internal Node 1 to 2.
   */
  private InternalNode testInternalNode1, testInternalNode2;

  /**
   * Test character 1 to 4.
   */
  private byte testCharacter1, testCharacter2, testCharacter3, testCharacter4;

  /**
   * Test frequency 1 to 4.
   */
  private int testFrequency1, testFrequency2, testFrequency3, testFrequency4;

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
    testFrequency3 = FOUR;
    testFrequency4 = NINE;
  }

  /**
   * Tests the Node.getFrequency() method.
   */
  @Test
  public final void testGetFrequency()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    assertEquals(testExternalNode1.getFrequency(), testFrequency1, 0);
    testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2);
    testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2);
    assertEquals(testInternalNode1.getFrequency(), testFrequency1 + testFrequency2, 0);
  }

  /**
   * Tests the Node.increaseFrequency() method.
   */
  @Test
  public final void testIncreaseFrequency()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1);
    testExternalNode1.increaseFrequency();
    assertEquals(testExternalNode1.getFrequency(), testFrequency1 + 1, 0);
    testExternalNode1.increaseFrequency();
    assertEquals(testExternalNode1.getFrequency(), testFrequency1 + 2, 0);
  }

  /**
   * Tests the Node.addBit(int bit) method once.
   */
  @Test
  public final void testAddBitLength1()
  {
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1); //1
    testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2); //2
    testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2); //3
    assertEquals(testExternalNode1.getBinaryCode().getLength(), 1, 0);
    // assertEquals(testExternalNode1.getBinaryCodeInt(), 0, 0);
    assertEquals(testExternalNode1.getBinaryCode().toString(), new String("0"));

    assertEquals(testExternalNode2.getBinaryCode().getLength(), 1, 0);
    // assertEquals(testExternalNode2.getBinaryCodeInt(), 1, 0);
    assertEquals(testExternalNode2.getBinaryCode().toString(), new String("1"));
  }

  /**
   * Tests the Node.addBit(int bit) method twice.
   */
  @Test
  public final void testAddBitLength2()
  {
    //Paired in the order selected from the priority queue (min frequency).
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1); //1
    testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2); //2
    testExternalNode3 = new ExternalNode(testCharacter3, testFrequency3); //4

    testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2); //3
    testInternalNode2 = new InternalNode(testInternalNode1, testExternalNode3); //10

    assertEquals(testExternalNode1.getBinaryCode().getLength(), 2, 0);
    assertEquals(testExternalNode1.getBinaryCode().toString(), new String("00"));

    assertEquals(testExternalNode2.getBinaryCode().getLength(), 2, 0);
    assertEquals(testExternalNode2.getBinaryCode().toString(), new String("01"));

    assertEquals(testExternalNode3.getBinaryCode().getLength(), 1, 0);
    assertEquals(testExternalNode3.getBinaryCode().toString(), new String("1"));
  }

  /**
   * Tests the Node.addBit(int bit) three times.
   */
  @Test
  public final void testAddBitLength3()
  {
    // Paired in the order selected from the priority queue (min frequency).
    testExternalNode1 = new ExternalNode(testCharacter1, testFrequency1); //1
    testExternalNode2 = new ExternalNode(testCharacter2, testFrequency2); //2
    testExternalNode3 = new ExternalNode(testCharacter3, testFrequency3); //4
    testExternalNode4 = new ExternalNode(testCharacter4, testFrequency4); //9

    testInternalNode1 = new InternalNode(testExternalNode1, testExternalNode2); //3
    testInternalNode2 = new InternalNode(testInternalNode1, testExternalNode3); //7
    new InternalNode(testInternalNode2, testExternalNode4); //16

    assertEquals(testExternalNode1.getBinaryCode().getLength(), THREE, 0);
    assertEquals(testExternalNode1.getBinaryCode().toString(), new String("000"));

    assertEquals(testExternalNode2.getBinaryCode().getLength(), THREE, 0);
    assertEquals(testExternalNode2.getBinaryCode().toString(), new String("001"));

    assertEquals(testExternalNode3.getBinaryCode().getLength(), 2, 0);
    assertEquals(testExternalNode3.getBinaryCode().toString(), new String("01"));

    assertEquals(testExternalNode4.getBinaryCode().getLength(), 1, 0);
    assertEquals(testExternalNode4.getBinaryCode().toString(), new String("1"));
  }

}
