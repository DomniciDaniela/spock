package eligibilityRules.changeOfVehicle

import database.DataBase
import database.PolicyType
import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.Utils
import validation.TestValidation


class OutstandingBalanceOwedOnPolicySpec extends Specification {

    HttpResponseDecorator response
    def testValidation = new TestValidation()
    def dataBase = new DataBase()

    String POLICY_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.OUTSTANDING_BALANCED_TRUE)[0].substring(10)
    String VERSION_NO_TIA_TRUE = dataBase.getPolicyAndVersion(PolicyType.OUTSTANDING_BALANCED_TRUE)[1].substring(14)
    String POLICY_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[0].substring(10)
    String VERSION_NO_TIA_FALSE = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[1].substring(14)

    def "OutstandingBalanceOwedOnPolicy - Business Allow - TIA Value True"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : POLICY_NO_TIA_TRUE,
            version: VERSION_NO_TIA_TRUE
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT,Utils.apiKey,PAYLOAD)
        then:"Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "OutstandingBalanceOwedOnPolicy - Business Allow - TIA Value False"(){
        given: "Policy has an outstanding balance owed on the policy(false)"
        def PAYLOAD = new JsonBuilder(
            policyNo : POLICY_NO_TIA_FALSE,
            version: VERSION_NO_TIA_FALSE
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT,Utils.apiKey,PAYLOAD)
        then:"Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "OutstandingBalanceOwedOnPolicy - Business not Allow - TIA Value False"(){
        given: "Policy has an outstanding balance owed on the policy(false)"
        def PAYLOAD = new JsonBuilder(
            policyNo : POLICY_NO_TIA_FALSE,
            version: VERSION_NO_TIA_FALSE
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT,Utils.apiKey,PAYLOAD)
        then:"Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "OutstandingBalanceOwedOnPolicy - Business not Allow - TIA Value true"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : POLICY_NO_TIA_TRUE,
            version: VERSION_NO_TIA_TRUE
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT,Utils.apiKey,PAYLOAD)
        then:"Response should return 200 and COV value should return false in order to proof that customer cannot do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null
        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleNotAllowed(jsonResponse)
    }
}
