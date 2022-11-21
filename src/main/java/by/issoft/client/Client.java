package by.issoft.client;

import by.issoft.httpclient.Request;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

public class Client {

    final static String BASE_URL = "http://localhost:49000";

    public static HttpResponse doGet(String endpoint) {
        return Request.get(BASE_URL + endpoint).send();
    }

    public static HttpResponse doGet(String endpoint, List<BasicNameValuePair> params) {
        Request request = Request.get(BASE_URL + endpoint);
        params.forEach(pair -> request.addParameter(pair.getName(), pair.getValue()));
        return request.send();
    }

    public static HttpResponse doGet(String endpoint, String bearerToken) {
        return Request.get(BASE_URL + endpoint).addBearerTokenAuth(bearerToken).send();
    }

    public static HttpResponse doGet(String endpoint, String login, String password) {
        return Request.get(BASE_URL + endpoint).addBasicAuth(login, password).send();
    }

    public static HttpResponse doGet(String endpoint, List<BasicNameValuePair> params, String login, String password) {
        Request request = Request.get(BASE_URL + endpoint);
        params.forEach(pair -> request.addParameter(pair.getName(), pair.getValue()));
        return request.addBasicAuth(login, password).send();
    }

    public static HttpResponse doPost(String endpoint, String bearerToken, String jsonBody) {
        return Request.post(BASE_URL + endpoint)
                .addBearerTokenAuth(bearerToken)
                .addContentType("application/json")
                .addJsonBody(jsonBody)
                .send();
    }

    public static HttpResponse doPost(String endpoint, String login, String password, String jsonBody) {
        return Request.post(BASE_URL + endpoint)
                .addBasicAuth(login, password)
                .addContentType("application/json")
                .addJsonBody(jsonBody)
                .send();
    }

    public static HttpResponse doPost(String endpoint, List<BasicNameValuePair> params, String login, String password) {
        Request request = Request.post(BASE_URL + endpoint);
        params.forEach(pair -> request.addParameter(pair.getName(), pair.getValue()));
        return request.addBasicAuth(login, password).send();
    }
}
