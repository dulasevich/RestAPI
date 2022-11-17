package by.issoft.client;

import by.issoft.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZipCodeClient {

    private Methods clientMethods;

    public ZipCodeClient() {
        clientMethods = new Methods();
    }

    public HttpResponse<String> getZipCodesResponse() {
        HttpResponse<String> httpResponse = clientMethods.doGet("zip-codes");
        return httpResponse;
    }

    public ResponseEntity<List<String>> getZipCodes() {
        List<String> zipCodes = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<List<String>> response = new ResponseEntity<>();
        try {
            response.setStatusCode(getZipCodesResponse().statusCode());
            zipCodes = Arrays.stream(objectMapper.readValue(getZipCodesResponse().body(), String[].class)).toList();
            response.setBody(zipCodes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void postNewZipCode(String zipCode) {
        clientMethods.doPost("zip-codes/expand", zipCode);
    }
}
