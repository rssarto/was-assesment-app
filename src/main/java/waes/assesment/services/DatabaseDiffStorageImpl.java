package waes.assesment.services;

import org.springframework.stereotype.Service;
import waes.assesment.repositories.DiffDataRepository;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.UUID;

@Service
public class DatabaseDiffStorageImpl implements DiffStorage {

    private final DiffDataRepository diffDataRepository;

    public DatabaseDiffStorageImpl(DiffDataRepository diffDataRepository) {
        this.diffDataRepository = diffDataRepository;
    }

    @Override
    public void create(DiffContent diffContent) {
    }

    @Override
    public DiffDataEntity findById(UUID diffDataId) {
        return null;
    }
}
