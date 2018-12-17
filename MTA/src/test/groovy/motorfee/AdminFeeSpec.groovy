package motorfee

import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.Utils
import validation.TestValidation

import static motorfee.AdminFeeDefault.adminFeePayloadWithBrandcodeMissing
import static motorfee.AdminFeeDefault.adminFeePayloadWithChannelMissing
import static motorfee.AdminFeeDefault.adminFeePayloadWithDateMissing
import static motorfee.AdminFeeDefault.adminFeePayloadWithPolicyNoMissing
import static motorfee.AdminFeeDefault.defaultAdminFeePayload

class AdminFeeSpec extends Specification{
    HttpResponseDecorator responseDecorator
    TestValidation testValidation = new TestValidation()
    def apikey = "d02szyag01w6jypo6gpiq7z5vcydijbi"

    def "Success - All field are correct for admin fee" (){
        given: "Request has all the correct field(policy number, channel, effective date, brand-code)"
        def PAYLOAD = defaultAdminFeePayload().build().asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)
        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 200
        assert responseDecorator.data.apiVersion!=null

        JSONObject responseDataInfos = responseDecorator.getData().infos[0]
        testValidation.adminFeeSuccessInfoResponseValidation(responseDataInfos)

        JSONObject responseDataResults = responseDecorator.getData().results[0]
        testValidation.adminFeeSuccessResultsResponseValidation(responseDataResults)
    }

    def "Date_Validation - If date is invalid when requesting for admin fee" (){
        given: "Request has all the correct field but date is invalid"
        def PAYLOAD = defaultAdminFeePayload()
                        .effectiveDate("2019")
                        .build()
                        .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }

    def "Brandcode_Validation - If brandcode is invalid when requesting for admin fee" (){
        given: "Request has all the correct field but brand code is invalid"
        def PAYLOAD = defaultAdminFeePayload()
            .brandCode("SBB")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }

    def "Channel_Validation - If channel is invalid when requesting for admin fee" (){
        given: "Request has all the correct field but channel is invalid"
        def PAYLOAD = defaultAdminFeePayload()
            .channel("we")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }

    def "Date_Validation - If date is an empty string when requesting for admin fee" (){
        given: "Request has all the correct field but effective date is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .effectiveDate("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
    def "Policy no_Validation - If policy no is an empty string when requesting for admin fee" (){
        given: "Request has all the correct field but policy no is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .policyNo("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }

    def "Brandcode_Validation - If brand code is an empty string when requesting for admin fee" (){
        given: "Request has all the correct field but brand code is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .brandCode("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
    def "Channel_Validation - If channel is an empty string when requesting for admin fee" (){
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .channel("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }

    def "date_Validation - If date is missing when requesting for admin fee" (){
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithDateMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
    def "policyNo_Validation - If policyNo is missing when requesting for admin fee" (){
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithPolicyNoMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
    def "channel_Validation - If channel is missing when requesting for admin fee" (){
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithChannelMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
    def "brandcode_Validation - If brand code is missing when requesting for admin fee" (){
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithBrandcodeMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT,apikey,PAYLOAD)

        then: "Response should contain the response coe 200 and all valid values"
        assert responseDecorator.status == 400
    }
}
