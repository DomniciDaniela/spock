# api-spock-framework

Tools used:
- Groovy -> programming language
- Spock -> testing framework
- Maven -> project management and building tool

# Spock with Groovy + Maven (advantages/disadvantages)
A - easy to integrate to Jenkins;
A - the manual team can switch from SOAP-UI scripts to Spock + Groovy scripts 
A - supports parallel execution
A - supports multi-module approach
A - supports BDD style

# Spock test snapshot (BDD style):
```
def "EligibilityRules Mock - all requested values"() {
    given: "User provided a motormtaeligibility/check rule using following schema"
    def payload = new JsonBuilder(
            policyNo: TestDataUtils.Policy.POLICY_NO,
            version: TestDataUtils.Version.IN_FORCE,
            mtaTransactionTypes: TestDataUtils.TransactionTypes.ALL_TYPES
    ).toString()
    when: "POST schema on the /check endpoint"
    Utils utils = new Utils()
    response = utils.createPOSTRequest(ENDPOINT, apiKey, payload)
    then: "Response code validation"
    assert response.status == 200
    then: "Response body validation"
    assert response.data.apiVersion != null
    String motorMtaEligibility = response.data.results[0].motorMtaEligibility.toString()
    assert motorMtaEligibility != null
    assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) != null
    assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) != null
    assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) != null
    assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) != null
    assert motorMtaEligibility.contains(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) != null
}
```
Structure & builders: 

TestDataUtils -> Groovy class used to define constants 

Utils -> Groovy class which contains helper methods to avoid code duplication

JSONBuilder -> Groovy builder for creating JSON payloads.

HTTPBuilder -> Groovy builder for creating REST requests.

The framework supports to run the scripts on different environments (dev/test)

# How to run the maven tests from command line: 
- mvn clean test -Dbranch=deve13 (runs the tests on dev environment)
- mvn clean test (runs the tests on the default profile - test environment)
