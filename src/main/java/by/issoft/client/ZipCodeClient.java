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
        HttpResponse response = Client.doGet(GET_ZIPCODES_ENDPOINT, AuthClient.getToken(AuthClient.AccessType.READ));
        return prepareEntity(response);
    }

    public ResponseEntity<List<String>> postZipCodes(String... zipcodes) {
        String jsonBody = prepareBody(zipcodes);
        HttpResponse response = Client.doPost(POST_ZIPCODES_EXPAND_ENDPOINT,
                AuthClient.getToken(AuthClient.AccessType.WRITE),
                jsonBody);
        return prepareEntity(response);
    }

    private ResponseEntity<List<String>> prepareEntity(HttpResponse response) {
        ResponseEntity<List<String>> entity = new ResponseEntity<>();
        entity.setStatusCode(response.getStatusLine().getStatusCode());
        try {
            entity.setEntity(Arrays.stream(objectMapper.readValue(response.getEntity().getContent(), String[].class)).toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    private String prepareBody(String... zipcodes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Arrays.stream(zipcodes).forEach(zipcode -> sb
                .append("\"")
                .append(zipcode)
                .append("\", "));
        sb.append("]");
        return sb.toString().replace(", ]", "]");
    }

//    private Methods clientMethods;
//
//    public ZipCodeClient() {
//        clientMethods = new Methods();
//    }

//    public HttpResponse<String> getZipCodesResponse() {
//        HttpResponse<String> httpResponse = clientMethods.doGet("zip-codes");
//        return httpResponse;
//    }
//
//    public ResponseEntity<List<String>> getZipCodes() {
//        List<String> zipCodes = new ArrayList<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ResponseEntity<List<String>> response = new ResponseEntity<>();
//        try {
//            response.setStatusCode(getZipCodesResponse().statusCode());
//            zipCodes = Arrays.stream(objectMapper.readValue(getZipCodesResponse().body(), String[].class)).toList();
//            response.setBody(zipCodes);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }
//
//    public void postNewZipCode(String zipCode) {
//        clientMethods.doPost("zip-codes/expand", zipCode);
//    }
}
