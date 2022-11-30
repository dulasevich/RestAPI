package by.issoft.client;

import by.issoft.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ZipCodeClient {

    private static final String GET_ZIPCODES_ENDPOINT = "/zip-codes";
    private static final String POST_ZIPCODES_EXPAND_ENDPOINT = "/zip-codes/expand";
    private final ObjectMapper objectMapper;

    public ZipCodeClient() {
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<List<String>> getZipCodes() {
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

    public ResponseEntity<List<String>> postZipCodes(String... zipcodes) {
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

    public String createAvailableZipcode () {
        String zipcode = RandomStringUtils.randomNumeric(5);
        ResponseEntity<List<String>> response = postZipCodes(zipcode);
        if(response.getStatusCode() == 201) {
            return zipcode;
        } else {
            throw new RuntimeException("Failed to create available zipcode. Check POST /zip-codes/expand method.");
        }
    }
}
