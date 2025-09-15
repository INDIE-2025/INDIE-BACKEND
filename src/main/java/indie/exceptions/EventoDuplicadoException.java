package indie.exceptions;

public class EventoDuplicadoException extends RuntimeException {
    
    public EventoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}