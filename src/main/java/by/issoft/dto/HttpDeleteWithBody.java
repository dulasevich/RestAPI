package by.issoft.dto;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

    @Override
    public String getMethod() {
        return "DELETE";
    }

    public HttpDeleteWithBody(String URL) {
        super();
        setURI(URI.create(URL));
    }
}
