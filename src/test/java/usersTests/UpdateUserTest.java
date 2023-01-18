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
        userClient.createAvailableUser(new User(RandomUtils.nextInt(0, 120),
                RandomStringUtils.randomAlphabetic(10),
                Sex.FEMALE, List.of(zipCodeClient.getZipCodes().as(String[].class)).get(0)));
        userToUpdate = List.of(userClient.getUsers().as(User[].class)).get(0);
    }

    @Test
    void successUpdateUserTest() {
        User newUser = new User(RandomUtils.nextInt(0, 120), userToUpdate.getName(),
                userClient.setOppositeSex(userToUpdate.getSex()), userToUpdate.getZipCode());
        userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate)).then().statusCode(SUCCESS_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(users.contains(userToUpdate));
        Assertions.assertTrue(users.contains(newUser));
    }

    @Test
    void incorrectZipcodeUpdateUserTest() {
        User newUser = new User(userToUpdate.getAge(), userToUpdate.getName(),
                userToUpdate.getSex(), String.valueOf(RandomUtils.nextInt(0, 100)));
        userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate)).then().statusCode(NO_SUCH_ZIPCODE_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(users.contains(newUser));
    }

    @Test
    void noRequiredFieldsUpdateUserTest() {
        User newUser = new User(userToUpdate.getAge(), null,
                null, userToUpdate.getZipCode());
        userClient.updateUser(new UserPairToUpdate(newUser, userToUpdate)).then().statusCode(REQUIRED_FIELD_MISSED_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(users.contains(newUser));
    }
}