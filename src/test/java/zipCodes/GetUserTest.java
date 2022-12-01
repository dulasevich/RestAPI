package zipCodes;

import by.issoft.client.UserClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetUserTest {

    private static final int RESPONSE_CODE = 200;
    private static final User expectedUser = new User(31, "Dmitry", Sex.MALE, "12345");
    private UserClient userClient;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
    }

    @Test
    void getUserTest() {
        userClient.prepareExpectedUser();
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(RESPONSE_CODE, userClient.getUsers().getStatusCode());
        Assertions.assertTrue(users.contains(expectedUser));
    }

    @Test
    void getOlderUsersTest() {
        int ageToCheck = 30;
        List<User> users = userClient.getOlderThanUsers(ageToCheck);
        Assertions.assertEquals(RESPONSE_CODE, userClient.getUsers().getStatusCode());
        users.forEach(user -> Assertions.assertTrue(user.getAge() > ageToCheck));
    }

    @Test
    void getYoungerUsersTest() {
        int ageToCheck = 20;
        List<User> users = userClient.getYoungerThanUsers(ageToCheck);
        Assertions.assertEquals(RESPONSE_CODE, userClient.getUsers().getStatusCode());
        users.forEach(user -> Assertions.assertTrue(user.getAge() < ageToCheck));
    }

    @Test
    void getUserBySexTest() {
        List<User> users = userClient.getUsersBySex(Sex.MALE);
        Assertions.assertEquals(RESPONSE_CODE, userClient.getUsers().getStatusCode());
        users.forEach(user -> Assertions.assertNotEquals(user.getSex(), Sex.FEMALE));
    }
}
