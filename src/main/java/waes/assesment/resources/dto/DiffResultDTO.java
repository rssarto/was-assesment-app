package waes.assesment.resources.dto;

import lombok.Getter;
import lombok.Setter;
import waes.assesment.resources.enums.DataType;

import java.util.List;
import java.util.Map;

public class DiffResultDTO {
    @Getter
    private final Map<DataType, DiffDataDTO> diffMap;

    @Getter
    @Setter
    private boolean equal;

    @Getter
    @Setter
    private Map<DataType, Integer> dataSizeMap;

    @Getter
    @Setter
    private List<ChangeLog> changeLogList;

    public DiffResultDTO(Map<DataType, DiffDataDTO> diffMap){
        this.diffMap = diffMap;
    }
}
