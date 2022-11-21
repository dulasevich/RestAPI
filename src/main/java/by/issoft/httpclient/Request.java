package by.issoft.httpclient;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.String.format;

public class Request {

    private final HttpRequest rawRequest;
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private Request(String url, HttpMethod method) {
        switch (method) {
            case GET -> rawRequest = new HttpGet(url);
            case PUT -> rawRequest = new HttpPut(url);
            case POST -> rawRequest = new HttpPost(url);
            case DELETE -> rawRequest = new HttpDelete(url);
            default -> throw new RuntimeException("Unknown request method");
        }
    }

    public static Request get(String url) {
        return new Request(url, HttpMethod.GET);
    }

    public static Request put(String url) {
        return new Request(url, HttpMethod.PUT);
    }

    public static Request post(String url) {
        return new Request(url, HttpMethod.POST);
    }

    public static Request delete(String url) {
        return new Request(url, HttpMethod.DELETE);
    }

    public Request addParameter(String key, String value) {
        try {
            URI uri = new URIBuilder(rawRequest.getRequestLine().getUri()).addParameter(key, value).build();
            ((HttpRequestBase) rawRequest).setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Request addHeader(String key, String value) {
        rawRequest.addHeader(key, value);
        return this;
    }

    public Request addContentType(String value) {
        return addHeader("Content-Type", value);
    }

    public Request addBasicAuth(String login, String password) {
        String encodedAuthorization = "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes());
        addHeader("Authorization", encodedAuthorization);
        return this;
    }

    public Request addBearerTokenAuth(String accessToken) {
        addHeader("Authorization", format("Bearer %s", accessToken));
        return this;
    }

    @SneakyThrows
    public Request addJsonBody(String body) {
        StringEntity entity = new StringEntity(body);
        ((HttpPost) rawRequest).setEntity(entity);
        return this;
    }

    public HttpResponse send() {
        try {
            return httpClient.execute((HttpUriRequest) rawRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum HttpMethod {
        GET, PUT, POST, DELETE
    }
}
