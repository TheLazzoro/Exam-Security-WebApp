package errorhandling;

public class UserAlreadyExistsException extends Exception {
    private int errorCode;

    public UserAlreadyExistsException(String message) {
        super (message);
        this.errorCode = 403;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
