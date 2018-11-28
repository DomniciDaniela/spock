import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification

class OutstandingPOB extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    String apiKey = Utils.apiKey
    HttpResponseDecorator response

    // Customer can do an MTA (change of vehicle) successfully
    // when the business value for eligibility rule(for Outstanding POB) says allow
    // and TIA value is True for Outstanding POB
    def "Scenario 17"() {
        given: "User provided mta/check rule using the following schema where Policy is not in force is allow"
        def payload = new JsonBuilder(
                policyNo: "71997908",
                version: "134841483"
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

    // Customer can do an MTA (change of vehicle) successfully
    // when the business value for eligibility rule(for Outstanding POB) says allow
    // and TIA value is FALSE for Outstanding POB
    def "Scenario 6"() {
        given: "User provided mta/check rule using the following schema where Policy is not in force is allow"
        def payload = new JsonBuilder(
                policyNo: "65309936",
                version: "131092250"
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

    // Customer can do an MTA (change of vehicle) successfully when business value for eligibility rule
    // Policy is not in force - says do not allow and TIA value is FALSE for for Policy is not in force
    def "Scenario 7"() {
        given: "User provided mta/check rule using the following schema where Policy is not in force is allow"
        def payload = new JsonBuilder(
                policyNo: "65309936",
                version: "131092250"
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

    // Customer cannot do an MTA (change of vehicle) successfully when business value for eligibility rule
    // Policy is not in force - says do not allow
    // and TIA value is True for for Policy is not in force
    def "Scenario 8"() {
        given: "User provided mta/check rule using the following schema where Policy is not in force is not allow"
        def payload = new JsonBuilder(
                policyNo: "71998785",
                version: "134842001"
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

        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == false
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }
}
