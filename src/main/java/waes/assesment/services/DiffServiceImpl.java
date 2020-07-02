package waes.assesment.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

@Service
public class DiffServiceImpl implements DiffService {

    private final DiffStorage diffStorage;

    public DiffServiceImpl(@Qualifier("inMemoryDiffStorageImpl") DiffStorage diffStorage) {
        this.diffStorage = diffStorage;
    }

    @Override
    public void create(final UUID id, DiffDataDTO diffDataDTO) {
        this.diffStorage.create(id, diffDataDTO);
    }

    @Override
    public DiffDataEntity findByd(UUID diffDataId) {
        return this.diffStorage.findById(diffDataId);
    }
}
