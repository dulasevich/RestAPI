package usersTests;

import by.issoft.client.UserClient;
import by.issoft.client.ZipCodeClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.dto.UserPairToUpdate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UpdateUserTest {

    private final static int SUCCESS_RESPONSE_CODE = 200;
    private final static int NO_SUCH_ZIPCODE_RESPONSE_CODE = 424;
    private final static int REQUIRED_FIELD_MISSED_RESPONSE_CODE = 409;

    private UserClient userClient;
    private User userToUpdate;
    private ZipCodeClient zipCodeClient;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
        zipCodeClient = new ZipCodeClient();
        userToUpdate = userClient.createAvailableUser(new User(RandomUtils.nextInt(0, 120),
                RandomStringUtils.randomAlphabetic(10),
                Sex.FEMALE, zipCodeClient.getZipCodes().getBody().get(0)));
    }

    @Test
    void successUpdateUserTest() {
        User newUser = new User(RandomUtils.nextInt(0, 120), userToUpdate.getName(),
                userClient.setOppositeSex(userToUpdate.getSex()), userToUpdate.getZipCode());
        int statusCode = userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate));
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, statusCode);
        Assertions.assertFalse(users.contains(userToUpdate));
        Assertions.assertTrue(users.contains(newUser));
    }

    @Test
    void incorrectZipcodeUpdateUserTest() {
        User newUser = new User(userToUpdate.getAge(), userToUpdate.getName(),
                userToUpdate.getSex(), String.valueOf(RandomUtils.nextInt(0, 100)));
        int statusCode = userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate));
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(NO_SUCH_ZIPCODE_RESPONSE_CODE, statusCode);
        Assertions.assertFalse(users.contains(newUser));
    }

    @Test
    void noRequiredFieldsUpdateUserTest() {
        User newUser = new User(userToUpdate.getAge(), null,
                null, userToUpdate.getZipCode());
        int statusCode = userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate));
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(REQUIRED_FIELD_MISSED_RESPONSE_CODE, statusCode);
        Assertions.assertFalse(users.contains(newUser));
    }
}