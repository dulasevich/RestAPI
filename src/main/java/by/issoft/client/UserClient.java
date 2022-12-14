package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.dto.UserPairToUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserClient {

    private final static String USER_ENDPOINT = "/users";
    private final static String USER_UPLOAD_ENDPOINT = "/users/upload";
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

    public ResponseEntity<List<User>> getUsers(List<BasicNameValuePair> params) {
        List<User> users;
        ResponseEntity<List<User>> response = new ResponseEntity<>();
        HttpResponse httpResponse = Client.doGet(USER_ENDPOINT, params);
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            users = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), User[].class)).toList();
            response.setBody(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public int updateUser(UserPairToUpdate userPairToUpdate) {
        try {
            HttpResponse httpResponse = Client.doPut(USER_ENDPOINT, objectMapper.writeValueAsString(userPairToUpdate));
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public Sex setOppositeSex(Sex sex) {
        return sex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }

    public User createAvailableUser (User user) {
        int statusCode = postUser(user);
        if(statusCode == 201) {
            return user;
        } else {
            throw new RuntimeException("Failed to create available user. Check POST /users method.");
        }
    }

    public int deleteUser(User user) {
        try {
            HttpResponse httpResponse = Client.doDelete(USER_ENDPOINT, objectMapper.writeValueAsString(user));
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public HttpResponse uploadUser(File file, String fileName) {
        return Client.doPostFile(USER_UPLOAD_ENDPOINT, file, fileName);
    }

    public List<User> getUsersFromFile(File file) {
        List<User> users = new ArrayList<>();
        try {
            users = (Arrays.stream(objectMapper.readValue(file, User[].class)).toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
