package waes.assesment.resources.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeLog {

    private int offset;
    private int length;

    public ChangeLog(final int offset, final int length){
        this.offset = offset;
        this.length = length;
    }
}
