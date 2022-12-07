package by.issoft.client;

import by.issoft.httpClient.AccessType;
import by.issoft.httpClient.Request;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class Client {

    public final static String BASE_URL = "http://localhost:49000";

    public static HttpResponse doGet(String endpoint) {
        return Request.get(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.READ))
                .send();
    }

    public static HttpResponse doGet(String endpoint, List<BasicNameValuePair> params) {
        Request request = Request.get(BASE_URL + endpoint);
        params.forEach(pair -> request.addParameter(pair.getName(), pair.getValue()));
        return request.addBearerTokenAuth(AuthClient.getToken(AccessType.READ)).send();
    }

    public static HttpResponse doPost(String endpoint, String body) {
        return Request.post(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .addHeader("Content-Type", "application/json")
                .addJsonBody(body)
                .send();
    }

    public static HttpResponse doPut(String endpoint, String body) {
        return Request.put(BASE_URL + endpoint)
                .addBearerTokenAuth(AuthClient.getToken(AccessType.WRITE))
                .addHeader("Content-Type", "application/json")
                .addJsonBody(body)
                .send();
    }
}
