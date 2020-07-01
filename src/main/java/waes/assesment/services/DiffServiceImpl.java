package waes.assesment.services;

import org.springframework.stereotype.Service;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

@Service
public class DiffServiceImpl implements DiffService {

    private final DiffStorage diffStorage;

    public DiffServiceImpl(DiffStorage diffStorage) {
        this.diffStorage = diffStorage;
    }

    @Override
    public void create(DiffDataDTO diffDataDTO) {
    }

    @Override
    public DiffDataEntity findByd(UUID diffDataId) {
        return null;
    }
}
