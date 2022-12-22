package by.issoft.httpClient;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Request {

    private final HttpRequest request;
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private Request(String url, HttpMethod method) {
        switch (method) {
            case GET -> request = new HttpGet(url);
            case PUT -> request = new HttpPut(url);
            case POST -> request = new HttpPost(url);
            case DELETE -> request = new HttpDeleteWithBody(url);
            default -> throw new RuntimeException("Unknown request method");
        }
        setRequestTimeouts();
    }

    private void setRequestTimeouts() {
        ((HttpRequestBase) request).setConfig(RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build());
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
            URI uri = new URIBuilder(request.getRequestLine().getUri()).addParameter(key, value).build();
            ((HttpRequestBase) request).setURI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public Request addHeader(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public Request addBasicAuth(String login, String password) {
        addHeader("Authorization", "Basic " + Base64.encodeBase64String((login + ":" + password).getBytes()));
        return this;
    }

    public Request addBearerTokenAuth(String accessToken) {
        addHeader("Authorization", "Bearer " + accessToken);
        return this;
    }

    public Request addJsonBody(String body) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
        return this;
    }

    public Request attachFileToBody(File file, String fileName) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, fileName);

        ((HttpEntityEnclosingRequestBase) request).setEntity(builder.build());
        return this;
    }

    public HttpResponse send() {
        try {
            return httpClient.execute((HttpUriRequest) request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

        @Override
        public String getMethod() {
            return "DELETE";
        }

        public HttpDeleteWithBody(String URL) {
            super();
            setURI(URI.create(URL));
        }
    }
}