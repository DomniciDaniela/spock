import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import org.json.JSONObject
import spock.lang.Specification


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
            String results = response.data.results[0].toString()
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
            String results = response.data.results[0].toString()
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
            String results = response.data.results[0].toString()
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
            String results = response.data.results[0].toString()
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
            String results = response.data.results[0].toString()
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
            String results = response.data.results[0].toString()
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

    void responseBodyValidation(results) {
        assert results != null
        assert results.contains(TestDataUtils.JSONObjects.VEHICLE_MAKE) != null
        assert results.contains(TestDataUtils.JSONObjects.VEHICLE_MODEL) != null
        assert results.contains(TestDataUtils.JSONObjects.FUEL_TYPE_CODE) != null
        assert results.contains(TestDataUtils.JSONObjects.CAR_VALUE) != null
        assert results.contains(TestDataUtils.JSONObjects.SECURITY_DEVICE) != null
        assert results.contains(TestDataUtils.JSONObjects.TRACKER_YN) != null
        assert results.contains(TestDataUtils.JSONObjects.MODIFICATION) != null
        assert results.contains(TestDataUtils.JSONObjects.MILEAGE) != null
        assert results.contains(TestDataUtils.JSONObjects.MILEAGE_DESCRIPTION) != null
        assert results.contains(TestDataUtils.JSONObjects.CLASS_OF_USE) != null
        assert results.contains(TestDataUtils.JSONObjects.OVERNIGHT_LOCATION) != null
        assert results.contains(TestDataUtils.JSONObjects.CAR_OWNER) != null
        assert results.contains(TestDataUtils.JSONObjects.REGISTERED_KEEPER) != null
        assert results.contains(TestDataUtils.JSONObjects.QUOTE_ID) != null
        assert results.contains(TestDataUtils.JSONObjects.QUOTE_VERSION) != null
        assert results.contains(TestDataUtils.JSONObjects.QUOTE_EXPIRY_FLAG) != null
        assert results.contains(TestDataUtils.JSONObjects.QUOTE_EXPIRY_DATE) != null
    }

    void errorsBodyValidation(errors) {
        assert errors.get(TestDataUtils.JSONObjects.CODE) != null
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) != null
    }
}
