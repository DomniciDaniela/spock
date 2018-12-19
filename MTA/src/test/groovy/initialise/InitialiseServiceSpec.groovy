package initialise

import database.DataBase
import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification
import utils.ApiKeys
import utils.TestDataUtils
import utils.Utils
import validation.TestValidation


class InitialiseServiceSpec extends Specification {

    HttpResponseDecorator response
    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.INITIALISE_ENDPOINT

    DataBase dataBase = new DataBase()
    TestValidation validation = new TestValidation()

    String EM_POLICY_NO = getDataBase().getActivePolicyBasedOnCenterCode(TestDataUtils.PolicyCenterCode.EM_CENTER_CODE)
    String EH_POLICY_NO = getDataBase().getActivePolicyBasedOnCenterCode(TestDataUtils.PolicyCenterCode.EH_CENTER_CODE)
    String SW_POLICY_NO = getDataBase().getActivePolicyBasedOnCenterCode(TestDataUtils.PolicyCenterCode.SW_CENTER_CODE)
    String FC_POLICY_NO = getDataBase().getActivePolicyBasedOnCenterCode(TestDataUtils.PolicyCenterCode.FC_CENTER_CODE)
    String EM_CANCELLED_POLICY_NO = getDataBase().getCancelledPolicy(TestDataUtils.PolicyCenterCode.EM_CENTER_CODE)
    String POLICY_NO_MULTIPLE_VERSION = getDataBase().getActivePolicyWithMultipleVersions(TestDataUtils.PolicyCenterCode.SW_CENTER_CODE)
    String POLICY_PAYMENT_TYPE_DD = getDataBase().getPolicyWithPaymentType(TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_DD)
    String POLICY_PAYMENT_TYPE_CARD = getDataBase().getPolicyWithPaymentType(TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_CARD)
    String POLICY_PAYMENT_TYPE_CPA = getDataBase().getPolicyWithPaymentType(TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_CPA)

    def "Initialise Service - EsureMotor policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: EM_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - SheilasWheels policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: SW_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
        assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - FirstAlternative policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: FC_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - EsureMotor policyNo and version - LATEST"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: EM_POLICY_NO,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - EsureHome policyNo"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: EH_POLICY_NO,
                    version: TestDataUtils.Version.LATEST
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
                    policyNo: EM_CANCELLED_POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
        Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with multiple versions"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_MULTIPLE_VERSION
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then: "Response code validation"
            assert response.status == 201
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject results = response.data.results[0].mtaQuote
            validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with invalid version"() {
        given: "Submit the following schema on the initialise endpoint"
            def payload = new JsonBuilder(
                    policyNo: EM_POLICY_NO,
                    version: TestDataUtils.Version.INVALID_SEQUENCE_NO
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

    def "Initialise Service - PolicyNo with Payment type DD"(){
        given: "Customer has a Policy with Payment type DD"
        def payload = new JsonBuilder(
                policyNo: POLICY_PAYMENT_TYPE_DD,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with Payment type CPA"(){
        given: "Customer has a Policy with Payment type CPA"
        def payload = new JsonBuilder(
                policyNo: POLICY_PAYMENT_TYPE_CPA,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should not contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        validation.initialise_responseBodyValidation(results)
    }

    def "Initialise Service - PolicyNo with Payment type CARD"(){
        given: "Customer has a Policy with Payment type CARD"
        def payload = new JsonBuilder(
                policyNo: POLICY_PAYMENT_TYPE_CARD,
                version: TestDataUtils.Version.LATEST
        ).toString()
        when: "Initialise method is called on the Request Object"
        Utils utils = new Utils()
        response = utils.createPOSTRequest(ENDPOINT, ApiKeys.getMTAApiKey(), payload)
        then:   "Response Code should be 201"
        assert response.status == 201
        "Response should not contain Current Payment Plan Details"
        assert response.data.apiVersion !=null
        JSONObject results = response.data.results[0].mtaQuote
        validation.initialise_responseBodyValidation(results)
    }
}
