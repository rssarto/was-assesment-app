package waes.assesment.exceptions;

public class RecordAlreadyExistsException extends RuntimeException {
    public static final String MESSAGE_TEMPLATE = "The id %s already exists, please use another id.";

    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
