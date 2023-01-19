package usersTests;

import by.issoft.client.UserClient;
import by.issoft.client.ZipCodeClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import java.util.List;
public class CreateUserTest {
    private final static int POST_RESPONSE_CODE = 201;
    private UserClient userClient;
    private ZipCodeClient zipCodeClient;
    private Integer age;
    private String name;
    private String zipCode;

    @BeforeEach
    void initUser() {
        userClient = new UserClient();
        zipCodeClient = new ZipCodeClient();
        age = RandomUtils.nextInt(0, 120);
        name = RandomStringUtils.randomAlphabetic(10);
        zipCode = zipCodeClient.createAvailableZipcode();
    }

    @Test
    void postUserAllFieldsTest() {
        User user = new User(age, name, Sex.FEMALE, zipCode);
        userClient.postUser(user).then().statusCode(POST_RESPONSE_CODE);
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        List<String> zipCodes = List.of(zipCodeClient.getZipCodes().as(String[].class));

        Assertions.assertTrue(users.contains(user));
        Assertions.assertFalse(zipCodes.contains(zipCode));
    }

    @Test
    void postUserRequiredFieldsTest() {
        User user = new User(null, name, Sex.MALE, null);
        userClient.postUser(user).then().statusCode(POST_RESPONSE_CODE);
        List <User> users = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertTrue(users.contains(user));
    }

    @Test
    void postIncorrectZipCodeUser () {
        zipCode = RandomStringUtils.randomNumeric(10);
        User user = new User(age, name, Sex.FEMALE, zipCode);
        userClient.postUser(user).then().statusCode(424);
    }

    @Test()
    void postExistingUserTest() {
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        User user = new User(age, users.get(0).getName(), users.get(0).getSex(), zipCode);
        userClient.postUser(user).then().statusCode(400);
    }
}
