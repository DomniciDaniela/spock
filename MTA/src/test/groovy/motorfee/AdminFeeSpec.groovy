package motorfee

import groovyx.net.http.HttpResponseDecorator
import org.json.JSONArray
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import spock.lang.Specification
import utils.ApiKeys
import utils.Utils
import validation.TestValidation

import java.util.stream.Collectors

import static payload.AdminFeeDefault.*

class AdminFeeSpec extends Specification {

    def PAYLOAD
    HttpResponseDecorator responseDecorator
    TestValidation testValidation = new TestValidation()
    JSONObject responseDataInfos, responseDataResults, responseDataErrors
    JSONArray responseEmptyErrors
    def apikey = ApiKeys.getMotorFeeApiKey()

    def "Success - All field are correct for admin fee"() {
        given: "Request has all the correct field(policy number, channel, effective date, brand-code)"
        PAYLOAD = defaultAdminFeePayload().build().asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataInfos = responseDecorator.getData().infos[0]
        responseDataResults = responseDecorator.getData().results[0]

        then: "Response should contain the response code 200 and all valid values"
        assert responseDecorator.status == 200
        assert responseDecorator.data.apiVersion != null
        testValidation.adminFeeSuccessInfoResponseValidation(responseDataInfos)
        testValidation.adminFeeSuccessResultsResponseValidation(responseDataResults)
    }

    def "Date_Validation - If date is invalid when requesting for admin fee"() {
        given: "Request has all the correct field but date is invalid"
        PAYLOAD = defaultAdminFeePayload()
            .effectiveDate("2019")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)
    }

    def "Brandcode_Validation - If brandcode is invalid when requesting for admin fee"() {
        given: "Request has all the correct field but brand code is invalid"
        PAYLOAD = defaultAdminFeePayload()
            .brandCode("SBB")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 404"
        assert responseDecorator.status == 404
        testValidation.adminFeeErrorsResponseValidation404(responseDataErrors, PAYLOAD)
    }

    def "Channel_Validation - If channel is invalid when requesting for admin fee"() {
        given: "Request has all the correct field but channel is invalid"
        def PAYLOAD = defaultAdminFeePayload()
            .channel("we")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 404"
        assert responseDecorator.status == 404
        testValidation.adminFeeErrorsResponseValidation404(responseDataErrors, PAYLOAD)

    }

    def "Date_Validation - If date is an empty string when requesting for admin fee"() {
        given: "Request has all the correct field but effective date is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .effectiveDate("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "Policy no_Validation - If policy no is an empty string when requesting for admin fee"() {
        given: "Request has all the correct field but policy no is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .policyNo("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "Brandcode_Validation - If brand code is an empty string when requesting for admin fee"() {
        given: "Request has all the correct field but brand code is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .brandCode("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 404"
        assert responseDecorator.status == 404
        testValidation.adminFeeErrorsResponseValidation404(responseDataErrors, PAYLOAD)

    }

    def "Channel_Validation - If channel is an empty string when requesting for admin fee"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .channel("")
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 404"
        assert responseDecorator.status == 404
        testValidation.adminFeeErrorsResponseValidation404(responseDataErrors, PAYLOAD)

    }

    def "Date_Validation - If date is missing when requesting for admin fee"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithDateMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "PolicyNo_Validation - If policyNo is missing when requesting for admin fee"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithPolicyNoMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "Channel_Validation - If channel is missing when requesting for admin fee"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithChannelMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "Brandcode_Validation - If brand code is missing when requesting for admin fee"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = adminFeePayloadWithBrandcodeMissing()
            .build()
            .asJsonString()

        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataErrors = responseDecorator.getData().errors[0]

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeErrorsResponseValidation400(responseDataErrors, PAYLOAD)

    }

    def "Empty payload"() {
        given: "Request with empty payload"
        def PAYLOAD = "{}"
        when: "Request is send to the service"
        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseEmptyErrors = responseDecorator.getData().errors

        then: "Response should contain the response code 400"
        assert responseDecorator.status == 400
        testValidation.adminFeeEmptyErrorsResponseValidation400(responseEmptyErrors)
    }

    def "Service return the correct admin fees as per the effective date"() {
        given: "Request has all the correct field but channel is an empty string"
        def PAYLOAD = defaultAdminFeePayload()
            .build()
            .asJsonString()

        when: "Request is send to the service"


        responseDecorator = new Utils().createPOSTRequest(Utils.ADMIN_FEE_ENDPOINT, apikey, PAYLOAD)
        responseDataInfos = responseDecorator.getData().infos[0]
        responseDataResults = responseDecorator.getData().results[0]

        then: "Response should contain the response code 200 and all valid values"
        assert responseDecorator.status == 200
        assert responseDecorator.data.apiVersion != null
        testValidation.adminFeeSuccessInfoResponseValidation(responseDataInfos)
        testValidation.adminFeeSuccessResultsResponseValidation(responseDataResults)
    }

    def "Read yaml file"(){

        given: ""
        List<JSONObject> list= new ArrayList<JSONObject>()
        List<String> admin = new ArrayList<String>()
        Yaml yaml = new Yaml()
        InputStream document = new FileInputStream(new File("C:\\Repos\\api-spock-framework\\MTA\\src\\main\\resources\\motorfee.yml"))
        Object data = yaml.load(document)
        JSONObject jsonObject = new JSONObject(data)
        JSONObject motorFee = jsonObject.get("motor-fees")
        JSONArray adminFee = motorFee.get("fees")
        (0..adminFee.length()-1).each{
            list.add(adminFee.getJSONObject(it))
        }
        admin = list.stream()
                                .filter{f->f.get("effectiveDate")<="2018-01-05" && f.get("brandCode")=='ESB' && f.get("channel")=='web'}
                                .map{f->f.get("fee")}
                                .collect(Collectors.toList())

        println(admin.get(admin.size()-1))


        when: ""
        println(admin.get(admin.size()-1))

        then:""


    }
}
