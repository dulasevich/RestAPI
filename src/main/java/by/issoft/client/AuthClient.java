package by.issoft.client;

import by.issoft.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import by.issoft.dto.Auth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class AuthClient {

    private Client client;

    private final static String name = "0oa157tvtugfFXEhU4x7";
    private final static String password = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";

    public AuthClient() {
        client = new Client();
    }

    private static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    public ResponseEntity<Auth> getAuth(String scope) {
        Auth authResponseBody = null;
        ResponseEntity<Auth> response = new ResponseEntity<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:49000/oauth/token?grant_type=client_credentials&scope=" + scope))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Authorization", getBasicAuthenticationHeader(name, password))
                .build();
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = client.initializeClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            authResponseBody = objectMapper.readValue(httpResponse.body(), Auth.class);
            response.setBody(authResponseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
