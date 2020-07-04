package waes.assesment.exceptions;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException(String message){
        super(message);
    }
}
