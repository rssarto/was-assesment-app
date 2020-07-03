package waes.assesment.services;

import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.Map;
import java.util.UUID;

public interface DiffStorage {
    void create(final UUID id, DiffContent diffContent);
    Map<DataType, DiffDataEntity> findById(UUID diffDataId);
}
