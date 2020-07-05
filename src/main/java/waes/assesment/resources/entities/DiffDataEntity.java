package waes.assesment.resources.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import waes.assesment.resources.enums.DataType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(DiffDataEntity.DiffDataEntityId.class)
@Table(name = "diff_data")
@Entity
public class DiffDataEntity implements Serializable {

    @Id
    @Column(name = "diff_id")
    private UUID diffId;

    @Column(name = "data", length = 999999, nullable = false)
    private String base64Data;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    public static class DiffDataEntityId implements Serializable {
        UUID diffId;
        DataType dataType;
    }

}
