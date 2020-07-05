package waes.assesment.resources.entities;

import waes.assesment.resources.enums.DataType;

public interface DiffContent {
    String base64Content();

    DataType dataType();
}
