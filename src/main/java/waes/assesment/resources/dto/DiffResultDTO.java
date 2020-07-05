package waes.assesment.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import waes.assesment.resources.enums.DataType;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiffResultDTO {
    /**
     * Map containing the data used in comparison.
     */
    private Map<DataType, DiffDataDTO> diffMap;

    /**
     * Flag to define if the data pair is equal.
     */
    private boolean equal;

    /**
     * Present only when the pair content size does not match.
     * Map containing the data size by {@link DataType}
     */
    private Map<DataType, Integer> dataSizeMap;

    /**
     * Present only when the content is not equal
     * List with changes containing offset and length.
     */
    private List<ChangeLog> changeLogList;

    public DiffResultDTO(final Map<DataType, DiffDataDTO> diffMap) {
        this.diffMap = diffMap;
    }
}
