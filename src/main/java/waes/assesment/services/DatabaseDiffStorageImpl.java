package waes.assesment.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import waes.assesment.exceptions.RecordAlreadyExistsException;
import waes.assesment.exceptions.RecordNotFoundException;
import waes.assesment.repositories.DiffDataRepository;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Primary
@Service
public class DatabaseDiffStorageImpl implements DiffStorage {

    private final DiffDataRepository diffDataRepository;

    public DatabaseDiffStorageImpl(DiffDataRepository diffDataRepository) {
        this.diffDataRepository = diffDataRepository;
    }

    @Override
    public void create(final UUID id, DiffContent diffContent) {
        final DiffDataEntity dataEntity = new DiffDataEntity(id, diffContent.base64Content(), diffContent.dataType());
        try {
            /**
             * Check if id already exists in the storage
             */
            final Map<DataType, DiffDataEntity> typeDiffDataEntityMap = this.findById(id);
            typeDiffDataEntityMap.computeIfPresent(diffContent.dataType(), (dataType, diffDataEntity) -> {
                throw new RecordAlreadyExistsException(String.format(RecordAlreadyExistsException.MESSAGE_TEMPLATE, id.toString()));
            });

            this.diffDataRepository.save(dataEntity);
        } catch (RecordNotFoundException ex) {
            /**
             * The id was not found in the storage so create a new one.
             */
            this.diffDataRepository.save(dataEntity);
        }
    }

    @Override
    public Map<DataType, DiffDataEntity> findById(UUID diffDataId) {
        final Optional<List<DiffDataEntity>> optionalDiffDataEntities = this.diffDataRepository.findByDiffId(diffDataId);
        List<DiffDataEntity> diffDataEntities = optionalDiffDataEntities.orElseThrow(() -> {
            throw new RecordNotFoundException(String.format(RecordNotFoundException.MESSAGE_TEMPLATE, diffDataId));
        });
        return diffDataEntities.stream().collect(Collectors.toMap(DiffDataEntity::getDataType, Function.identity()));
    }
}
