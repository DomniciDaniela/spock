import groovy.json.JsonBuilder
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Ignore
import spock.lang.Specification


//@Ignore
class EligibilityRulesMockServiceSpec extends Specification {

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.MTA_MOCK_MOCK_ENDPOINT
    String apiKey = Utils.mockedApiKey
    HttpResponseDecorator response

    def "EligibilityRules - all requested values"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
            ).toPrettyString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - policy number only - integer"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO.toInteger()
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - policy number only"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - version missing"() {
        given: "User provided motormtaeligibility/check rule using following schema "
        def payload = new JsonBuilder(
                policyNo: TestDataUtils.Policy.POLICY_NO,
                mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL
        ).toString()

        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - transaction missing"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.LATEST
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - transaction all"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: "aaa",
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction none"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: "aaa",
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.NONE
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction all - cov"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction none - cov"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.NONE_COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction none - all"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version: TestDataUtils.Version.IN_FORCE,
                    mtaTransactionTypes:TestDataUtils.TransactionTypes.NONE_ALL
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - empty request"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder("":"").toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - policy number missing"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - policy number invalid"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_INVALID,
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction invalid"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.TESTING
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - transaction - string"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.STRING
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - policy number - empty string"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: "",
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - policy number - more 10 digits"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO + "234356789878",
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 400"
            assert response.status == 400
        then: "Errors validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }

    def "EligibilityRules - policy number - old endpoint "() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            def oldEndpoint = "https://ops-kong-tste.escloud.co.uk/api-jva-motormta-eligibility/v1/rules/motormtaeligibility/check"
            response = utils.createPOSTRequest(oldEndpoint, apiKey, payload)
        then: "Response code is 503"
            assert response.status == 503
    }

    def "EligibilityRules - version - LATEST"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO,
                    version:  TestDataUtils.Version.LATEST,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 200"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - version - valid sequence number"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: TestDataUtils.Policy.POLICY_NO_MULTIPLE_VERSION,
                    version:  TestDataUtils.Version.SEQUENCE_NO,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 200"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
            mtaBodyValidation(motorMtaEligibility)
    }

    def "EligibilityRules - version - invalid sequence number"() {
        given: "User provided motormtaeligibility/check rule using following schema "
            def payload = new JsonBuilder(
                    policyNo: "222",
                    version:  TestDataUtils.Version.INVALID_SEQUENCE_NO,
                    mtaTransactionTypes: TestDataUtils.TransactionTypes.COV
            ).toString()
        when: "POST schema on the /check endpoint"
            Utils utils = new Utils()
            response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
        then: "Response code is 404"
             assert response.status == 404
        then: "Response body validation"
            assert response.data.apiVersion != null
            String errors = response.data.errors[0].toString()
            errorsValidation(errors)
    }
    
    void mtaBodyValidation(motorMtaEligibility) {
        assert motorMtaEligibility != null
        assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) != null
        assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) != null
        assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) != null
        assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) != null
        assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) != null
    }
    
    void errorsValidation(errors) {
        assert errors.contains(TestDataUtils.JSONObjects.CODE) != null
        assert errors.contains(TestDataUtils.JSONObjects.DESCRIPTION) != null
        assert errors.contains(TestDataUtils.JSONObjects.MESSAGE) != null
    }
}
