package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void prepareExpectedUser() {
        User user = new User(31, "Dmitry", Sex.MALE, "12345");
        List<User> users = getUsers().getBody();
        if (!(users.contains(user))) {
            postUser(user);
        }
    }

    public List<User> getOlderThanUsers(Integer age) {
        return getUsers().getBody().stream().filter(user -> user.getAge() != null && user.getAge() > age).toList();
    }

    public List<User> getYoungerThanUsers(Integer age) {
        return getUsers().getBody().stream().filter(user -> user.getAge() != null && user.getAge() < age).toList();
    }

    public List<User> getUsersBySex(Sex sex) {
        return sex == Sex.FEMALE ? getUsers().getBody().stream().filter(user -> user.getSex().equals(Sex.FEMALE)).toList() :
                getUsers().getBody().stream().filter(user -> user.getSex().equals(Sex.MALE)).toList();
    }
}
