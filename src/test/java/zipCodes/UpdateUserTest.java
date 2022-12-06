package zipCodes;

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

public class UpdateUserTest {

    private final static int SUCCESS_RESPONSE_CODE = 200;
    private final static int FAILED_DEPENDENCY_RESPONSE_CODE = 424;
    private final static int CONFLICT_RESPONSE_CODE = 409;

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
        int response = userClient.updateUser(newUser, userToUpdate);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, response);
        Assertions.assertFalse(users.contains(userToUpdate));
        Assertions.assertTrue(users.contains(newUser));
    }

    @Test
    void incorrectZipcodeUpdateUserTest() {
        User newUser = new User(userToUpdate.getAge(), userToUpdate.getName(),
                userToUpdate.getSex(), String.valueOf(RandomUtils.nextInt(0, 100)));
        int response = userClient.updateUser(newUser, userToUpdate);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(FAILED_DEPENDENCY_RESPONSE_CODE, response);
        Assertions.assertTrue(users.contains(userToUpdate));
    }

    @Test
    void noRequiredFieldsUpdateUserTest() {
        User newUser = new User(null, userToUpdate.getName(),
                userToUpdate.getSex(), null);
        int response = userClient.updateUser(newUser, userToUpdate);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(CONFLICT_RESPONSE_CODE, response);
        Assertions.assertTrue(users.contains(userToUpdate));
    }
}
