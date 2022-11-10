import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

public class Response {

    private final static String name = "0oa157tvtugfFXEhU4x7";
    private final static String password = "X7eBCXqlFC7x-mjxG5H91IRv_Bqe1oq7ZwXNA8aq";

    private CredentialsProvider getAuth() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials(name, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }

    public AuthResponseBody getAuthResponse(HttpPost request) throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(getAuth())
                .build();

        CloseableHttpResponse response = httpclient.execute(request);
        AuthResponseBody authResponseBody = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    authResponseBody = objectMapper.readValue(EntityUtils.toString(response.getEntity()),
                            AuthResponseBody.class);
                } finally {
                    instream.close();
                }
            }
        } finally {
            response.close();
        }
        return authResponseBody;
    }
}
