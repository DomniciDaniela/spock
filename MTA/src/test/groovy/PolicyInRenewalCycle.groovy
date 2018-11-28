import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification

class PolicyInRenewalCycle extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    String apiKey = Utils.apiKey
    HttpResponseDecorator response


    // https://myesure.atlassian.net/browse/MTAQ-264
     // ‌Customer can do an MTA (change of vehicle) successfully when business value for eligibility rule
    //(Policy is in the renewal cycle) says allow
    // and TIA value is True for Policy is in the renewal cycle
    // outstandingBalanceOwedOnPolicy : N
    def "Scenario 1"() {
        given: "User provided mta/check rule using the following  motor policy which is in the renewal cycle"
            def payload = new JsonBuilder(
                    policyNo: "31714184",
                    version: "76626286"
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String body = JsonOutput.toJson(response.data.results[0].motorMtaEligibility)
            JSONObject responseBody = new JsonSlurper().parseText(body)

            assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
            assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
            assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
            assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
            assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }

    // ‌Customer can do an MTA (change of vehicle) successfully when business value for eligibility rule
    //(Policy is in the renewal cycle) says allow
    // and TIA value is false for Policy is in the renewal cycle
    // outstandingBalanceOwedOnPolicy : N
     def "Scenario 2"() {
        given: "User provided mta/check rule using the following  motor policy which is in the renewal cycle"
        def payload = new JsonBuilder(
                policyNo: "65269440",
                version: "131886675"
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
        assert response.status == 200
        then: "Response body validation"
        assert response.data.apiVersion != null
        String body = JsonOutput.toJson(response.data.results[0].motorMtaEligibility)
        JSONObject responseBody = new JsonSlurper().parseText(body)

        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }

    // https://myesure.atlassian.net/browse/MTAQ-264
    // ‌Customer can do an MTA (change of vehicle) successfully when business value for eligibility rule
    //Policy is in the renewal cycle says do not allow
    // and TIA value is True for Policy is in the renewal cycle
    // Query: select * from policy where expiry_code = 9 and newest = 'Y' and CENTER_CODE = 'EM';
    // outstandingBalanceOwedOnPolicy : N
    def "Scenario 3"() {
        given: "User provided mta/check rule using the following  motor policy which is in the renewal cycle"
        def payload = new JsonBuilder(
                policyNo: "65269440",
                version: "131886675"
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
        assert response.status == 200
        then: "Response body validation"
        assert response.data.apiVersion != null
        String body = JsonOutput.toJson(response.data.results[0].motorMtaEligibility)
        JSONObject responseBody = new JsonSlurper().parseText(body)

        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }


}
