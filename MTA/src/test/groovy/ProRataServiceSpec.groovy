import groovy.json.JsonBuilder
import org.json.JSONObject
import spock.lang.Specification

class ProRataServiceSpec extends Specification {

    Utils utils = new Utils()
    def testValidation = new TestValidation()

    def "Pro - Rata service sends an empty request"() {
        given: "Define the payload data using the following schema"
        def payload = new JsonBuilder("":"").toString()

        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 400
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            testValidation.proRata_400ResponseValidation(errors)
    }

    def "Pro - Rata service sends an invalid product id"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{ productLineId "EMMOTTTT"
                                premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 400
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            testValidation.proRata_400ResponseValidation(errors)
    }

    def "Pro - Rata service -  A Customer makes changes without any add-ons"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{productLineId "EMMOT"
                                 premiumValue  382.91 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos != null
            JSONObject infos = response.data.infos[0]
            testValidation.proRata_infoValidation(infos)
            assert response.data.results != null

            JSONObject results = response.data.results[0]
            testValidation.proRata_resultsValidation_proratable(results)
    }

    def "Pro - Rata service -  A Customer makes changes with MLP add-on"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{productLineId "EMMOT"
                                 premiumValue  38291},
                             {productLineId "EMLGL"
                                 premiumValue  3100 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null

            assert response.data.infos != null
            JSONObject infos = response.data.infos[0]
            testValidation.proRata_infoValidation(infos)

            assert response.data.results != null
            JSONObject results = response.data.results[0]
            testValidation.proRata_resultsValidation_proratable(results)
    }

    def "Pro - Rata service -  A Customer makes without any add-ons and Admin fee is returned"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{ productLineId "EMADM"
                                 premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null

            assert response.data.infos != null
            JSONObject infos = response.data.infos[0]
            testValidation.proRata_infoValidation(infos)

            assert response.data.results != null
            JSONObject results = response.data.results[0]
            testValidation.proRata_resultsValidation_notProratable(results)
    }

    def "Pro - Rata service -  A Customer makes changes with FullCover add-on"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{ productLineId  "EMMOT"
                                 premiumValue  38291 },
                             { productLineId  "EMCHC"
                                 premiumValue  1000 },
                             {productLineId  "EMKYC"
                                 premiumValue  950 },
                             {productLineId  "EMPIB"
                                 premiumValue 1000 },
                             {productLineId  "EMMFC"
                                 premiumValue  950 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null

            assert response.data.infos != null
            JSONObject infos = response.data.infos[0]
            testValidation.proRata_infoValidation(infos)

            assert response.data.results != null
            JSONObject results = response.data.results[0]
            testValidation.proRata_resultsValidation_proratable(results)
    }

    def "Pro - Rata service - A Customer with a temporary driver changes a vehicle"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{ productLineId "EMTAD"
                                 premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null

            assert response.data.infos != null
            JSONObject infos = response.data.infos[0]
            testValidation.proRata_infoValidation(infos)

            assert response.data.results != null
            JSONObject results = response.data.results[0]
            testValidation.proRata_resultsValidation_notProratable(results)
    }

    def "Pro - Rata service sends an invalid apiKey"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
                premiumValue  4600
                coverLines ([{ productLineId "EMMOT"
                                 premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey + 1, payload)
        then: "Response code validation"
            assert response.status == 403
        then: "Response body validation"
            assert response.data.message == TestDataUtils.Description.INVALID_CREDENTIALS
    }

    def "Pro - Rata service sends an invalid date"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  TestDataUtils.Prorata.COVER_START_DATE + 1
                premiumValue  4600
                coverLines ([{ productLineId "EMMOT"
                                 premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 400
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            testValidation.proRata_400ResponseValidation(errors)
    }

    def "Pro - Rata service sends cover start date is in the past"() {
        given: "Define the payload data using the following schema"
            def builder = new JsonBuilder()
            builder.policy {
                yearStartDate TestDataUtils.Prorata.YEAR_START_DATE
                coverEndDate TestDataUtils.Prorata.COVER_END_DATE
                coverStartDate  "2016-11-14T00:00:00Z"
                premiumValue  4600
                coverLines ([{ productLineId "EMMOT"
                                 premiumValue  38291 }])
            }
            def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
            def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
            assert response.status == 400
        then: "Response body validation"
            assert response.data.apiVersion != null
            assert response.data.infos == null
            assert response.data.results == null
            JSONObject errors = response.data.errors[0]
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == TestDataUtils.Description.INVALID_COVER_START_DATE
            testValidation.proRata_400ResponseValidation(errors)
    }

    def "Pro - Rata service - policy duration is more than a year"() {
        given: "Define the payload data using the following schema"
        def builder = new JsonBuilder()
        builder.policy {
            yearStartDate "2017-10-14T00:00:00Z"
            coverEndDate TestDataUtils.Prorata.COVER_END_DATE
            coverStartDate  TestDataUtils.Prorata.COVER_START_DATE
            premiumValue  4600
            coverLines ([{ productLineId "EMMOT"
                             premiumValue  38291 }])
        }
        def payload = builder.toString()
        when: "POST schema on the /calculate endpoint"
        def response = utils.createPOSTRequest(utils.PRO_RATA_ENDPOINT, apiKey, payload)
        then: "Response code validation"
        assert response.status == 400
        then: "Response body validation"
        assert response.data.apiVersion != null
        assert response.data.infos == null
        assert response.data.results == null
        JSONObject errors = response.data.errors[0]
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == TestDataUtils.Description.INVALID_POLICY_TERM
        testValidation.proRata_400ResponseValidation(errors)
    }

    String getApiKey() {
        String environment = System.getProperty("branch")
        try {
            switch (environment) {
                case "deve13":
                    return "d02szyag01w6jypo6gpiq7z5vcydijbi"

                case "tste13":
                    return "2mkhrx2lyp2bdlhzy1j10f6eubgynul2"

                default:
                    System.out.println("Invalid key" + environment)
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }
}
