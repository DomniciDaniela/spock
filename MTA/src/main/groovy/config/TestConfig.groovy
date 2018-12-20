package config

import configModel.MotorFee
import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated


@ConfigurationProperties(prefix = "motor-fees")
@Validated
@Component
@RefreshScope
@Data
 class TestConfig {
    private List<MotorFee> adminFeeList
}

