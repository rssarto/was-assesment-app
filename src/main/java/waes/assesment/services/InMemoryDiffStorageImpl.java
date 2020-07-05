package waes.assesment.services;

import org.springframework.stereotype.Service;
import waes.assesment.exceptions.RecordAlreadyExistsException;
import waes.assesment.exceptions.RecordNotFoundException;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory Storage version
 */
@Service
public class InMemoryDiffStorageImpl implements DiffStorage {
    private final Map<UUID, Map<DataType, DiffDataEntity>> diffDataStorage;

    public InMemoryDiffStorageImpl() {
        this.diffDataStorage = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new {@link DiffDataEntity}
     *
     * @param id
     * @param diffContent
     * @throws RecordAlreadyExistsException
     */
    @Override
    public void create(final UUID id, final DiffContent diffContent) {
        final DiffDataEntity dataEntity = new DiffDataEntity(id, diffContent.base64Content(), diffContent.dataType());
        try {
            /**
             * Check if id already exists in the storage
             */
            final Map<DataType, DiffDataEntity> typeDiffDataEntityMap = this.findById(id);
            typeDiffDataEntityMap.computeIfPresent(diffContent.dataType(), (dataType, diffDataEntity) -> {
                throw new RecordAlreadyExistsException(String.format(RecordAlreadyExistsException.MESSAGE_TEMPLATE, id.toString()));
            });

            typeDiffDataEntityMap.put(diffContent.dataType(), dataEntity);
        } catch (RecordNotFoundException ex) {
            /**
             * The id was not found in the storage so create a new one.
             */
            final Map<DataType, DiffDataEntity> diffDataEntityMap = new HashMap<>();
            diffDataEntityMap.put(diffContent.dataType(), dataEntity);
            this.diffDataStorage.put(id, diffDataEntityMap);
        }
    }

    /**
     * Find diff data by id.
     *
     * @param diffDataId
     * @return Map<DataType, DiffDataEntity>
     * @throws RecordNotFoundException
     */
    @Override
    public Map<DataType, DiffDataEntity> findById(final UUID diffDataId) {
        this.diffDataStorage.computeIfAbsent(diffDataId, (uuid) -> {
            throw new RecordNotFoundException(String.format(RecordNotFoundException.MESSAGE_TEMPLATE, uuid.toString()));
        });
        return this.diffDataStorage.get(diffDataId);
    }
}
