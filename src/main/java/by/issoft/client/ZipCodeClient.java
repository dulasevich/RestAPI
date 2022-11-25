package by.issoft.client;

import by.issoft.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        for (int i=0; i< zipcodes.length; i++) {
            HttpResponse httpResponse = Client.doPost(POST_ZIPCODES_EXPAND_ENDPOINT,
                    "[\"" + Arrays.stream(zipcodes).toList().get(i) + "\"]");
            response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            try {
                zipCodes = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), String[].class)).toList();
                response.setBody(zipCodes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
