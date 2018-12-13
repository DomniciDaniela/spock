package eligibilityRules.changeOfVehicle

import database.DataBase
import database.PolicyType
import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.Utils
import validation.TestValidation

class CustomerIsBarredSpec extends Specification {

    HttpResponseDecorator response
    def testValidation = new TestValidation()
    def dataBase = new DataBase()

    String POLICY_NO_TIA_ACC_TRUE = dataBase.getPolicyAndVersion(PolicyType.CUSTOMER_BARRED_ACC)[0].substring(10)
    String VERSION_NO_TIA_ACC_TRUE = dataBase.getPolicyAndVersion(PolicyType.CUSTOMER_BARRED_ACC)[2].substring(14)
    String POLICY_NO_TIA_ACC_FALSE = "72080883"
    String VERSION_NO_TIA_ACC_FALSE = "135015350"

     String POLICY_NO_TIA_UW_VAL_TRUE = dataBase.getPolicyAndVersion(PolicyType.CUSTOMER_BARRED_UW_VAL)[0].substring(10)
     String VERSION_NO_TIA_UW_VAL_TRUE = dataBase.getPolicyAndVersion(PolicyType.CUSTOMER_BARRED_UW_VAL)[2].substring(14)
     String POLICY_NO_TIA_UW_VAL_FALSE = "72080883"
     String VERSION_NO_TIA_UW_VAL_FALSE = "135015350"

    String POLICY_NO_TIA_UW_TRUE = "1000815"
    String VERSION_NO_TIA_UW_TRUE = "135015443"
    String POLICY_NO_TIA_UW_FALSE = "72080883"
    String VERSION_NO_TIA_UW_FALSE = "135015350"

    def "Customer barred (Accounts) - Business Allow - TIA Value True"() {
        given: "Customer is barred (Accounts for the given policy) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_ACC_TRUE,
            version: VERSION_NO_TIA_ACC_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred (Accounts) - Business Allow - TIA Value False"() {
        given: "Customer is barred (Accounts for the given policy) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_ACC_FALSE,
            version: VERSION_NO_TIA_ACC_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred (Accounts) - Business not Allow - TIA Value False"() {
        given: "Customer is barred (Accounts for the given policy) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_ACC_FALSE,
            version: VERSION_NO_TIA_ACC_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred (Accounts) - Business not Allow - TIA Value True"() {
        given: "Customer is barred (Accounts for the given policy) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_ACC_TRUE,
            version: VERSION_NO_TIA_ACC_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleNotAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW-VAL) - Business Allow - TIA Value True"() {
        given: "Customer is barred fraud (UW-VAL) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_VAL_TRUE,
            version: VERSION_NO_TIA_UW_VAL_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW-VAL) - Business Allow - TIA Value False"() {
        given: "Customer is barred fraud (UW-VAL) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_VAL_FALSE,
            version: VERSION_NO_TIA_UW_VAL_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW-VAL) - Business not Allow - TIA Value False"() {
        given: "Customer is barred fraud (UW-VAL) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_VAL_FALSE,
            version: VERSION_NO_TIA_UW_VAL_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW-VAL) - Business not Allow - TIA Value True"() {
        given: "Customer is barred fraud (UW-VAL) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_VAL_TRUE,
            version: VERSION_NO_TIA_UW_VAL_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleNotAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW) - Business Allow - TIA Value True"() {
        given: "Customer is barred fraud (UW) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_TRUE,
            version: VERSION_NO_TIA_UW_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW) - Business Allow - TIA Value False"() {
        given: "Customer is barred fraud (UW) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_FALSE,
            version: VERSION_NO_TIA_UW_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW) - Business not Allow - TIA Value False"() {
        given: "Customer is barred fraud (UW) is false"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_FALSE,
            version: VERSION_NO_TIA_UW_FALSE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleAllowed(jsonResponse)
    }

    def "Customer barred fraud (UW) - Business not Allow - TIA Value True"() {
        given: "Customer is barred fraud (UW) is true"
        def PAYLOAD = new JsonBuilder(
            policyNo: POLICY_NO_TIA_UW_TRUE,
            version: VERSION_NO_TIA_UW_TRUE
        ).toString()
        when: "Request is sent to the service"
        response = new Utils().createPOSTRequest(Utils.MTA_RULES_ENDPOINT, Utils.apiKey, PAYLOAD)

        then: "Response should return 200 and COV value should return true in order to proof that customer can do MTA"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        testValidation.responseBodyValidation_changeOfVehicleNotAllowed(jsonResponse)
    }
}
