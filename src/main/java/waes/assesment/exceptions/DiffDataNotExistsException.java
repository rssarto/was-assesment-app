package waes.assesment.exceptions;

import waes.assesment.resources.enums.DataType;

public class DiffDataNotExistsException extends RuntimeException {
    public DiffDataNotExistsException(DataType dataType){
        super(String.format("The '%s' does not exist.", dataType.name()));
    }
}
