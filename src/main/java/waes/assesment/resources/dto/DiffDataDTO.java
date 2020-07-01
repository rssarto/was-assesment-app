package waes.assesment.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import waes.assesment.resources.entities.DiffContent;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiffDataDTO implements DiffContent {

    private String binaryData;

    @Override
    public String base64Content() {
        return this.binaryData;
    }
}
