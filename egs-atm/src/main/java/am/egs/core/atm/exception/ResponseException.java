package am.egs.core.atm.exception;

public class ResponseException extends RuntimeException {
    private int status = 500;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
