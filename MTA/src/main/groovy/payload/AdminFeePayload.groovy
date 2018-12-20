package payload

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
    static AdminFeePayloadBuilder adminFeePayloadWithDateMissing(){
        return new AdminFeePayloadBuilder()
            .policyNo("12312")
            .channel("web")
            .brandCode("ESB")
    }
    static AdminFeePayloadBuilder adminFeePayloadWithPolicyNoMissing(){
        return new AdminFeePayloadBuilder()
            .effectiveDate("2018-01-04")
            .channel("web")
            .brandCode("ESB")
    }
    static AdminFeePayloadBuilder adminFeePayloadWithChannelMissing() {
        return new AdminFeePayloadBuilder()
            .policyNo("12312")
            .brandCode("ESB")
            .effectiveDate("2018-01-04")
    }
    static AdminFeePayloadBuilder adminFeePayloadWithBrandcodeMissing() {
        return new AdminFeePayloadBuilder()
            .policyNo("12312")
            .channel("web")
            .effectiveDate("2018-01-04")
    }
    static AdminFeePayloadBuilder getCurrentPayload(){
        return this
    }
}

