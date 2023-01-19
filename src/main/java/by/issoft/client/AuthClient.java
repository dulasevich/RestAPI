package by.issoft.client;

import by.issoft.httpClient.AccessType;
import by.issoft.dto.Auth;

import static io.restassured.RestAssured.given;

public class AuthClient {
    public static final String BASE_URL = "http://localhost:50000";
    private final static String writeToken;
    private final static String readToken;
    private final static String LOGIN = "0oa157tvtugfFXEhU4x7";
    private final static String PASSWORD = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";
    private final static String TOKEN_ENDPOINT = "/oauth/token";

    static {
        writeToken = setToken(AccessType.WRITE);
        readToken = setToken(AccessType.READ);
    }

    private static String setToken(AccessType access) {
        return given()
                .param("grant_type", "client_credentials")
                .param("scope", access.name().toLowerCase())
                .auth()
                .basic(LOGIN, PASSWORD)
                .when()
                .post(BASE_URL + TOKEN_ENDPOINT)
                .as(Auth.class)
                .getAccessToken();
    }

    public static String getToken(AccessType access) {
        return access == AccessType.WRITE ? writeToken : readToken;
    }
}
