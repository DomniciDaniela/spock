package initialise

import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.TestDataUtils
import utils.Utils


class InitialiseServiceSpec extends Specification {

    HttpResponseDecorator response
    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.INITIALISE_ENDPOINT
    String apiKey = Utils.apiKey

    def "Initialise Service - EsureMotor policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - SheilasWheels policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.SW_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
        assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - FirstAlternative policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.FA_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - EsureMotor policyNo and version - LATEST"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - EsureHome policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO_HOME,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 404
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            errorsBodyValidation(errors)
    }

    def "Initialise Service - Invalid policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_INVALID,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 400
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            errorsBodyValidation(errors)
    }

    def "Initialise Service - Cancelled policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: "23115431",
                    version: "47498495"
            ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with multiple versions"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO_MULTIPLE_VERSION
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with invalid version"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.INVALID_SEQUENCE_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 404
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null

            JSONObject errors = response.data.errors[0]
            errorsBodyValidation(errors)
    }


    def "Initialise Service - PolicyNo with Payment type DD"(){
        given: "Customer has a Policy with Payment type DD"
        def payload = new JsonBuilder(
                policyNo: TestDataUtils.Policy.POLICY_PAYMENT_TYPE_DD,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with Payment type CPA"(){
        given: "Customer has a Policy with Payment type CPA"
        def payload = new JsonBuilder(
                policyNo: TestDataUtils.Policy.POLICY_PAYMENT_TYPE_CPA,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should not contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with Payment type CARD"(){
        given: "Customer has a Policy with Payment type CARD"
        def payload = new JsonBuilder(
                policyNo: TestDataUtils.Policy.POLICY_PAYMENT_TYPE_CARD,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should not contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        responseBodyValidation(results)
    }

    void responseBodyValidation(results) {
        assert results != null

        assert results.get(TestDataUtils.JSONObjects.QUOTE_ID) != null
        assert results.get(TestDataUtils.JSONObjects.QUOTE_VERSION) != null
        assert results.get(TestDataUtils.JSONObjects.QUOTE_EXPIRY_FLAG) != null
        assert results.get(TestDataUtils.JSONObjects.QUOTE_EXPIRY_TIME_STAMP) != null

        def coverLines = results.quoteDetails.coverLines

        for (int j = 0; j < (coverLines.length()); j++) {

            if (coverLines.get(j).(TestDataUtils.JSONObjects.PRODUCT_LINE_ID) == TestDataUtils.JSONValues.PRODUCT_LINE_ID) {

                def coveredVehicle = results.quoteDetails.coverLines.get(j).coveredVehicle

                for (def i = 0; i < coveredVehicle.length(); i++) {
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.VEHICLE_MAKE) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.VEHICLE_MODEL) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.FUEL_TYPE_CODE) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.CAR_VALUE) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.SECURITY_DEVICE) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.TRACKER_YN) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.MODIFICATION) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.MILEAGE) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.MILEAGE_DESCRIPTION) != null
                    assert coveredVehicle.get(TestDataUtils.JSONObjects.OVERNIGHT_LOCATION) != null
                }

                def coveredDriver = results.quoteDetails.coverLines.get(j).coveredDrivers.get(0)

                assert coveredDriver.get(TestDataUtils.JSONObjects.CLASS_OF_USE) != null
                assert coveredDriver.get(TestDataUtils.JSONObjects.CAR_OWNER) != null
                assert coveredDriver.get(TestDataUtils.JSONObjects.REGISTERED_KEEPER) != null
            }
        }
        def availabilityRules = results.quoteDetails.availabilityRules

        for (def i = 0; i < availabilityRules.length(); i++) {

            if (availabilityRules.get(i).get(TestDataUtils.JSONObjects.RULE_NAME) == TestDataUtils.JSONValues.AVAILABILITY_RULE_PAYMENT_METHOD_DD
                    && availabilityRules.get(i).get(TestDataUtils.JSONObjects.RULE_VALUE)  == TestDataUtils.JSONValues.AVAILABILITY_RULE_VALUE_Y) {

                def currentPaymentPlan = results.currentPaymentPlan.toString()

                assert currentPaymentPlan != "null"

                def installments = results.currentPaymentPlan.instalments

                for(def j=0; j<installments.length();j++){

                    assert installments.get(j).get(TestDataUtils.JSONObjects.INSTALMENT_COLLECTED) != null
                    assert installments.get(j).get(TestDataUtils.JSONObjects.INSTALMENT_AMOUNT) != null
                    assert installments.get(j).get(TestDataUtils.JSONObjects.INSTALMENT_DATE) != null
                }
                break
            }
            else if (availabilityRules.get(i).get(TestDataUtils.JSONObjects.RULE_NAME)  == TestDataUtils.JSONValues.AVAILABILITY_RULE_PAYMENT_METHOD_DD
                    && availabilityRules.get(i).get(TestDataUtils.JSONObjects.RULE_VALUE)  == TestDataUtils.JSONValues.AVAILABILITY_RULE_VALUE_N) {

                def currentPaymentPlan = results.currentPaymentPlan.toString()

                assert currentPaymentPlan == "null"
                break
            }
        }
    }

    void errorsBodyValidation(errors) {
        assert errors.get(TestDataUtils.JSONObjects.CODE) != null
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) != null
    }
}
