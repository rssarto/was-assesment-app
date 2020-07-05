package waes.assesment.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import waes.assesment.resources.enums.DataType;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffResultDTO {
    /**
     * Map containing the data used in comparison.
     */
    @Getter
    private final Map<DataType, DiffDataDTO> diffMap;

    /**
     * Flag to define if the data pair is equal.
     */
    @Getter
    @Setter
    private boolean equal;

    /**
     * Present only when the pair content size does not match.
     * Map containing the data size by {@link DataType}
     */
    @Getter
    @Setter
    private Map<DataType, Integer> dataSizeMap;

    /**
     * Present only when the content is not equal
     * List with changes containing offset and length.
     */
    @Getter
    @Setter
    private List<ChangeLog> changeLogList;

    public DiffResultDTO(final Map<DataType, DiffDataDTO> diffMap) {
        this.diffMap = diffMap;
    }
}
