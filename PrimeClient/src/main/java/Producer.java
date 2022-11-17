import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
  private final BlockingQueue<Integer> dataBuffer;
  private final Integer numRequests;
  protected static final int MAX = 10000;
  protected static final int MIN = 1;


  public Producer(BlockingQueue<Integer> dataBuffer, Integer requests) {
    this.dataBuffer = dataBuffer;
    this.numRequests = requests;
  }

  @Override
  public void run() {
    try {
      for (int i = 0; i < this.numRequests; i++) {
        SecureRandom secureRandom = new SecureRandom();
        int r = secureRandom.nextInt(MAX) + MIN;
        if (r % 2 == 0) {
          if (r == MAX) {
            r = r - 1;
          } else {
            r = r + 1;
          }
        }
        dataBuffer.put(r);
      }
      dataBuffer.put(0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
