package waes.assesment.services;

import org.springframework.stereotype.Service;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDiffStorageImpl implements DiffStorage {
    private final Map<UUID, DiffDataEntity> diffDataStorage;

    public InMemoryDiffStorageImpl() {
        this.diffDataStorage = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new {@link DiffDataEntity}
     * @param id
     * @param diffContent
     */
    @Override
    public void create(final UUID id, final DiffContent diffContent) {
        try{
            /**
             * Check if id already exists in the storage
             */
            this.findById(id);
        }catch (IllegalArgumentException ex){
            /**
             * The id was not found in the storage so create a new one.
             */
            final DiffDataEntity dataEntity = new DiffDataEntity(id, diffContent.base64Content());
            this.diffDataStorage.put(dataEntity.getId(), dataEntity);
            return;
        }
        throw new IllegalArgumentException(String.format("The id %s already exists, please use another id.", id.toString()));
    }

    /**
     *
     * @param diffDataId
     * @return
     */
    @Override
    public DiffDataEntity findById(final UUID diffDataId) {
        this.diffDataStorage.computeIfAbsent(diffDataId, (uuid) -> {
            throw new IllegalArgumentException(String.format("The id %s was not found", uuid.toString()));
        });
        return this.diffDataStorage.get(diffDataId);
    }
}
