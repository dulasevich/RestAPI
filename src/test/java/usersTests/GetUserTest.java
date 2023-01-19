package usersTests;

import by.issoft.client.UserClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
public class GetUserTest {
    private static final int RESPONSE_CODE = 200;
    private UserClient userClient;

    @BeforeEach
    public void initUserClient() {
        userClient = new UserClient();
    }

    @Test
    void getUserTest() {
        Response response = userClient.getUsers();
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = List.of(response.getBody().as(User[].class));
        Assertions.assertNotNull(users);
    }

    @Test
    void getOlderUsersTest() {
        int ageToCheck = 90;
        Response response = userClient.getUsers("olderThan", String.valueOf(ageToCheck));
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = List.of(response.getBody().as(User[].class));
        users.forEach(user -> Assertions.assertTrue(user.getAge() > ageToCheck));
    }

    @Test
    void getYoungerUsersTest() {
        int ageToCheck = 20;
        Response response = userClient.getUsers("youngerThan", String.valueOf(ageToCheck));
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = List.of(response.getBody().as(User[].class));
        users.forEach(user -> Assertions.assertTrue(user.getAge() < ageToCheck));
    }

    @Test
    void getUserBySexTest() {
        Response response = userClient.getUsers("sex", Sex.FEMALE.toString());
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = List.of(response.getBody().as(User[].class));
        users.forEach(user -> Assertions.assertNotEquals(user.getSex(), Sex.MALE));
    }
}
