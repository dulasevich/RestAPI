package by.issoft.client;

import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.dto.UserPairToUpdate;
import by.issoft.httpClient.AccessType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static by.issoft.client.AuthClient.BASE_URL;
import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USER_ENDPOINT = "/users";
    private static final String USER_UPLOAD_ENDPOINT = "/users/upload";
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);

    public UserClient() {
        this.objectMapper = new ObjectMapper();
    }

    @Step("Get all users")
    public Response getUsers() {
        logger.info("Getting all users");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when()
                .get(BASE_URL + USER_ENDPOINT);
    }

    @Step("Create user\n [{user}]")
    public Response postUser(User user) {
        logger.info("Creating new user: " + user);
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + USER_ENDPOINT);
    }

    @Step("Get users by parameter(s)")
    public Response getUsers(String parameter, String value) {
        logger.info("Getting " + parameter + " " + value +  " user(s)");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .param(parameter, value)
                .when()
                .get(BASE_URL + USER_ENDPOINT);

    }

    @Step("Update user")
    public Response updateUser(UserPairToUpdate userPairToUpdate) {
        logger.info("Updating user");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userPairToUpdate)
                .when()
                .patch(BASE_URL + USER_ENDPOINT);
    }

    public Sex setOppositeSex(Sex sex) {
        return sex == Sex.FEMALE ? Sex.MALE : Sex.FEMALE;
    }

    @Step("Create user for testing")
    public void createAvailableUser (User user) {
        logger.info("Creating user for testing");
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + USER_ENDPOINT);
        if(response.statusCode() != 201) {
            logger.error("Failed to create available user. Check POST /users method.");
        }
    }

    @Step("Delete user")
    public Response deleteUser(User user) {
        logger.info("Deleting user");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .delete(BASE_URL + USER_ENDPOINT);
    }

    @Step("Upload user")
    public Response uploadUser(File file) {
        logger.info("Uploading user(s)");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.MULTIPART)
                .accept(ContentType.JSON)
                .multiPart(file)
                .when()
                .post(BASE_URL + USER_UPLOAD_ENDPOINT);
    }

    public List<User> getUsersFromFile(File file) {
        try {
            return List.of(objectMapper.readValue(file, User[].class));
        } catch (IOException e) {
            logger.error("unable to load users");
            throw new RuntimeException();
        }
    }
}
