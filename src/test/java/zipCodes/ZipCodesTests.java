package zipCodes;

import by.issoft.ResponseEntity;
import by.issoft.client.ZipCodeClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class ZipCodesTests {

    private final static int RESPONSE_CODE = 201;
    private static final List<String> EXPECTED_CODES = List.of("12345", "23456");
    private String newZipCode;
    private ZipCodeClient zipCodeClient;

    @BeforeEach
    public void initZipCodeClient() {
        zipCodeClient = new ZipCodeClient();
        newZipCode = RandomStringUtils.randomNumeric(5);
    }

    @Test
    void getZipCodesTest() {
        ResponseEntity<List<String>> response =  zipCodeClient.getZipCodes();
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().containsAll(EXPECTED_CODES));
    }

    @Test
    void postZipCodeTest() {
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(newZipCode);
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(newZipCode));
    }

    @Test
    void postDuplicatedZipCodesTest() {
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(newZipCode, newZipCode);
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(newZipCode));
        Assertions.assertNotEquals(response.getBody().get(response.getBody().size()-2),
                newZipCode);
    }

    @Test
    void postExistingZipCodeTest() {
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(EXPECTED_CODES.get(0));
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertNotEquals(response.getBody().get(response.getBody().size()-1),
                EXPECTED_CODES.get(0));
    }
}
