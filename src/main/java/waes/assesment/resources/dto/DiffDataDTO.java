package waes.assesment.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import waes.assesment.resources.entities.DiffContent;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiffDataDTO implements DiffContent {

    @NotBlank(message = "The attribute binaryData is mandatory.")
    private String binaryData;

    @Override
    public String base64Content() {
        return this.binaryData;
    }
}
