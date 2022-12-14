package usersTests;

import by.issoft.client.UserClient;
import by.issoft.client.ZipCodeClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadUserTest {

    private final static int SUCCESS_RESPONSE_CODE = 201;
    private final static int NO_SUCH_ZIPCODE_RESPONSE_CODE = 424;
    private final static int REQUIRED_FIELD_MISSED_RESPONSE_CODE = 409;

    private UserClient userClient;
    private List<User> fileUsers;
    private ZipCodeClient zipCodeClient;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
        zipCodeClient = new ZipCodeClient();
        userClient.createAvailableUser(new User(RandomUtils.nextInt(0, 120),
                RandomStringUtils.randomAlphabetic(10),
                Sex.FEMALE, zipCodeClient.getZipCodes().getBody().get(0)));
        fileUsers = userClient.getUsersFromFile(new File("filesToUpload/successUsers.json"));
    }

    @Test
    @Order(1)
    void successUploadUserTest() {
        int statusCode = userClient.uploadUser(new File("filesToUpload/successUsers.json"),
                "successUsers").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, statusCode);
        Assertions.assertEquals(usersAfterUpload, fileUsers);
    }

    @Test
    @Order(2)
    void incorrectZipUploadUserTest() {
        int statusCode = userClient.uploadUser(new File("filesToUpload/incorrectZip.json"),
                "incorrectZip").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertEquals(NO_SUCH_ZIPCODE_RESPONSE_CODE, statusCode);
        Assertions.assertNotEquals(usersAfterUpload, fileUsers);
    }

    @Test
    @Order(3)
    void noRequiredFieldUploadUserTest() {
        int statusCode = userClient.uploadUser(new File("filesToUpload/noRequiredField.json"),
                "noRequiredField").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertEquals(REQUIRED_FIELD_MISSED_RESPONSE_CODE, statusCode);
        Assertions.assertNotEquals(usersAfterUpload, fileUsers);
    }

    @AfterAll
    void deleteUsersAfterUploda() {
        List<User> users = userClient.getUsers().getBody();
        users.forEach(user -> userClient.deleteUser(user));
    }
}
