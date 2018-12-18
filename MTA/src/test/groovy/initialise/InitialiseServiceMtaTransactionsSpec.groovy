package initialise

import database.DataBase
import database.PolicyType
import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONArray
import org.json.JSONObject
import spock.lang.Specification
import utils.ApiKeys
import utils.TestDataUtils
import utils.Utils


class InitialiseServiceMtaTransactionsSpec extends Specification {

    HttpResponseDecorator response
    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.INITIALISE_ENDPOINT
    DataBase dataBase = new DataBase()

    String POLICY_NO = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[0].substring(10)
    String POLICY_VERSION = dataBase.getPolicyAndVersion(PolicyType.TIA_RETURNS_FALSE)[1].substring(14)

    def "Initialise Service - no mtaTransaction supplied"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            JSONObject mtaDta = results.getJSONObject(TestDataUtils.JSONObjects.MTA_DATA)
            // TODO update this
            JSONArray codes = mtaDta.get(TestDataUtils.JSONObjects.CODES)

            responseBodyValidation(results)
    }

    def "Initialise Service - changeOfVehicle supplied"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO,
                    version: POLICY_VERSION,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
        assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - all transaction types supplied"() {
        given: "Submit the following schema on the initialise endpoint"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO,
                version: POLICY_VERSION,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
        ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - invalid transaction type"() {
        given: "Submit the following schema on the initialise endpoint"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code is 400"
        assert response.status == 400
        then: "Errors validation"
        assert response.data.apiVersion != null
        JSONObject body = response.data.errors[0]
        errorsBodyValidation(body)
    }

    def "Initialise Service - valid % invalid transaction type"() {
        given: "Submit the following schema on the initialise endpoint"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_COV
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code is 400"
        assert response.status == 400
        then: "Errors validation"
        assert response.data.apiVersion != null
        JSONObject body = response.data.errors[0]
        errorsBodyValidation(body)
    }

    def "Initialise Service - mta transaction type - string"() {
        given: "Submit the following schema on the initialise endpoint"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.STRING
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code is 400"
        assert response.status == 400
        then: "Errors validation"
        assert response.data.apiVersion != null
        JSONObject body = response.data.errors[0]
        errorsBodyValidation(body)
    }

    def "Initialise Service - home policy"() {
        given: "Submit the following schema on the initialise endpoint"
        def payload = new JsonBuilder(
                policyNo: TestDataUtils.Policy.POLICY_NO_HOME,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
        ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code is 404"
        assert response.status == 404
        then: "Errors validation"
        assert response.data.apiVersion != null
        JSONObject body = response.data.errors[0]
        errorsBodyValidation(body)
    }

    def "Initialise Service - EsureMotor policyNo and version - LATEST"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            responseBodyValidation(results)
    }

    def "Initialise Service - Invalid policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_INVALID,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
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
                    version: "47498495",
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
            ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
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
                    policyNo: TestDataUtils.Policy.POLICY_NO_MULTIPLE_VERSION,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
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
                    version: TestDataUtils.Version.INVALID_SEQUENCE_NO,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 404
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null

            JSONObject errors = response.data.errors[0]
            errorsBodyValidation(errors)
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
