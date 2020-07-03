package waes.assesment.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import waes.assesment.resources.entities.DiffContent;
import waes.assesment.resources.enums.DataType;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiffDataDTO implements DiffContent {

    @NotBlank(message = "The attribute binaryData is mandatory.")
    private String binaryData;

    private DataType dataType;

    @Override
    public String base64Content() {
        return this.binaryData;
    }

    @Override
    public DataType dataType() {
        return this.dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiffDataDTO that = (DiffDataDTO) o;
        return binaryData.equals(that.binaryData) &&
                dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(binaryData, dataType);
    }
}
