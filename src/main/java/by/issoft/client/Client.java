package by.issoft.client;

import by.issoft.httpClient.AccessType;
import by.issoft.httpClient.Request;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class Client {

    public static final String BASE_URL = "http://localhost:51000";
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static HttpResponse doGet(String endpoint) {
        logger.info("Making get call to " + BASE_URL + endpoint);
        return Request.get(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.READ))
                .send();
    }

    public static HttpResponse doGet(String endpoint, List<BasicNameValuePair> params) {
        logger.info("Making get call to " + BASE_URL + endpoint);
        Request request = Request.get(BASE_URL + endpoint);
        params.forEach(pair -> request.addParameter(pair.getName(), pair.getValue()));
        return request.addBearerTokenAuth(AuthClient.getToken(AccessType.READ)).send();
    }

    public static HttpResponse doPost(String endpoint, String body) {
        logger.info("Posting  JSON body - " + body + " to " + BASE_URL + endpoint);
        return Request.post(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .addHeader("Content-Type", "application/json")
                .addJsonBody(body)
                .send();
    }

    public static HttpResponse doPut(String endpoint, String body) {
        logger.info("Updating user " + body + " on " + BASE_URL + endpoint);
        return Request.put(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .addHeader("Content-Type", "application/json")
                .addJsonBody(body)
                .send();
    }

    public static HttpResponse doDelete(String endpoint, String body) {
        logger.info("Deleting user " + body + " on " + BASE_URL + endpoint);
        return Request.delete(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .addHeader("Content-Type", "application/json")
                .addJsonBody(body)
                .send();
    }

    public static HttpResponse doPost(String endpoint, File file, String fileName){
        logger.info("Posting user(s) from " + file.getName() + " to " + BASE_URL + endpoint);
        return Request.post(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .attachFileToBody(file, fileName)
                .send();
    }
}
