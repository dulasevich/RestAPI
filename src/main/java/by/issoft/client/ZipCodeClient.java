package by.issoft.client;

import by.issoft.httpClient.AccessType;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static by.issoft.client.AuthClient.BASE_URL;
import static io.restassured.RestAssured.given;

public class ZipCodeClient {
    private static final String GET_ZIPCODES_ENDPOINT = "/zip-codes";
    private static final String POST_ZIPCODES_EXPAND_ENDPOINT = "/zip-codes/expand";
    private static final Logger logger = LoggerFactory.getLogger(ZipCodeClient.class);

    @Step("Get all zipCodes")
    public Response getZipCodes() {
        logger.info("Getting all zipCodes");
        return given().header(
                        "Authorization", "Bearer " + AuthClient.getToken(AccessType.READ))
                .when()
                .get(BASE_URL + GET_ZIPCODES_ENDPOINT);
    }

    @Step("Create new zipCode(s)")
    public Response postZipCodes(String... zipcodes) {
        logger.info("Creating new zipCode");
        return given()
                .header("Authorization", "Bearer " + AuthClient.getToken(AccessType.WRITE))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(zipcodes)
                .when()
                .post(BASE_URL + POST_ZIPCODES_EXPAND_ENDPOINT);
    }

    @Step("Create zipCode for testing")
    public String createAvailableZipcode () {
        logger.info("Creating zipCode for testing");
        String zipcode = RandomStringUtils.randomNumeric(5);
        Response response = postZipCodes(zipcode);
        if(response.getStatusCode() == 201) {
            return zipcode;
        } else {
            logger.error("Failed to create available zipcode. Check POST /zip-codes/expand method.");
            throw new RuntimeException();
        }
    }
}
