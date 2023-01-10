package zipCodes;

import by.issoft.client.AuthClient;
import by.issoft.httpClient.AccessType;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsIterableContaining.hasItems;

public class ZipCodeTest {

    private static final String BASE_URL = "http://localhost:51000";
    private final static int GET_RESPONSE_CODE = 200;
    private final static int POST_RESPONSE_CODE = 201;
    private static final List<String> EXPECTED_CODES = List.of("43247", "95465");
    private String newZipCode;

    @BeforeEach
    public void initZipCodeClient() {
        newZipCode =  RandomStringUtils.randomNumeric(5);
    }

    @Test
    void getZipCodesTest() {
        Response response = given().header(
                        "Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when()
                .get(BASE_URL + "/zip-codes");
        response.then().statusCode(GET_RESPONSE_CODE);
        //response.then().body("", hasItems(EXPECTED_CODES));
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertTrue(zipCodes.containsAll(EXPECTED_CODES));
    }

    @Test
    void postZipCodeTest() {
        given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("[" + newZipCode + "]")
                .when()
                .post(BASE_URL + "/zip-codes/expand")
                .then()
                .statusCode(POST_RESPONSE_CODE)
                .body("", hasItems(newZipCode));
    }

    @Test
    void postDuplicatedZipCodesTest() {
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("[" + newZipCode + "," + newZipCode + "]")
                .when()
                .post(BASE_URL + "/zip-codes/expand");
        response.then().statusCode(POST_RESPONSE_CODE);
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, newZipCode));
    }

    @Test
    void postExistingZipCodeTest() {
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(EXPECTED_CODES)
                .when()
                .post(BASE_URL + "/zip-codes/expand");
        response.then().statusCode(POST_RESPONSE_CODE);
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, EXPECTED_CODES.get(0)));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, EXPECTED_CODES.get(1)));
    }
}
