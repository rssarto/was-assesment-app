package waes.assesment.services;

import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

public interface DiffService {
    void create(UUID id, DiffDataDTO diffDataDTO);
    DiffDataEntity findByd(UUID diffDataId);
}
