import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification

class TemporarySubstitutionVehicleSpec extends Specification {

    Utils utils = new Utils()

    String POLICY_NO_TIA_TRUE = "47641920"
    String VERSION_NO_TIA_TRUE = "135015466"
    String POLICY_NO_TIA_FALSE = "65269440"
    String VERSION_NO_TIA_FALSE = "131886675"

    def "Temporary Substitution Vehicle - allow - TIA - true"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Temporary Substitution Vehicle in force) says allow" +
                "and TIA value is True for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - allow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(for Temporary Substitution Vehicle in force) says allow" +
                "and TIA value is False for Temporary Substitution Vehicle in force"
        def payload = new JsonBuilder(
                policyNo: POLICY_NO_TIA_FALSE,
                version: VERSION_NO_TIA_FALSE
        ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - disallow - TIA - false"() {
        given: "Customer can do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(Temporary Substitution Vehicle in force) say do not allow" +
                "and TIA value is False for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_FALSE,
                    version: VERSION_NO_TIA_FALSE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
             assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleAllowed(responseBody)
    }

    def "Temporary Substitution Vehicle - disallow - TIA - true"() {
        given: "Customer cannot do an MTA (change of vehicle) successfully" +
                "when the business value for eligibility rule(Temporary Substitution Vehicle in force) say do not allow" +
                "and TIA value is true for Temporary Substitution Vehicle in force"
            def payload = new JsonBuilder(
                    policyNo: POLICY_NO_TIA_TRUE,
                    version: VERSION_NO_TIA_TRUE
            ).toString()
        when: "POST schema on the /check endpoint"
            def response = utils.createPOSTRequest(utils.MTA_RULES_ENDPOINT, utils.apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0].motorMtaEligibility
            TestValidation validation = new TestValidation()
            validation.responseBodyValidation_changeOfVehicleNotAllowed(responseBody)
    }
}
