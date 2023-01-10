package usersTests;

import by.issoft.client.AuthClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.httpClient.AccessType;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateUserTest {

    private final static String BASE_URL = "http://localhost:51000";
    private final static int POST_RESPONSE_CODE = 201;
    private Integer age;
    private String name;
    private String zipCode;

    @BeforeEach
    void initUser() {
        age = RandomUtils.nextInt(0, 120);
        name = RandomStringUtils.randomAlphabetic(10);
        zipCode = Arrays.stream(given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("[" + RandomStringUtils.randomNumeric(5) + "]")
                .when()
                .post(BASE_URL + "/zip-codes/expand")
                .getBody()
                .as(String[].class))
                .toList().get(0);
    }

    @Test
    void postUserAllFieldsTest() {
        User user = new User(age, name, Sex.FEMALE, zipCode);
        given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/users")
                .then()
                .statusCode(POST_RESPONSE_CODE);

        List <User> users = Arrays.stream(given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when().get(BASE_URL + "/users")
                .getBody().as(User[].class)).toList();
        List<String> zipCodes = Arrays.stream(given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when().get(BASE_URL + "/zip-codes")
                .getBody().as(String[].class)).toList();

        Assertions.assertTrue(users.contains(user));
        Assertions.assertFalse(zipCodes.contains(zipCode));
    }

    @Test
    void postUserRequiredFieldsTest() {
        User user = new User(null, name, Sex.MALE, null);
        given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/users")
                .then()
                .statusCode(POST_RESPONSE_CODE);
        List <User> users = Arrays.stream(given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when().get(BASE_URL + "/users")
                .getBody().as(User[].class)).toList();
        Assertions.assertTrue(users.contains(user));
    }

    @Test
    void postIncorrectZipCodeUser () {
        zipCode = RandomStringUtils.randomNumeric(10);
        User user = new User(age, name, Sex.FEMALE, zipCode);
        given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/users")
                .then()
                .statusCode(424);
    }

    @Test()
    void postExistingUserTest() {
        List<User> users = Arrays.stream(given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when()
                .get(BASE_URL + "/users")
                .getBody()
                .as(User[].class))
                .toList();
        User user = new User(age, users.get(0).getName(), users.get(0).getSex(), zipCode);
        given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + "/users")
                .then()
                .statusCode(400);
    }
}
