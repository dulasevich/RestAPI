package zipCodes;

import by.issoft.ResponseEntity;
import by.issoft.client.UserClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetUserTest {

    private static final int RESPONSE_CODE = 200;
    private UserClient userClient;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
    }

    @Test
    void getUserTest() {
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(RESPONSE_CODE, userClient.getUsers().getStatusCode());
        Assertions.assertNotNull(users);
    }

    @Test
    void getOlderUsersTest() {
        Integer ageToCheck = 40;
        ResponseEntity<List<User>> users = userClient.getUsers("olderThan", ageToCheck.toString());
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        users.getBody().forEach(user -> Assertions.assertTrue(user.getAge() > ageToCheck));
    }

    @Test
    void getYoungerUsersTest() {
        Integer ageToCheck = 20;
        ResponseEntity<List<User>> users = userClient.getUsers("youngerThan", ageToCheck.toString());
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        users.getBody().forEach(user -> Assertions.assertTrue(user.getAge() < ageToCheck));
    }

    @Test
    void getUserBySexTest() {
        ResponseEntity<List<User>> users = userClient.getUsers("sex", Sex.FEMALE.toString());
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        users.getBody().forEach(user -> Assertions.assertNotEquals(user.getSex(), Sex.MALE));
    }
}
