/**
 * Utilised tutorial to implement huffman coding technique
 * CS3580 Notes by Zhiyuan Luo
 * Chapter 2 - Multimedia Transmission
 */
package huffman;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.BitReader;
import utils.BitWriter;

/**
 */
public class Huffman
{

  /**
   * HashMap to make testing existence of an external node with a particular byte O(1).
   */
  private TreeMap<Byte, ExternalNode> nodeHashMap = new TreeMap<Byte, ExternalNode>();

  public  Huffman()
  {
  }
  
  /**
   * Add a record of a new encounter of a byte to the nodeHashMap.
   * @param b byte value.
   */
  public void add(final byte b)
  {
    ExternalNode externalNode = nodeHashMap.get(b);
    if (externalNode == null)
    {
      externalNode = new ExternalNode(b, 1);
      nodeHashMap.put(b, externalNode);
    }
    else
    {
      externalNode.increaseFrequency();
    }
  }
  
  public void add(final byte b, final int i)
  {
    ExternalNode externalNode = new ExternalNode(b, i);
    nodeHashMap.put(b, externalNode);
  }

  /**
   * Compress method constructs the Huffman tree and binary codes from the nodeHashMap object.
   * @return HashMap containing the byte and binary code pairs.
   */
  //private static HashMap<Byte, BinaryCode> compress()
  public TreeMap<Byte, ExternalNode> compress()
  {
    
    PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>(nodeHashMap.values());

    if (priorityQueue.size() == 1)
    {
      priorityQueue.add(new ExternalNode(null, 0));
    }

    while (priorityQueue.size() > 1)
    {
      Node leftNode = priorityQueue.remove();
      Node rightNode = priorityQueue.remove();
      priorityQueue.add(new InternalNode(leftNode, rightNode));
    }
    
    //HashMap<Byte, ExternalNode> nodeHashMap2 = new HashMap<Byte, ExternalNode>(nodeHashMap);
    
    //nodeHashMap.clear(); //Reset for new compress() call.

    //return nodeHashMap2
    
    return nodeHashMap;
  }

  /**
   * Compress a file.
   * @param pathInput to be compressed.
   * @return output information.
   * @throws IOException if there is an error with the file.
   */
  public static void compress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();

    Huffman huffman = new Huffman();
    
    while(bbInput.remaining() > 0)
    {
      byte b = bbInput.get();
      huffman.add(b);
    }
    
    TreeMap<Byte, ExternalNode> nodeHashMap = huffman.compress();
    bbOutput.putInt(nodeHashMap.size());

    //Max size hashmap = 2^8
    for (Entry<Byte, ExternalNode> e : nodeHashMap.entrySet())
    {
      bbOutput.put(e.getKey().byteValue());
      bbOutput.putInt(e.getValue().getFrequency());
    }
    bbOutput.putLong(bbInput.limit());

    BitWriter bitWriter = new BitWriter(bbOutput);
    
    bbInput.position(0);
    while(bbInput.remaining() > 0)
    {
      byte b = bbInput.get();
      BinaryCode binaryCode = nodeHashMap.get(b).getBinaryCode();
      for (int j = 0; j < binaryCode.getLength(); j++)
      {
        bitWriter.put(binaryCode.getBitSet().get(j));
      }
    }
    bitWriter.finish();
 
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }

  /**
   * Decompresses a file.
   * @param pathInput compressed file.
   * @return output information.
   * @throws IOException if file encounter IOException.
   * @throws ClassNotFoundException if loaded object is not of correct type.
   */
  public static void decompress(ByteBuffer bbInput, ByteBuffer bbOutput, StringBuilder information, long[] time) throws IOException
  {
    long startTime = System.currentTimeMillis();

    Huffman huffman = new Huffman();
    
    int hashMapSize = bbInput.getInt();
    
    for (int count = 0; count < hashMapSize; count++)
    {
      byte b = bbInput.get();
      int i = bbInput.getInt();
      huffman.add(b, i);
    }

    TreeMap<Byte, ExternalNode> nodeHashMap = huffman.compress();

    // Reverse nodeHashMap into byteHashMap, used to lookup BinaryCode by byte
    // value.
    HashMap<BinaryCode, Byte> byteHashMap = new HashMap<BinaryCode, Byte>(nodeHashMap.size());
    for (Entry<Byte, ExternalNode> e : nodeHashMap.entrySet())
    {
      byteHashMap.put(e.getValue().getBinaryCode(), e.getKey());
    }

    long fileSize = bbInput.getLong();

    BitReader bitReader = new BitReader(bbInput);

    BitSet bitSet = new BitSet(8);
    int index = 0;
    
    boolean[] bit = new boolean[1];
    
    boolean EOF = false;
    while(!EOF && bitReader.get(bit) != -1)
    {
      bitSet.set(index++, bit[0]);
      BinaryCode binaryCode = new BinaryCode(bitSet, index);
      if (byteHashMap.containsKey(binaryCode))
      {
        bbOutput.put(byteHashMap.get(binaryCode).byteValue());
        bitSet.clear();
        index = 0;
        if (--fileSize == 0)
        {
          EOF = true;
        }
      }
    }
    bitReader.finalize();
 
    long endTime = System.currentTimeMillis();
    time[0] += (endTime - startTime);
  }

}