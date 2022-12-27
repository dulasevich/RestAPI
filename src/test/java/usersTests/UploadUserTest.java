package usersTests;

import by.issoft.client.UserClient;
import by.issoft.dto.User;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadUserTest {

    private final static int SUCCESS_RESPONSE_CODE = 201;
    private final static int NO_SUCH_ZIPCODE_RESPONSE_CODE = 424;
    private final static int REQUIRED_FIELD_MISSED_RESPONSE_CODE = 409;
    private final Logger logger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private UserClient userClient;

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
        logger.setLevel(Level.DEBUG);
    }

    @Test
    @Order(1)
    void successUploadUserTest() {
        List<User> fileUsers = userClient.getUsersFromFile(new File("src/test/resources/filesToUpload/successUsers.json"));
        int statusCode = userClient.uploadUser(new File("src/test/resources/filesToUpload/successUsers.json"),
                "successUsers").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertEquals(SUCCESS_RESPONSE_CODE, statusCode);
        Assertions.assertTrue(usersAfterUpload.containsAll(fileUsers));
    }

    @Test
    @Order(2)
    void incorrectZipUploadUserTest() {
        List<User> fileUsers = userClient
                .getUsersFromFile(new File("src/test/resources/filesToUpload/incorrectZip.json"));
        int statusCode = userClient.uploadUser(new File("src/test/resources/filesToUpload/incorrectZip.json"),
                "incorrectZip").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertFalse(usersAfterUpload.containsAll(fileUsers));
        Assertions.assertEquals(NO_SUCH_ZIPCODE_RESPONSE_CODE, statusCode);
    }

    @Test
    @Order(3)
    void noRequiredFieldUploadUserTest() {
        List<User> fileUsers = userClient
                .getUsersFromFile(new File("src/test/resources/filesToUpload/noRequiredField.json"));
        int statusCode = userClient.uploadUser(new File("src/test/resources/filesToUpload/noRequiredField.json"),
                "noRequiredField").getStatusLine().getStatusCode();
        List<User> usersAfterUpload = userClient.getUsers().getBody();
        Assertions.assertFalse(usersAfterUpload.containsAll(fileUsers));
        Assertions.assertEquals(REQUIRED_FIELD_MISSED_RESPONSE_CODE, statusCode);
    }

    @AfterAll
    void deleteUsersAfterUpload() {
        List<User> users = userClient.getUsers().getBody();
        users.forEach(user -> userClient.deleteUser(user));
    }
}