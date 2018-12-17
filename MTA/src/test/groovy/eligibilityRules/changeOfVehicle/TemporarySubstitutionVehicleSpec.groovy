package eligibilityRules.changeOfVehicle

import database.DataBase
import database.PolicyType
import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification
import utils.ApiKeys
import utils.Utils
import validation.TestValidation

class TemporarySubstitutionVehicleSpec extends Specification {

    Utils utils = new Utils()
    TestValidation validation = new TestValidation()
    def dataBase = new DataBase()

    String POLICY_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.TSV_POB)[0].substring(10)
    String VERSION_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.TSV_POB)[1].substring(14)
    String POLICY_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[0].substring(10)
    String VERSION_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[1].substring(14)

    def "Temporary Substitution Vehicle - allow - TIA - true"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Temporary Substitution Vehicle in force) says allow" +
                "and TIA value is True for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - allow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Temporary Substitution Vehicle in force) says allow" +
                "and TIA value is False for Temporary Substitution Vehicle in force"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO_TIA_FALSE,
                version: VERSION_NO_TIA_FALSE
        ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - disallow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(Temporary Substitution Vehicle in force) say do not allow" +
                "and TIA value is False for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_FALSE,
                    version: VERSION_NO_TIA_FALSE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return TRUE in order to proof that customer can do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - disallow - TIA - true"() {
        given: "Customer cannot do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(Temporary Substitution Vehicle in force) say do not allow" +
                "and TIA value is true for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "The response code should be 200"
            assert response.status == 200
        then: "COV value should return FALSE in order to proof that customer cannot do MTA"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            validation.responseBodyValidation_changeOfVehicleNotAllowed(responseBody)
    }
}
