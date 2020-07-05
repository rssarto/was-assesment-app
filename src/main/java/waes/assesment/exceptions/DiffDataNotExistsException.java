package waes.assesment.exceptions;

import waes.assesment.resources.enums.DataType;

public class DiffDataNotExistsException extends RuntimeException {
    public static final String DIFF_DATA_MESSAGE_TEMPLATE = "The '%s' does not exist.";

    public DiffDataNotExistsException(DataType dataType) {
        super(String.format(DIFF_DATA_MESSAGE_TEMPLATE, dataType.name()));
    }
}
