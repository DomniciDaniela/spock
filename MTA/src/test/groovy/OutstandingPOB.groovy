import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification

class OutstandingPOB extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    String apiKey = Utils.apiKey
    HttpResponseDecorator response
    
    String POLICY_NO_TIA_TRUE = "71997908"
    String VERSION_NO_TIA_TRUE = "134841483"
    String POLICY_NO_TIA_FALSE = "65309936"
    String VERSION_NO_TIA_FALSE = "131092250"

    def "OutstandingPOB - allow - TIA - true"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                " when the business value for eligibility rule(for Outstanding POB) says allow" +
                "and TIA value is True for Outstanding POB"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
            println(response.data)

        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "OutstandingPOB - allow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Outstanding POB) says allow" +
                "and TIA value is FALSE for Outstanding POB"
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

    def "OutstandingPOB - disallow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Outstanding POB) say do not allow" +
                "and TIA value is FALSE for Outstanding POB"
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

    def "OutstandingPOB - disallow - TIA - true"() {
        given: " ‌Customer cannot do an MTA (change of vehicle)" +
                " when business value for eligibility rule(for Outstanding POB) say do not allow" +
                " and TIA value is True Policy for Outstanding POB"
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
