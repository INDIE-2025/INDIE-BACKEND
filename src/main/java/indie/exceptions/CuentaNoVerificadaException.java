package indie.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CuentaNoVerificadaException extends AuthenticationException {
    public CuentaNoVerificadaException(String message) {
        super(message);
    }
}