package motorfee

import database.Payload
import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy

class AdminFeePayload implements Payload{
    String policyno, channel, brandcode, effectivedate
}

@Builder(builderStrategy = ExternalStrategy, forClass = AdminFeePayload)
class AdminFeePayloadBuilder {}

class AdminFeeDefault {
    static AdminFeePayloadBuilder defaultAdminFeePayload() {
        return new AdminFeePayloadBuilder()
            .policyno("12312")
            .channel("web")
            .brandcode("EM")
            .effectivedate("2018-01-04")
    }
}

