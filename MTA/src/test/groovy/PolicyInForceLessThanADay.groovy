import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification

class PolicyInForceLessThanADay extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    String apiKey = Utils.apiKey
    HttpResponseDecorator response

    String POLICY_NO_TIA_TRUE = "71998785"
    String VERSION_NO_TIA_TRUE = "134842001"
    String POLICY_NO_TIA_FALSE = "35143314"
    String VERSION_NO_TIA_FALSE = "132064389"

    def "Policy InForce Less Than A Day - allow - TIA - true "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says allow" +
                "and TIA value is True for Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - allow - TIA - false "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says allow" +
                "and TIA value is False Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - disallow - TIA - false "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says do not allow" +
                "and TIA value is False Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - disallow - TIA - true "() {
        given: "Customer cannot do an MTA (change of vehicle" +
                "when business value for eligibility rule(Policy has been in force less than a day) says do not allow" +
                "and TIA value is TRUE Policy has been in force less than a day"
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
