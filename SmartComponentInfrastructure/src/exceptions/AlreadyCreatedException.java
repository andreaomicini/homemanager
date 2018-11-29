package exceptions;

public class AlreadyCreatedException extends Exception {

	private static final long serialVersionUID = 1L;

    public AlreadyCreatedException() {
        super();
    }

    public AlreadyCreatedException(String message) {
        super(message);
    }

    
    public AlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCreatedException(Throwable cause) {
        super(cause);
    }
}
