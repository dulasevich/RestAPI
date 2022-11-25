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
    private static final List<String> EXPECTED_CODES = List.of("ABCDE", "12345");
    private static final String NEW_ZIP_CODE = RandomStringUtils.randomNumeric(5);
    private ZipCodeClient zipCodeClient;

    @BeforeEach
    public void initZipCodeClient() {
        zipCodeClient = new ZipCodeClient();
    }

    @Test
    void getZipCodesTest() {
        ResponseEntity<List<String>> response =  zipCodeClient.getZipCodes();
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().containsAll(EXPECTED_CODES));
    }

    @Test
    void postZipCodeTest() {
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(NEW_ZIP_CODE);
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(NEW_ZIP_CODE));
    }

    @Test
    void postDuplicatedZipCodesTest() {
        int currentZipCodesQuantity = zipCodeClient.getZipCodes().getBody().size();
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(NEW_ZIP_CODE, NEW_ZIP_CODE);
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(NEW_ZIP_CODE));
        Assertions.assertEquals(currentZipCodesQuantity+1, zipCodeClient.getZipCodes().getBody().size());
    }

    @Test
    void postExistingZipCodeTest() {
        int currentZipCodesQuantity = zipCodeClient.getZipCodes().getBody().size();
        ResponseEntity<List<String>> response = zipCodeClient.postZipCodes(EXPECTED_CODES.get(0), NEW_ZIP_CODE);
        Assertions.assertEquals(RESPONSE_CODE, response.getStatusCode());
        Assertions.assertEquals(currentZipCodesQuantity+1, zipCodeClient.getZipCodes().getBody().size());
    }
}
