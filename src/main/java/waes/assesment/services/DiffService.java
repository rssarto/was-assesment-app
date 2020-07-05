package waes.assesment.services;

import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.dto.DiffResultDTO;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.Map;
import java.util.UUID;

public interface DiffService {
    /**
     * Create a new record.
     * @param id
     * @param diffDataDTO
     */
    void create(UUID id, DiffDataDTO diffDataDTO);

    /**
     * Find by id.
     * @param diffDataId
     * @return
     */
    Map<DataType, DiffDataEntity> findByd(UUID diffDataId);

    /**
     * Compare the pair data.
     * @param diffDataId
     * @return
     */
    DiffResultDTO compare(UUID diffDataId);
}
