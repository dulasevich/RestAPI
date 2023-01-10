package usersTests;

import by.issoft.client.AuthClient;
import by.issoft.dto.Sex;
import by.issoft.dto.User;
import by.issoft.httpClient.AccessType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GetUserTest {

    private static final String BASE_URL = "http://localhost:51000";
    private static final int RESPONSE_CODE = 200;

    @Test
    void getUserTest() {
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when()
                .get(BASE_URL + "/users");
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = Arrays.stream(response.getBody().as(User[].class)).toList();
        Assertions.assertNotNull(users);
    }

    @Test
    void getOlderUsersTest() {
        int ageToCheck = 90;
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .param("olderThan", ageToCheck)
                .when()
                .get(BASE_URL + "/users");
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = Arrays.stream(response.getBody().as(User[].class)).toList();
        users.forEach(user -> Assertions.assertTrue(user.getAge() > ageToCheck));
    }

    @Test
    void getYoungerUsersTest() {
        int ageToCheck = 20;
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .param("youngerThan", ageToCheck)
                .when()
                .get(BASE_URL + "/users");
//                .then()
//                .statusCode(RESPONSE_CODE)
//                .body("age", Arrays.toString(ageToCheck));
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = Arrays.stream(response.getBody().as(User[].class)).toList();
        users.forEach(user -> Assertions.assertTrue(user.getAge() < ageToCheck));
    }

    @Test
    void getUserBySexTest() {
        Response response = given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .param("sex", Sex.FEMALE.toString())
                .when()
                .get(BASE_URL + "/users");
        response.then().statusCode(RESPONSE_CODE);
        List<User> users = Arrays.stream(response.getBody().as(User[].class)).toList();
        users.forEach(user -> Assertions.assertNotEquals(user.getSex(), Sex.MALE));
    }
}
