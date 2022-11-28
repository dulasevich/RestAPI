package by.issoft.client;

import by.issoft.httpClient.AccessType;
import by.issoft.httpClient.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import by.issoft.dto.Auth;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.List;

import static by.issoft.client.Client.BASE_URL;

public class AuthClient {

    public static String WRITE_TOKEN;
    public static String READ_TOKEN;
    private final static String LOGIN = "0oa157tvtugfFXEhU4x7";
    private final static String PASSWORD = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";
    private final static String TOKEN_ENDPOINT = "/oauth/token";

    static {
        WRITE_TOKEN = getToken(AccessType.WRITE);
        READ_TOKEN = getToken(AccessType.READ);
    }

    private static String getToken(AccessType access) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse response = Request.post(BASE_URL + TOKEN_ENDPOINT)
                .addParameter("grant_type", "client_credentials")
                .addParameter("scope", access.name().toLowerCase())
                .addBasicAuth(LOGIN, PASSWORD)
                .send();
        try {
            return objectMapper.readValue(response.getEntity().getContent(), Auth.class).getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String setToken(AccessType access) {
        String bearerToken;
        if(access.equals(AccessType.READ)) {
            bearerToken = READ_TOKEN;
        } else {
            bearerToken = WRITE_TOKEN;
        }
        return bearerToken;
        //return access == AccessType.WRITE ? WRITE_TOKEN : READ_TOKEN;
    }
}
