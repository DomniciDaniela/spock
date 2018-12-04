import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification


class OutstandingBalanceOwedOnPolicySpec extends Specification {

    HttpResponseDecorator response
    def testValidation = new TestValidation()

    String POLICY_NO_TIA_TRUE = "11250591"
    String VERSION_NO_TIA_TRUE = "134342079"
    String POLICY_NO_TIA_FALSE = "65270337"
    String VERSION_NO_TIA_FALSE = "x"

    //Sql Query for outstanding balance owed on policy:
    // select policy_no,policy_seq_no,cover_start_date, payment_method from policy where newest = 'Y' and payment_method = 'DD';

    //Business rule for outstanding balance owed on policy is allow
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

    //Business rule for outstanding balance owed on policy is not allow
    def "NoOutstandingBalanceOwedOnPolicy - Business not Allow - TIA Value False"(){
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

    def "NoOutstandingBalanceOwedOnPolicy - Business not Allow - TIA Value true"(){
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
