package by.issoft.client;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Methods {

    private Client client;
    private AuthClient authClient;
    private final static String BASE_URL = "http://localhost:49000/";

    public Methods() {
        client = new Client();
        authClient = new AuthClient();
    }

    private String getReadBearerToken() {
        return "Bearer " + authClient.getAuth("read").getBody().getAccess_token();
    }

    private String getWriteBearerToken() {
        return "Bearer " + authClient.getAuth("write").getBody().getAccess_token();
    }

    public HttpResponse<String> doGet(String endpoint) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", getReadBearerToken())
                .build();
        try {
            return client.initializeClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> doPost(String endpoint, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-type", "application/json")
                .header("Authorization", getWriteBearerToken())
                .build();
        try {
            return client.initializeClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> doPut(String endpoint, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-type", "application/json")
                .header("Authorization", getWriteBearerToken())
                .build();
        try {
            return client.initializeClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> doDelete(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-type", "application/json")
                .header("Authorization", getWriteBearerToken())
                .build();
        try {
            return client.initializeClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
