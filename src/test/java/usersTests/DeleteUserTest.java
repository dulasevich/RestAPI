package usersTests;

import by.issoft.client.UserClient;
import by.issoft.client.ZipCodeClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class DeleteUserTest {
    private final static int SUCCESS_RESPONSE_CODE = 204;
    private final static int REQUIRED_FIELD_MISSED_RESPONSE_CODE = 409;
    private UserClient userClient;
    private ZipCodeClient zipCodeClient;
    private User userToDelete;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
        zipCodeClient = new ZipCodeClient();
        userClient.postUser(new User(RandomUtils.nextInt(0, 120),
                RandomStringUtils.randomAlphabetic(10),
                Sex.FEMALE, List.of(zipCodeClient.getZipCodes().as(String[].class)).get(0)));
        userToDelete = List.of(userClient.getUsers().as(User[].class)).get(0);
    }

    @Test
    void successDeleteUserTest() {
        userClient.deleteUser(userToDelete).then().statusCode(SUCCESS_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        List<String> zipCodes = List.of(zipCodeClient.getZipCodes().as(String[].class));
        Assertions.assertFalse(users.contains(userToDelete));
        Assertions.assertTrue(zipCodes.contains(userToDelete.getZipCode()));
    }

    @Test
    void deleteUserWithRequiredFieldTest() {
        userToDelete = new User(null, RandomStringUtils.randomAlphabetic(10), Sex.MALE, null);
        userClient.deleteUser(userToDelete).then().statusCode(SUCCESS_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(users.contains(userToDelete));
    }

    @Test
    void deleteUserNoRequiredFieldTest() {
        User userNoField = new User(userToDelete.getAge(), userToDelete.getName(), null, userToDelete.getZipCode());
        userClient.deleteUser(userNoField).then().statusCode(REQUIRED_FIELD_MISSED_RESPONSE_CODE);
    }
}
