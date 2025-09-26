package indie.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private String jwtSecret;
    private long jwtExpirationMs;
    private String baseUrl;

    private ResetPassword resetPassword = new ResetPassword();
    private Verification verification = new Verification();
    private Cors cors = new Cors();

    @Getter
    @Setter
    public static class ResetPassword {
        private String url;
    }

    @Getter
    @Setter
    public static class Verification {
        private String url;
    }
    
    @Getter
    @Setter
    public static class Cors {
        private String allowedOrigins;
    }
}