package configModel;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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