import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class PrimeClient {
    private final static int INIT_NUM_REQUESTS = 32000;
    private final static int SECOND_NUM_REQUESTS= 68000;
    private final static int INITIAL_NUM_THREADS = 32;
    private final static int SECOND_NUM_THREADS = 40;
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue firstBuffer = new LinkedBlockingQueue();
        BlockingQueue secondBuffer = new LinkedBlockingQueue();
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch secondLatch = new CountDownLatch(SECOND_NUM_THREADS);
        CountDownLatch thirdLatch = new CountDownLatch(INITIAL_NUM_THREADS - 1);
        Producer firstProducer = new Producer(firstBuffer, INITIAL_NUM_THREADS, INIT_NUM_REQUESTS);
        Producer secondProducer = new Producer(secondBuffer, SECOND_NUM_THREADS, SECOND_NUM_REQUESTS);
        Consumer firstConsumer = new Consumer(firstBuffer, latch, thirdLatch);
        Consumer secondConsumer = new Consumer(secondBuffer, secondLatch, null);
        new Thread(firstProducer).start();
        new Thread(secondProducer).start();
        long start = System.currentTimeMillis();
        for (int i = 0; i < INITIAL_NUM_THREADS; i++) {
            new Thread(firstConsumer).start();
        }
        System.out.println("The first phase is processing........");
        latch.await();
        System.out.println("The first latch is triggered.");
        System.out.println("The second phase is processing........");
        for (int i = 0; i < SECOND_NUM_THREADS; i++) {
            new Thread(secondConsumer).start();
        }
        secondLatch.await();
        thirdLatch.await();
        long end = System.currentTimeMillis();
        System.out.println("The second and third latch is triggered.");
        System.out.println(" ");
        float elapsedMs = (end - start);
        double elapsedS = (end-start) * 0.001;
        System.out.println("Wall time is: " + elapsedMs + " milliseconds");
        System.out.println("Wall time is: " + elapsedS + " seconds");
    }
}
