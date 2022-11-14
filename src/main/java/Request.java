import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class Request {

    public HttpPost postAuthRequest(String scopeValue) {
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("localhost:49000")
                    .setPath("/oauth/token")
                    .setParameter("grant_type", "client_credentials")
                    .setParameter("scope", scopeValue)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new HttpPost(uri);
    }
}
