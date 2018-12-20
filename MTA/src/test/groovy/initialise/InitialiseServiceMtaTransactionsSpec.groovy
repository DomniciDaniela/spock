package initialise

import database.OracleDataBase
import database.PolicyType
import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.ApiKeys
import utils.TestDataUtils
import utils.Utils
import validation.TestValidation


class InitialiseServiceMtaTransactionsSpec extends Specification {

    HttpResponseDecorator response
    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.INITIALISE_ENDPOINT
    OracleDataBase dataBase = new OracleDataBase()
    TestValidation validation = new TestValidation()

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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
        JSONObject errors = response.data.errors[0]
        validation.errorsBodyValidation(errors)
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
        JSONObject errors = response.data.errors[0]
        validation.errorsBodyValidation(errors)
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
        JSONObject errors = response.data.errors[0]
        validation.errorsBodyValidation(errors)
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
        JSONObject errors = response.data.errors[0]
        validation.errorsBodyValidation(errors)
    }

    def "Initialise Service - EsureMotor policyNo and version - LATEST"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO,
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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
            validation.errorsBodyValidation(errors)
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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
            validation.initialise_mtaDataValidation(results)
            validation.initialise_responseBodyValidation(results)
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
            validation.errorsBodyValidation(errors)
    }
}
