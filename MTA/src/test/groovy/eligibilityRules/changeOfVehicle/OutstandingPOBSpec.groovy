package eligibilityRules.changeOfVehicle

import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification
import utils.TestDataUtils
import utils.Utils
import validation.TestValidation

class OutstandingPOBSpec extends Specification {

    Utils utils = new Utils()
    TestValidation validation = new TestValidation()

    String POLICY_NO_TIA_TRUE = "72081303"
    String VERSION_NO_TIA_TRUE = "135015447"
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
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
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
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
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
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
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
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return FALSE in order to proof that customer cannot do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            validation.responseBodyValidation_changeOfVehicleNotAllowed(responseBody)
    }
}
