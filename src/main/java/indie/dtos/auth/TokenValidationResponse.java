package indie.dtos.auth;

public class TokenValidationResponse {
    private boolean valid;

    public TokenValidationResponse() {}

    public TokenValidationResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
