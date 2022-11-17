package by.issoft.dto;

import lombok.Data;

@Data
public class Auth {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
}
