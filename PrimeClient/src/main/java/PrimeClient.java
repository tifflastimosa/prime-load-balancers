import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class PrimeClient {
    private final static int NUM_REQUEST = 1000;
    private final static int NUM_CLIENT_THREAD = 10;
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue buffer = new LinkedBlockingQueue();
        CountDownLatch latch = new CountDownLatch(NUM_CLIENT_THREAD);
        Producer producer = new Producer(buffer, NUM_REQUEST);
        Consumer consumer = new Consumer(buffer, latch);
        long start = System.currentTimeMillis();
        System.out.println("It's processing");
        new Thread(producer).start();
        for (int i = 0; i < NUM_CLIENT_THREAD; i++) {
            new Thread(consumer).start();
        }
        latch.await();
        long end = System.currentTimeMillis();
        float elapsedMs = (end - start);
        System.out.println("Wall time is: " + elapsedMs + " milliseconds");
        float avgResponseTime = Consumer.globalResponseTimeCounter / NUM_REQUEST;
        System.out.println("Avg response time is: " + avgResponseTime + " milliseconds");
        float numPrimeNumber = Consumer.globalPrimeNumberCounter / NUM_REQUEST * 100;
        System.out.println("% of prime number: " + numPrimeNumber + "%");
    }
}
