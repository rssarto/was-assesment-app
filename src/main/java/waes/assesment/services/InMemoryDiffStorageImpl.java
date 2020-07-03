package waes.assesment.services;

import org.springframework.stereotype.Service;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDiffStorageImpl implements DiffStorage {
    private final Map<UUID, Map<DataType, DiffDataEntity>> diffDataStorage;

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
            final Map<DataType, DiffDataEntity> typeDiffDataEntityMap = this.findById(id);
            throw new IllegalArgumentException(String.format("The id %s already exists, please use another id.", id.toString()));
        }catch (IllegalArgumentException ex){
            /**
             * The id was not found in the storage so create a new one.
             */
            final DiffDataEntity dataEntity = new DiffDataEntity(id, diffContent.base64Content(), diffContent.dataType());
            this.diffDataStorage.computeIfPresent(dataEntity.getId(), (uuid, dataTypeDiffDataEntityMap) -> {
                dataTypeDiffDataEntityMap.computeIfPresent(dataEntity.getDataType(), (dataType, diffDataEntity) -> {
                    throw new IllegalArgumentException(String.format("The id %s already exists, please use another id.", id.toString()));
                });
                dataTypeDiffDataEntityMap.put(dataEntity.getDataType(), dataEntity);
                return dataTypeDiffDataEntityMap;
            });
            this.diffDataStorage.computeIfAbsent(dataEntity.getId(), uuid -> {
                Map<DataType, DiffDataEntity> diffDataMap = new HashMap<>();
                diffDataMap.put(dataEntity.getDataType(), dataEntity);
                return diffDataMap;
            });
        }
    }

    /**
     *
     * @param diffDataId
     * @return
     */
    @Override
    public Map<DataType, DiffDataEntity> findById(final UUID diffDataId) {
        this.diffDataStorage.computeIfAbsent(diffDataId, (uuid) -> {
            throw new IllegalArgumentException(String.format("The id %s was not found", uuid.toString()));
        });
        return this.diffDataStorage.get(diffDataId);
    }
}
