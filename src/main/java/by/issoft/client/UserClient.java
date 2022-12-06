package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserClient {

    private final static String USER_ENDPOINT = "/users";
    private final ObjectMapper objectMapper;

    public UserClient() {
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<List<User>> getUsers() {
        List<User> users;
        ResponseEntity<List<User>> response = new ResponseEntity<>();
        HttpResponse httpResponse = Client.doGet(USER_ENDPOINT);
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            users = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), User[].class)).toList();
            response.setBody(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int postUser(User user) {
        try {
            HttpResponse httpResponse = Client.doPost(USER_ENDPOINT, objectMapper.writeValueAsString(user));
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public ResponseEntity<List<User>> getUsers(String parameter, String value) {
        List<User> users;
        ResponseEntity<List<User>> response = new ResponseEntity<>();
        HttpResponse httpResponse = Client.doGet(USER_ENDPOINT, parameter, value);
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            users = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), User[].class)).toList();
            response.setBody(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int updateUser(User newUser, User user) {
        try {
            HttpResponse httpResponse = Client.doPut(USER_ENDPOINT, "{\"userNewValues\": " + objectMapper.writeValueAsString(newUser)
                    + "," + "\"userToChange\": " + objectMapper.writeValueAsString(user) + "}");
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public Sex setOppositeSex(Sex sex) {
        return sex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }

    public User createAvailableUser (User user) {
        int code = postUser(user);
        if(code == 201) {
            return user;
        } else {
            throw new RuntimeException("Failed to create available zipcode. Check POST /zip-codes/expand method.");
        }
    }
}
