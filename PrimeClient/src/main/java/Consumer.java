import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Consumer implements Runnable {
    private final BlockingQueue buffer;
    private boolean active = true;
    private final String url = "http://localhost:8080/a1server_war_exploded/prime";
    static int globalPrimeNumberCounter = 0;

    public Consumer(BlockingQueue buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        int localPrimeNumberCounter = 0;
        while (active) {
            try {
                Integer number = (Integer) buffer.take();
                if (number == 0) {
                    active = false;
                }
                String urlPath = url + String.valueOf(number);
                HttpGet getRequest = new HttpGet(urlPath);
                HttpResponse response = httpClient.execute(getRequest);
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
    }
}
