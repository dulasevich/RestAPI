package zipCodes;

import by.issoft.ResponseEntity;
import by.issoft.client.ZipCodeClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

public class ZipCodeTest {

    private final static int RESPONSE_CODE = 201;
    private static final List<String> EXPECTED_CODES = List.of("ABCDE", "23456");
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
        Assertions.assertEquals(1, Collections.frequency(response.getBody(), newZipCode));
    }

    @Test
    void postExistingZipCodeTest() {
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(EXPECTED_CODES.toArray(String[]::new));
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertEquals(1, Collections.frequency(response.getBody(), EXPECTED_CODES.get(0)));
        Assertions.assertEquals(1, Collections.frequency(response.getBody(), EXPECTED_CODES.get(1)));
    }
}
