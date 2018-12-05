import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification

class PolicyNotInForceSpec extends Specification {

    Utils utils = new Utils()

    String POLICY_NO_TIA_TRUE = "72081303"
    String VERSION_NO_TIA_TRUE = "135015447"
    String POLICY_NO_TIA_FALSE = "65309936"
    String VERSION_NO_TIA_FALSE = "131092250"

    // Select * from policy where COVER_START_DATE >'03-DEC-2018' AND PAYMENT_METHOD='CARD' AND CENTER_CODE='EM';
    def "Policy Not InForce - allow - TIA - true"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(for Policy is not in force) says allow" +
                "and TIA value is True for for Policy is not in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy Not InForce - allow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(for Policy is not in force) says allow" +
                "and TIA value is FALSE for for Policy is not in force"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO_TIA_FALSE,
                version: VERSION_NO_TIA_FALSE
        ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy Not InForce - disallow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(for Policy is not in force) say do not allow" +
                "and TIA value is FALSE for for Policy is not in force"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO_TIA_FALSE,
                version: VERSION_NO_TIA_FALSE
        ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy Not InForce - disallow - TIA - true"() {
        given: "Customer cannot do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(for Policy is not in force) say do not allow" +
                "and TIA value is TRUE for for Policy is not in force"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO_TIA_TRUE,
                version: VERSION_NO_TIA_TRUE
        ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleNotAllowed(responseBody)
    }
}
