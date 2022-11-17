import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class PrimeClient {
    private final static int NUM_REQUEST = 1000;
    private final static int NUM_CLIENT_THREAD = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(NUM_CLIENT_THREAD);
        Consumer consumer = new Consumer(latch);
        long start = System.currentTimeMillis();
        System.out.println("It's processing");
        for (int i = 0; i < NUM_CLIENT_THREAD; i++) {
            new Thread(consumer).start();
        }
        latch.await();
        long end = System.currentTimeMillis();
        float elapsedMs = (end - start);
        System.out.println("Wall time is: " + elapsedMs + " milliseconds");
        float avgResponseTime = Consumer.globalResponseTimeCounter / NUM_REQUEST;
        System.out.println("Avg response time is: " + avgResponseTime + " milliseconds");
        double numPrimeNumber = (double) Consumer.globalPrimeNumberCounter / (double) NUM_REQUEST * 100.0;
        System.out.println("% of prime number: " + numPrimeNumber + "%");
    }
}
