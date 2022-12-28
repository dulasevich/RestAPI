package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.dto.UserPairToUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserClient {

    private static final String USER_ENDPOINT = "/users";
    private static final String USER_UPLOAD_ENDPOINT = "/users/upload";
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);

    public UserClient() {
        this.objectMapper = new ObjectMapper();
    }

    @Step("Get all users")
    public ResponseEntity<List<User>> getUsers() {
        logger.info("Getting all users");
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
        logger.info("Creating new user: " + user);
        try {
            String body = objectMapper.writeValueAsString(user);
            HttpResponse httpResponse = Client.doPost(USER_ENDPOINT, body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Step("Get users by parameter(s)")
    public ResponseEntity<List<User>> getUsers(List<BasicNameValuePair> params) {
        logger.info("Getting " + params +  " user(s)");
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
        logger.info("Updating user");
        try {
            String body = objectMapper.writeValueAsString(userPairToUpdate);
            HttpResponse httpResponse = Client.doPut(USER_ENDPOINT, body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public Sex setOppositeSex(Sex sex) {
        return sex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }

    @Step("Create user for testing")
    public void createAvailableUser (User user) {
        logger.info("Creating user for testing");
        int statusCode = 0;
        try {
            statusCode = Client.doPost(USER_ENDPOINT, objectMapper.writeValueAsString(user)).getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(statusCode != 201) {
            logger.error("Failed to create available user. Check POST /users method.");
        }
    }

    @Step("Delete user")
    public int deleteUser(User user) {
        logger.info("Deleting user");
        try {
            String body = objectMapper.writeValueAsString(user);
            HttpResponse httpResponse = Client.doDelete(USER_ENDPOINT, body);
            return httpResponse.getStatusLine().getStatusCode();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    @Step("Upload user")
    public HttpResponse uploadUser(File file, String fileName) {
        logger.info("Uploading user(s)");
        return Client.doPost(USER_UPLOAD_ENDPOINT, file, fileName);
    }

    public List<User> getUsersFromFile(File file) {
        try {
            return (Arrays.stream(objectMapper.readValue(file, User[].class)).toList());
        } catch (IOException e) {
            logger.error("unable to load users");
            throw new RuntimeException();
        }
    }
}
