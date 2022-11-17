import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Consumer implements Runnable {
    private final BlockingQueue buffer;
    private final CountDownLatch latch;
    private final CountDownLatch secondLatch;
    private boolean active = true;
    private final String url = "http://52.43.122.2:8080/ServerAPI_war/prime/";
    static int globalPrimeNumberCounter = 0;
    static int globalResponseTimeCounter = 0;

    public Consumer(BlockingQueue buffer, CountDownLatch latch, CountDownLatch secondLatch) {
        this.buffer = buffer;
        this.latch = latch;
        this.secondLatch = secondLatch;
    }

    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        int localPrimeNumberCounter = 0;
        int localResponseTimeCounter = 0;
        while (active) {
            try {
                Integer number = (Integer) buffer.take();
                if (number == 0) {
                    active = false;
                }
                String urlPath = url + String.valueOf(number);
                HttpGet getRequest = new HttpGet(urlPath);
                long start = System.currentTimeMillis();
                HttpResponse response = httpClient.execute(getRequest);
                System.out.println(response.getStatusLine().getStatusCode());
                long elapsed = System.currentTimeMillis() - start;
                localPrimeNumberCounter += elapsed;
                if (response.getStatusLine().getStatusCode() == 200) {
                    localPrimeNumberCounter += 1;
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClientProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        globalPrimeNumberCounter += localPrimeNumberCounter;
        globalResponseTimeCounter += localResponseTimeCounter;
        latch.countDown();
        System.out.println("latch countdown");
        if (secondLatch != null && latch.getCount() == 0) {
            secondLatch.countDown();
            System.out.println("second latch countdown");
        }
    }
}
