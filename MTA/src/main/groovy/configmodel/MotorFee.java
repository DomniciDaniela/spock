package configmodel;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MotorFee implements Serializable {

    private static final long serialVersionUID = -1598767937969468118L;

    private LocalDate effectiveDate;
    private Integer fee;
    private String channel;
    private String brandCode;
}
