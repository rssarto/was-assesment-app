package waes.assesment.resources.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Table(name = "diff_data")
@Entity
public class DiffDataEntity {

    @Id
    private UUID id;
    private String base64Data;

}
