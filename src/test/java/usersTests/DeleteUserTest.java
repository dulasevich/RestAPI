package usersTests;

import by.issoft.client.UserClient;
import by.issoft.client.ZipCodeClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DeleteUserTest {

    private final static int SUCCESS_RESPONSE_CODE = 204;
    private final static int REQUIRED_FIELD_MISSED_RESPONSE_CODE = 409;
    private UserClient userClient;
    private ZipCodeClient zipCodeClient;
    private User userToDelete;
    private final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
        zipCodeClient = new ZipCodeClient();
        userToDelete = userClient.createAvailableUser(new User(RandomUtils.nextInt(0, 120),
                RandomStringUtils.randomAlphabetic(10),
                Sex.FEMALE,
                zipCodeClient.getZipCodes().getBody().get(0)));
        logger.setLevel(Level.DEBUG);
    }

    @Test
    void successDeleteUserTest() {
        int statusCode = userClient.deleteUser(userToDelete);
        List<User> users = userClient.getUsers().getBody();
        List<String> zipCodes = zipCodeClient.getZipCodes().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, statusCode);
        Assertions.assertFalse(users.contains(userToDelete));
        Assertions.assertTrue(zipCodes.contains(userToDelete.getZipCode()));
    }

    @Test
    void deleteUserWithRequiredFieldTest() {
        userToDelete = new User(null, RandomStringUtils.randomAlphabetic(10), Sex.MALE, null);
        int statusCode = userClient.deleteUser(userToDelete);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, statusCode);
        Assertions.assertFalse(users.contains(userToDelete));
    }

    @Test
    void deleteUserNoRequiredFieldTest() {
        User userNoField = new User(userToDelete.getAge(), userToDelete.getName(), null, userToDelete.getZipCode());
        int statusCode = userClient.deleteUser(userNoField);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(REQUIRED_FIELD_MISSED_RESPONSE_CODE, statusCode);
        Assertions.assertTrue(users.contains(userToDelete));
    }
}
