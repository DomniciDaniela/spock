package eligibilityRules.changeOfVehicle

import database.DataBase
import database.PolicyType
import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification
import utils.Utils
import validation.TestValidation

class PolicyInForceLessThanADaySpec extends Specification {

    Utils utils = new Utils()
    TestValidation validation = new TestValidation()
    def dataBase = new DataBase()

    String POLICY_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.IN_FORCE_LESS_THAN_A_DAY_TRUE)[0].substring(10)
    String VERSION_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.IN_FORCE_LESS_THAN_A_DAY_TRUE)[1].substring(14)
    String POLICY_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[0].substring(10)
    String VERSION_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[1].substring(14)

    def "Policy InForce Less Than A Day - allow - TIA - true "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says allow" +
                "and TIA value is True for Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - allow - TIA - false "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says allow" +
                "and TIA value is False Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - disallow - TIA - false "() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when business value for eligibility rule(Policy has been in force less than a day) says do not allow" +
                "and TIA value is False Policy has been in force less than a day"
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

    def "Policy InForce Less Than A Day - disallow - TIA - true "() {
        given: "Customer cannot do an MTA (change of vehicle" +
                "when business value for eligibility rule(Policy has been in force less than a day) says do not allow" +
                "and TIA value is TRUE Policy has been in force less than a day"
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
