package arithmetic;

public class Range
{
  
  public long count;
  public char high;
  public char low;
  
  public Range(long count)
  {
    this.count = count;
  }
  
  public void inc()
  {
    count++;
  }

  public char set(char old)
  {
    this.low = old;
    this.high = (char) (old + count);
    return this.high;
  }
  
}
