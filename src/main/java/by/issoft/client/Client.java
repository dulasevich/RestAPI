package by.issoft.client;

import java.net.http.HttpClient;

public class Client {

    private HttpClient httpClient;

    public HttpClient initializeClient() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder().build();
        }
        return httpClient;
    }
}
