package waes.assesment.resources.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import waes.assesment.resources.enums.DataType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "diff_data")
@Entity
public class DiffDataEntity {

    @Id
    private UUID id;
    private String base64Data;
    private DataType dataType;

}
