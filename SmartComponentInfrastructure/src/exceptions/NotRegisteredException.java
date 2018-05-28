package exceptions;

public class NotRegisteredException extends Exception {

	private static final long serialVersionUID = 1L;

    public NotRegisteredException() {
        super();
    }

    public NotRegisteredException(String message) {
        super(message);
    }

    
    public NotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotRegisteredException(Throwable cause) {
        super(cause);
    }
}
