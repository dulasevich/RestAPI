package by.issoft.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import by.issoft.dto.Auth;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

import static by.issoft.client.Client.BASE_URL;

public class AuthClient {

    private final static String LOGIN = "0oa157tvtugfFXEhU4x7";
    private final static String PASSWORD = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";
    private final static String TOKEN_ENDPOINT = "/oauth/token";
//    private final static String TOKEN_ENDPOINT = "/oauth/token?grant_type=client_credentials&scope=";

    private static final String WRITE_TOKEN;
    private  static final String READ_TOKEN;

    private AuthClient() {}

    static {
        WRITE_TOKEN = requestToken(AccessType.WRITE);
        READ_TOKEN = requestToken(AccessType.READ);
    }

    // an example of the method with Client class
    private static String requestToken(AccessType access) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse response = Client.doPost(TOKEN_ENDPOINT,
                List.of(new BasicNameValuePair("grant_type", "client_credentials"),
                        new BasicNameValuePair("scope", access.name().toLowerCase())),
                LOGIN,
                PASSWORD);
        try {
            return objectMapper.readValue(response.getEntity().getContent(), Auth.class).getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // an example of the method with Request class
//    private static String requestToken(AccessType access) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        HttpResponse response = Request.post(BASE_URL + TOKEN_ENDPOINT)
//                .addParameter("grant_type", "client_credentials")
//                .addParameter("scope", access.name().toLowerCase())
//                .addBasicAuth(LOGIN, PASSWORD).send();
//        try {
//            return objectMapper.readValue(response.getEntity().getContent(), Auth.class).getAccessToken();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String getToken(AccessType accessType) {
        return accessType == AccessType.READ ? READ_TOKEN : WRITE_TOKEN;
    }

//    private static String getBasicAuthenticationHeader(String username, String password) {
//        String valueToEncode = username + ":" + password;
//        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
//    }
//
//    public ResponseEntity<Auth> getAuth(String scope) {
//        Auth authResponseBody = null;
//        ResponseEntity<Auth> response = new ResponseEntity<>();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:49000/oauth/token?grant_type=client_credentials&scope=" + scope))
//                .POST(HttpRequest.BodyPublishers.noBody())
//                .header("Authorization", getBasicAuthenticationHeader(name, password))
//                .build();
//        HttpResponse<String> httpResponse = null;
//        try {
//            httpResponse = client.initializeClient().send(request, HttpResponse.BodyHandlers.ofString());
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            authResponseBody = objectMapper.readValue(httpResponse.body(), Auth.class);
//            response.setBody(authResponseBody);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }

    public enum AccessType {
        WRITE, READ
    }
}
