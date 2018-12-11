package paymentTransaction

import groovyx.net.http.HttpResponseDecorator
import org.json.JSONArray
import org.json.JSONObject
import spock.lang.Specification
import utils.TestDataUtils
import utils.Utils

class PaymentTransactionServiceSpec extends Specification{

    String ENDPOINT = Utils.environment + TestDataUtils.Endpoint.PAYMENT_TRANSACTIONS_ENDPOINT + "23115431" +
            TestDataUtils.Endpoint.PAYMENT_TRANSACTIONS_ENDPOINT_PATH
    String apiKey = "4n30xjtqranhcc5ef5q0rs2569is8p9f"
    HttpResponseDecorator response

    def "Payment Transaction Service - EsureMotor policyNo"() {
        when: "Get payment detail"
            Utils utils = new Utils()
            response = utils.createGETRequest(ENDPOINT + "47498495", apiKey)
        then: "Response code validation"
            assert response.status == 200
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.results[0]

            assert responseBody.get("policyNo") != null
            assert responseBody.get("policySequenceNo") != null
            assert responseBody.get("coverStartDate") != null
            assert responseBody.get("coverEndDate") != null
            assert responseBody.get("coverEndTime") != null
            assert responseBody.get("totalPolicyAmount") != null
            assert responseBody.get("instalmentPlanTypeCode") != null
            assert responseBody.get("instalmentPlanTypeDescription") != null

            JSONObject ddDetails = responseBody.get("ddDetails")
            assert ddDetails != null
            assert ddDetails.get("currentInstalmentAmount") != null
            assert ddDetails.get("remainingNoOfInstalments") != null
            assert ddDetails.get("nextPaymentDate") != null
            String totalCollectedAmount = ddDetails.get("totalCollectedAmount").toString()
            int total = 0
            JSONArray currentPaymentPlan = ddDetails.getJSONArray("currentPaymentPlan")
                for (int i=0; i < currentPaymentPlan.length(); i++) {
                    assert currentPaymentPlan.getJSONObject(i).get("instalmentDate") != null
                    String instalmentCollected = currentPaymentPlan.getJSONObject(i).get("instalmentCollected")

                    String instalmentAmount = currentPaymentPlan.getJSONObject(i).get("instalmentAmount")
                    if(instalmentAmount != null && instalmentCollected == "true" ) {
                        total += currentPaymentPlan.getJSONObject(i).get("instalmentAmount").toInteger()
                    }
                }
            assert totalCollectedAmount == total.toString()
            assert ddDetails.get("bankAccountNo") != null
            assert ddDetails.get("bankSortCode") != null
            assert ddDetails.get("accountHolderName") != null
            assert ddDetails.get("suspensionCode") != null
    }

    def "Payment Transaction Service - invalid sequence number"() {
        when: "Get payment detail"
            Utils utils = new Utils()
            response = utils.createGETRequest(ENDPOINT + "474984952", apiKey)
        then: "Response code validation"
            assert response.status == 404
        then: "Response body validation"
            assert response.data.apiVersion != null
            JSONObject responseBody = response.data.errors[0]
            assert responseBody.get(TestDataUtils.JSONObjects.CODE) != null
            assert responseBody.get(TestDataUtils.JSONObjects.MESSAGE) != null
    }

    def "Payment Transaction Service - EsureHome policyNo"() {
        String endpoint = Utils.environment + TestDataUtils.Endpoint.PAYMENT_TRANSACTIONS_ENDPOINT + "33532559" +
                TestDataUtils.Endpoint.PAYMENT_TRANSACTIONS_ENDPOINT_PATH
        when: "Get payment detail"
        Utils utils = new Utils()
        response = utils.createGETRequest(endpoint + "62670781", apiKey)
        then: "Response code validation"
        assert response.status == 200
        then: "Response body validation"
        assert response.data.apiVersion != null
        JSONObject responseBody = response.data.results[0]

        assert responseBody.get("policyNo") != null
        assert responseBody.get("policySequenceNo") != null
        assert responseBody.get("coverStartDate") != null
        assert responseBody.get("coverEndDate") != null
        assert responseBody.get("coverEndTime") != null
        assert responseBody.get("totalPolicyAmount") != null
        assert responseBody.get("instalmentPlanTypeCode") != null
        assert responseBody.get("instalmentPlanTypeDescription") != null

        JSONObject cardDetails = responseBody.get("cardDetails")
        assert cardDetails != null
        assert cardDetails.get("lastDigits") != null
        assert cardDetails.get("cardType") != null
        assert cardDetails.get("expiryDate") != null
        assert cardDetails.get("pspReference") != null
        assert cardDetails.get("pspReference") != null
    }
}
