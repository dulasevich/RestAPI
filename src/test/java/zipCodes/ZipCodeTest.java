package zipCodes;

import by.issoft.client.ZipCodeClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
public class ZipCodeTest {
    private final static int GET_RESPONSE_CODE = 200;
    private final static int POST_RESPONSE_CODE = 201;
    private static final List<String> EXPECTED_CODES = List.of("03464", "26432");
    private String newZipCode;
    private ZipCodeClient zipCodeClient;

    @BeforeEach
    public void initZipCodeClient() {
        newZipCode =  RandomStringUtils.randomNumeric(5);
        zipCodeClient = new ZipCodeClient();
    }

    @Test
    void getZipCodesTest() {
        Response response = zipCodeClient.getZipCodes();
        response.then().statusCode(GET_RESPONSE_CODE);
        //response.then().body("", hasItems(EXPECTED_CODES));
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertTrue(zipCodes.containsAll(EXPECTED_CODES));
    }

    @Test
    void postZipCodeTest() {
        Response response = zipCodeClient.postZipCodes(newZipCode);
        response.then().statusCode(POST_RESPONSE_CODE);
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertTrue(zipCodes.contains(newZipCode));
    }

    @Test
    void postDuplicatedZipCodesTest() {
        Response response = zipCodeClient.postZipCodes(newZipCode, newZipCode);
        response.then().statusCode(POST_RESPONSE_CODE);
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, newZipCode));
    }

    @Test
    void postExistingZipCodeTest() {
        Response response = zipCodeClient.postZipCodes(EXPECTED_CODES.get(0), EXPECTED_CODES.get(1));
        response.then().statusCode(POST_RESPONSE_CODE);
        List<String> zipCodes = Arrays.asList(response.getBody().as(String[].class));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, EXPECTED_CODES.get(0)));
        Assertions.assertEquals(1, Collections.frequency(zipCodes, EXPECTED_CODES.get(1)));
    }
}
