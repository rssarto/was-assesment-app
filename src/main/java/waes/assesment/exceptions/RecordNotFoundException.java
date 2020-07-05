package waes.assesment.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public static final String MESSAGE_TEMPLATE = "The id %s was not found";

    public RecordNotFoundException(String message) {
        super(message);
    }
}
