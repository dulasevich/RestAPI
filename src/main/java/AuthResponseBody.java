import lombok.Data;

@Data
public class AuthResponseBody {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
}
