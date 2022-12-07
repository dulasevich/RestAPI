package zipCodes;

import by.issoft.ResponseEntity;
import by.issoft.client.UserClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.apache.http.message.BasicNameValuePair;
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
        ResponseEntity<List<User>> users = userClient.getUsers();
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        Assertions.assertNotNull(users.getBody());
    }

    @Test
    void getOlderUsersTest() {
        int ageToCheck = 40;
        ResponseEntity<List<User>> users = userClient
                .getUsers(List.of(new BasicNameValuePair("olderThan", String.valueOf(ageToCheck))));
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        Assertions.assertTrue(users.getBody().stream().allMatch(user -> user.getAge() > ageToCheck));
    }

    @Test
    void getYoungerUsersTest() {
        int ageToCheck = 20;
        ResponseEntity<List<User>> users = userClient
                .getUsers(List.of(new BasicNameValuePair("youngerThan", String.valueOf(ageToCheck))));
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        users.getBody().forEach(user -> Assertions.assertTrue(user.getAge() < ageToCheck));
    }

    @Test
    void getUserBySexTest() {
        ResponseEntity<List<User>> users = userClient
                .getUsers(List.of(new BasicNameValuePair("sex", Sex.FEMALE.toString())));
        Assertions.assertEquals(RESPONSE_CODE, users.getStatusCode());
        users.getBody().forEach(user -> Assertions.assertNotEquals(user.getSex(), Sex.MALE));
    }
}
