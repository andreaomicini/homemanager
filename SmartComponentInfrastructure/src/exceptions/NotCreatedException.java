package exceptions;

public class NotCreatedException extends Exception {
	
	private static final long serialVersionUID = 1L;

    public NotCreatedException() {
        super();
    }

    public NotCreatedException(String message) {
        super(message);
    }

    
    public NotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotCreatedException(Throwable cause) {
        super(cause);
    }
}
