package waes.assesment.services;

import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.dto.DiffResultDTO;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.Map;
import java.util.UUID;

public interface DiffService {
    void create(UUID id, DiffDataDTO diffDataDTO);

    Map<DataType, DiffDataEntity> findByd(UUID diffDataId);

    DiffResultDTO compare(UUID diffDataId);
}
