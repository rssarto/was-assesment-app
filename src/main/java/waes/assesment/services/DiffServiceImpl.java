package waes.assesment.services;

import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import waes.assesment.exceptions.DiffDataNotExistsException;
import waes.assesment.resources.dto.ChangeLog;
import waes.assesment.resources.dto.DiffDataDTO;
import waes.assesment.resources.dto.DiffResultDTO;
import waes.assesment.resources.entities.DiffDataEntity;
import waes.assesment.resources.enums.DataType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public Map<DataType, DiffDataEntity> findByd(UUID diffDataId) {
        return this.diffStorage.findById(diffDataId);
    }

    @Override
    public DiffResultDTO compare(UUID diffDataId) {
        final Map<DataType, DiffDataEntity> diffDataEntityMap = this.findByd(diffDataId);
        final DiffDataEntity leftData = diffDataEntityMap.get(DataType.LEFT);
        Objects.requireNonNullElseGet(leftData, () -> {
            throw new DiffDataNotExistsException(DataType.LEFT);
        });

        final DiffDataEntity rightData = diffDataEntityMap.get(DataType.RIGHT);
        Objects.requireNonNullElseGet(rightData, () -> {
            throw new DiffDataNotExistsException(DataType.RIGHT);
        });

        final Map<DataType, DiffDataDTO> diffDataDTOMap = diffDataEntityMap.keySet()
                .stream()
                .map(dataType -> new DiffDataDTO(diffDataEntityMap.get(dataType).getBase64Data(), dataType))
                .collect(Collectors.toMap(DiffDataDTO::getDataType, Function.identity()));

        final DiffResultDTO diffResultDTO = new DiffResultDTO(diffDataDTOMap);
        diffResultDTO.setEqual(false);

        final DiffDataDTO leftDiffData = diffDataDTOMap.get(DataType.LEFT);
        final DiffDataDTO rightDiffData = diffDataDTOMap.get(DataType.RIGHT);

        /**
         * Not equal size data.
         */
        if (leftDiffData.getData().length() != rightDiffData.getData().length()) {
            final Map<DataType, Integer> diffSizeMap = new HashMap<>();
            diffSizeMap.put(DataType.LEFT, leftDiffData.getData().length());
            diffSizeMap.put(DataType.RIGHT, rightDiffData.getData().length());
            diffResultDTO.setDataSizeMap(diffSizeMap);
            return diffResultDTO;
        }

        /**
         * Not equal data
         */
        if (!leftDiffData.getData().equals(rightDiffData.getData())) {
            final List<ChangeLog> changeLogList = new ArrayList<>();
            final char[] leftDataChars = leftDiffData.getData().toCharArray();
            final char[] rightDataChars = rightDiffData.getData().toCharArray();
            ChangeLog changeLog = null;
            for (int index = 0; index < leftDataChars.length; index++) {
                char leftDataChar = leftDataChars[index];
                char rightDataChar = rightDataChars[index];

                if (leftDataChar != rightDataChar) {
                    if (Objects.isNull(changeLog)) {
                        changeLog = new ChangeLog(index, 1);
                    } else {
                        changeLog.setLength(changeLog.getLength() + 1);
                    }
                } else {
                    if (Objects.nonNull(changeLog)) {
                        changeLogList.add(changeLog);
                        changeLog = null;
                    }
                }
            }
            diffResultDTO.setChangeLogList(changeLogList);
            return diffResultDTO;
        }

        diffResultDTO.setEqual(true);
        return diffResultDTO;
    }
}
