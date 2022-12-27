package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.dto.UserPairToUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserClient {

    private final static String USER_ENDPOINT = "/users";
    private final static String USER_UPLOAD_ENDPOINT = "/users/upload";
    private final ObjectMapper objectMapper;

    public UserClient() {
        this.objectMapper = new ObjectMapper();
    }

    @Step("Get all users")
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

    @Step("Create user\n [{user}]")
    public int postUser(User user) {
        try {
            String body = objectMapper.writeValueAsString(user);
            HttpResponse httpResponse = Client.doPost(USER_ENDPOINT, body);
            addPayload(body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Step("Get users by parameter(s)")
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

    @Step("Update user")
    public int updateUser(UserPairToUpdate userPairToUpdate) {
        try {
            String body = objectMapper.writeValueAsString(userPairToUpdate);
            HttpResponse httpResponse = Client.doPut(USER_ENDPOINT, body);
            addPayload(body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public Sex setOppositeSex(Sex sex) {
        return sex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }

    @Step("Create user for testing")
    public User createAvailableUser (User user) {
        int statusCode = 0;
        try {
            statusCode = Client.doPost(USER_ENDPOINT, objectMapper.writeValueAsString(user)).getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(statusCode == 201) {
            return user;
        } else {
            Client.logger.debug("Failed to create available user. Check POST /users method.");
            throw new RuntimeException();
        }
    }

    @Step("Delete user")
    public int deleteUser(User user) {
        try {
            String body = objectMapper.writeValueAsString(user);
            HttpResponse httpResponse = Client.doDelete(USER_ENDPOINT, body);
            addPayload(body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Step("Upload user")
    public HttpResponse uploadUser(File file, String fileName) {
        return Client.doPost(USER_UPLOAD_ENDPOINT, file, fileName);
    }

    public List<User> getUsersFromFile(File file) {
        try {
            return (Arrays.stream(objectMapper.readValue(file, User[].class)).toList());
        } catch (IOException e) {
            Client.logger.debug("unable to load users");
            throw new RuntimeException();
        }
    }

    public void addPayload(String body) {
        Allure.addAttachment("payload", body);
    }
}
