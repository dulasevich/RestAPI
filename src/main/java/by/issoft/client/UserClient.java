package by.issoft.client;

import by.issoft.ResponseEntity;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public ResponseEntity<List<User>> postUser(User user) {
        List<User> users;
        ResponseEntity<List<User>> response = new ResponseEntity<>();
        HttpResponse httpResponse = Client.doPost(USER_ENDPOINT, postBody(user));
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        try {
            users = Arrays.stream(objectMapper.readValue(httpResponse.getEntity().getContent(), User[].class)).toList();
            response.setBody(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String prepareBody(Integer age, String name, Sex sex, String zipcode) {
        return "{" + "\n" +
                "\"" + "age" + "\"" + ":" + age + ",\n" +
                "\"" + "name" + "\"" + ":" + "\"" + name + "\"" + ",\n" +
                "\"" + "sex" + "\"" + ":" + "\"" + sex.toString() + "\"" + ",\n" +
                "\"" + "zipCode" + "\"" + ":" + "\"" + zipcode + "\"" + "\n" +
                "}";
    }

    private String prepareAgeZipCodeNullBody(Integer age, String name, Sex sex, String zipcode) {
        return "{" + "\n" +
                "\"" + "name" + "\"" + ":" + "\"" + age + "\"" + ",\n" +
                "\"" + "name" + "\"" + ":" + "\"" + name + "\"" + ",\n" +
                "\"" + "sex" + "\"" + ":" + "\"" + sex.toString() + "\"" + ",\n" +
                "\"" + "zipCode" + "\"" + ":" + zipcode + "\n" +
                "}";
    }

    private String postBody(User user) {
        return user.getAge() == null ? prepareAgeZipCodeNullBody(user.getAge(), user.getName(), user.getSex(), user.getZipCode()) :
                prepareBody(user.getAge(), user.getName(), user.getSex(), user.getZipCode());
    }

    public Sex getRandomSex() {
        final List<Sex> SexValues = List.of(Sex.values());
        return SexValues.get(RandomUtils.nextInt(0, SexValues.size()-1));
    }
}