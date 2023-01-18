package usersTests;

import by.issoft.client.UserClient;
import by.issoft.dto.User;
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

    @BeforeEach
    void initUserClient() {
        userClient = new UserClient();
    }

    @Test
    @Order(1)
    void successUploadUserTest() {
        userClient.uploadUser(new File("src/test/resources/filesToUpload/successUsers.json"))
                .then().statusCode(SUCCESS_RESPONSE_CODE);
        List<User> fileUsers = userClient.getUsersFromFile(new File("src/test/resources/filesToUpload/successUsers.json"));
        List<User> usersAfterUpload = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertTrue(usersAfterUpload.containsAll(fileUsers));
    }

    @Test
    @Order(2)
    void incorrectZipUploadUserTest() {
        List<User> fileUsers = userClient
                .getUsersFromFile(new File("src/test/resources/filesToUpload/incorrectZip.json"));
        userClient.uploadUser(new File("src/test/resources/filesToUpload/incorrectZip.json"))
                .then().statusCode(NO_SUCH_ZIPCODE_RESPONSE_CODE);
        List<User> usersAfterUpload = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(usersAfterUpload.containsAll(fileUsers));
    }

    @Test
    @Order(3)
    void noRequiredFieldUploadUserTest() {
        List<User> fileUsers = userClient
                .getUsersFromFile(new File("src/test/resources/filesToUpload/noRequiredField.json"));
        userClient.uploadUser(new File("src/test/resources/filesToUpload/noRequiredField.json"))
                .then().statusCode(REQUIRED_FIELD_MISSED_RESPONSE_CODE);
        List<User> usersAfterUpload = List.of(userClient.getUsers().as(User[].class));
        Assertions.assertFalse(usersAfterUpload.containsAll(fileUsers));
    }

    @AfterAll
    void deleteUsersAfterUpload() {
        List<User> users = List.of(userClient.getUsers().as(User[].class));
        users.forEach(user -> userClient.deleteUser(user));
    }
}