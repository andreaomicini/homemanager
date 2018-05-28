package exceptions;

public class AuthorizationFailureException extends Exception {

	
	private static final long serialVersionUID = 1L;

    public AuthorizationFailureException() {
        super();
    }

    public AuthorizationFailureException(String message) {
        super(message);
    }

    
    public AuthorizationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationFailureException(Throwable cause) {
        super(cause);
    }
    
}
