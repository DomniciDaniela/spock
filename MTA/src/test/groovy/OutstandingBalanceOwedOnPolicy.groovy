import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification


class OutstandingBalanceOwedOnPolicy extends Specification {

    def ENDPOINT = Utils.getEnvironment() + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    def APIKEY = Utils.apiKey
    HttpResponseDecorator response

    //Business rule for outstanding balance owed on policy is allow
    def "Scenario 13_CustomerCanDoMTAWhenBusinessRuleSaysAllowAndTIAIsReturningTrueForOutstandingBalanceOwedOnPolicy"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : "71997908",
            version: "134841483"
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(ENDPOINT,APIKEY,PAYLOAD)
        then:"Response should return 200 and correct MTA transaction value"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true

    }

    def "Scenario 14_CustomerCanDoMTAWhenBusinessRuleSaysAllowAndTIAIsReturningFalseForOutstandingBalanceOwedOnPolicy"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : "71998785",
            version: "134842001"
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(ENDPOINT,APIKEY,PAYLOAD)
        then:"Response should return 200 and correct MTA transaction value"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true

    }

    //Business rule for outstanding balance owed on policy is not allow
    def "Scenario 15_CustomerCanDoMTAWhenBusinessRuleSaysNotAllowAndTIAIsReturningFalseForOutstandingBalanceOwedOnPolicy"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : "71998785",
            version: "134842001"
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(ENDPOINT,APIKEY,PAYLOAD)
        then:"Response should return 200 and correct MTA transaction value"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true

    }

    def "Scenario 16_CustomerCanNotDoMTAWhenBusinessRuleSaysNotAllowAndTIAIsReturningTrueForOutstandingBalanceOwedOnPolicy"(){
        given: "Policy has an outstanding balance owed on the policy(true)"
        def PAYLOAD = new JsonBuilder(
            policyNo : "71997908",
            version: "134841483"
        ).toString()
        when:"Request is sent to the service"
        response = new Utils().createPOSTRequest(ENDPOINT,APIKEY,PAYLOAD)
        then:"Response should return 200 and correct MTA transaction value"
        assert response.status == 200
        assert response.data.apiVersion != null

        JSONObject jsonResponse = response.data.results[0].motorMtaEligibility
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == false
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert jsonResponse.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true

    }

}
