import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Consumer implements Runnable {
    private final CountDownLatch latch;
    private boolean active = true;
    private final String url = "http://52.43.122.2:8080/ServerAPI_war/prime/";
    static int globalPrimeNumberCounter = 0;
    static int globalResponseTimeCounter = 0;

    public Consumer(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        int localPrimeNumberCounter = 0;
        int localResponseTimeCounter = 0;
        int requestCounter = 0;
        while (active) {
            try {
                if (requestCounter == 1000) {
                    System.out.println("here");
                    active = false;
                }
                int number = this.generatePrimeNumber();
                String urlPath = url + String.valueOf(number);
                HttpGet getRequest = new HttpGet(urlPath);
                long start = System.currentTimeMillis();
                HttpResponse response = httpClient.execute(getRequest);
                long elapsed = System.currentTimeMillis() - start;
                localResponseTimeCounter += elapsed;
                if (response.getStatusLine().getStatusCode() == 200) {
                    localPrimeNumberCounter += 1;
                }
                requestCounter += 1;
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
    }

    private int generatePrimeNumber() {
        SecureRandom secureRandom = new SecureRandom();
        int r = secureRandom.nextInt(100) + 1;
        if (r % 2 == 0) {
            if (r == 100) {
                r = r - 1;
            } else {
                r = r + 1;
            }
        }
        return r;
    }
}
