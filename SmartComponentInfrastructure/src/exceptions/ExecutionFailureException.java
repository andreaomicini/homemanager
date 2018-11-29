package exceptions;

public class ExecutionFailureException extends Exception {

	private static final long serialVersionUID = 1L;

    public ExecutionFailureException() {
        super();
    }

    public ExecutionFailureException(String message) {
        super(message);
    }

    
    public ExecutionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionFailureException(Throwable cause) {
        super(cause);
    }
    
}
