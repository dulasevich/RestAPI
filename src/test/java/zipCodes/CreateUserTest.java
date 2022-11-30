package zipCodes;

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
    private Integer age;
    private String name;
    private String zipCode;
    private UserClient userClient;
    private ZipCodeClient zipCodeClient;

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
        int response = userClient.postUser(user);
        List<User> users = userClient.getUsers().getBody();
        List<String> zipCodes = zipCodeClient.getZipCodes().getBody();
        Assertions.assertEquals(POST_RESPONSE_CODE, response);
        Assertions.assertTrue(users.contains(user));
        Assertions.assertFalse(zipCodes.contains(zipCode));
    }

    @Test
    void postUserRequiredFieldsTest() {
        User user = new User(null, name, Sex.MALE, null);
        int response = userClient.postUser(user);
        List<User> users = userClient.getUsers().getBody();
        Assertions.assertEquals(POST_RESPONSE_CODE, response);
        Assertions.assertTrue(users.contains(user));
    }

    @Test
    void postIncorrectZipCodeUser () {
        zipCode = RandomStringUtils.randomNumeric(10);
        User user = new User(age, name, Sex.FEMALE, zipCode);
        int response = userClient.postUser(user);
        Assertions.assertEquals(424, response);
    }

    @Test()
    void postExistingUserTest() {
        List<User> users = userClient.getUsers().getBody();
        User user = new User(age, users.get(0).getName(), users.get(0).getSex(), zipCode);
        int response = userClient.postUser(user);
        Assertions.assertEquals(400, response);
    }
}
