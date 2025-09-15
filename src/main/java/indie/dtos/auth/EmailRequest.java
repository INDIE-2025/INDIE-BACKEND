package indie.dtos.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailRequest {
    @NotBlank
    @Email
    private String email;

    public EmailRequest() {}

    public EmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

