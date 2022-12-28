package by.issoft.client;

import by.issoft.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ZipCodeClient {

    private static final String GET_ZIPCODES_ENDPOINT = "/zip-codes";
    private static final String POST_ZIPCODES_EXPAND_ENDPOINT = "/zip-codes/expand";
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ZipCodeClient.class);

    public ZipCodeClient() {
        this.objectMapper = new ObjectMapper();
    }

    @Step("Get all zipCodes")
    public ResponseEntity<List<String>> getZipCodes() {
        logger.info("Getting all zipCodes");
        List<String> zipCodes;
        ResponseEntity<List<String>> response = new ResponseEntity<>();
        HttpResponse httpResponse = Client.doGet(GET_ZIPCODES_ENDPOINT);
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            zipCodes = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), String[].class)).toList();
            response.setBody(zipCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Step("Create new zipCode(s)")
    public ResponseEntity<List<String>> postZipCodes(String... zipcodes) {
        logger.info("Creating new zipCode");
        List<String> zipCodes;
        ResponseEntity<List<String>> response = new ResponseEntity<>();
        String body = StringUtils.join("[\"", String.join("\", \"", zipcodes), "\"]");
        HttpResponse httpResponse = Client.doPost(POST_ZIPCODES_EXPAND_ENDPOINT, body);
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            zipCodes = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), String[].class)).toList();
            response.setBody(zipCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Step("Create zipCode for testing")
    public String createAvailableZipcode () {
        logger.info("Creating zipCode for testing");
        String zipcode = RandomStringUtils.randomNumeric(5);
        ResponseEntity<List<String>> response = postZipCodes(zipcode);
        if(response.getStatusCode() == 201) {
            return zipcode;
        } else {
            logger.error("Failed to create available zipcode. Check POST /zip-codes/expand method.");
            throw new RuntimeException();
        }
    }
}
