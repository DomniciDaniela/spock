import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification

class PolicyInRenewalCycle extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    String apiKey = Utils.apiKey
    HttpResponseDecorator response

    String POLICY_NO_TIA_TRUE = "31714184"
    String VERSION_NO_TIA_TRUE = "76626286"
    String POLICY_NO_TIA_FALSE = "65269440"
    String VERSION_NO_TIA_FALSE = "131886675"

    def "Policy in renewal cycle - allow - TIA - true"() {
        given: " ‌Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy is in the renewal cycle) says allow" +
                "and TIA value is True for Policy is in the renewal cycle"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
             assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy in renewal cycle - allow - TIA - false"() {
        given: " ‌Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy is in the renewal cycle) says allow" +
                "and TIA value is FALSE for Policy is in the renewal cycle"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_FALSE,
                    version: VERSION_NO_TIA_FALSE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy in renewal cycle - disallow - TIA - false"() {
        given: " ‌Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy is in the renewal cycle) says do not allow" +
                "and TIA value is FALSE for Policy is in the renewal cycle"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_FALSE,
                    version: VERSION_NO_TIA_FALSE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Policy in renewal cycle - disallow - TIA - true"() {
        given: " ‌Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy is in the renewal cycle) says do not allow" +
                "and TIA value is true for Policy is in the renewal cycle"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleNotAllowed(responseBody)
    }
}
