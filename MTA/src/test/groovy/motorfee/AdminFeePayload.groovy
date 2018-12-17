package motorfee

import utils.Payload
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

class AdminFeePayload implements Payload{
    String policyNo, channel, brandCode, effectiveDate
}

@Builder(builderStrategy = ExternalStrategy, forClass = AdminFeePayload)
class AdminFeePayloadBuilder {}

class AdminFeeDefault {
    static AdminFeePayloadBuilder defaultAdminFeePayload() {
        return new AdminFeePayloadBuilder()
            .policyNo("12312")
            .channel("web")
            .brandCode("ESB")
            .effectiveDate("2018-01-04")
    }
}

