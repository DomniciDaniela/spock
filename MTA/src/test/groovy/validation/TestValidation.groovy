package validation

import database.AuroraDataBase
import org.json.JSONArray
import org.json.JSONObject
import utils.Payload
import utils.TestDataUtils

import java.util.stream.Collectors


class TestValidation implements Payload {
    JSONObject jsonObject
    List<JSONObject> list= new ArrayList<JSONObject>();

    void responseBodyValidation_changeOfVehicleAllowed(responseBody) {
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }

    void responseBodyValidation_changeOfVehicleNotAllowed(responseBody) {
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_VEHICLE_ALLOWED) == false
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_TEMP_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_PERM_DRIVER_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.CHANGE_OF_REGISTRATION_ALLOWED) == true
        assert responseBody.get(TestDataUtils.JSONObjects.ADD_MOTORING_CONVICTION_ALLOWED) == true
    }

    void proRata_400ResponseValidation(errors) {
        assert errors.get(TestDataUtils.JSONObjects.CODE) != null
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) != null
        assert errors.get(TestDataUtils.JSONObjects.DESCRIPTION) == TestDataUtils.Description.BAD_REQUEST
    }

    void proRata_infoValidation(info) {
        info.get(TestDataUtils.JSONObjects.CODE) == TestDataUtils.Code.SUCCESS
        info.get(TestDataUtils.JSONObjects.DESCRIPTION) == null
        info.get(TestDataUtils.JSONObjects.MESSAGE) == TestDataUtils.Code.SUCCESS
    }

    void proRata_resultsValidation_proratable(results) {
        results.get(TestDataUtils.JSONObjects.YEAR_START_DATE) != null
        results.get(TestDataUtils.JSONObjects.COVER_END_DATE) != null
        results.get(TestDataUtils.JSONObjects.COVER_START_DATE) != null
        results.get(TestDataUtils.JSONObjects.PREMIUM_VALUE) != null
        JSONArray coverLines = results.getJSONArray(TestDataUtils.JSONObjects.COVER_LINES)
        for (int i=0; i< coverLines.length(); i++) {
            assert coverLines.getJSONObject(i).get(TestDataUtils.JSONObjects.PRORATABLE) == true
            assert coverLines.getJSONObject(i).get(TestDataUtils.JSONObjects.PRO_RATED_PREMIUM_VALUE) != null
        }

    }
    void proRata_resultsValidation_notProratable(results) {
        results.get(TestDataUtils.JSONObjects.YEAR_START_DATE) != null
        results.get(TestDataUtils.JSONObjects.COVER_END_DATE) != null
        results.get(TestDataUtils.JSONObjects.COVER_START_DATE) != null
        results.get(TestDataUtils.JSONObjects.PREMIUM_VALUE) != null
        JSONArray coverLines = results.getJSONArray(TestDataUtils.JSONObjects.COVER_LINES)
        for (int i=0; i< coverLines.length(); i++) {
            assert coverLines.getJSONObject(i).get(TestDataUtils.JSONObjects.PRORATABLE) == false
            assert coverLines.getJSONObject(i).get(TestDataUtils.JSONObjects.PRO_RATED_PREMIUM_VALUE) != null
        }
    }

    void initialise_responseBodyValidation(results) {
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

    void initialise_mtaDataValidation(results) {
        String mtaData = results.getJSONObject(TestDataUtils.JSONObjects.MTA_DATA).toString()
        AuroraDataBase db = new AuroraDataBase()
        String mtaAurora = db.getMtaData()
        assert mtaData == mtaAurora.replaceAll("\\s","")
    }

    void errorsBodyValidation(errors) {
        assert errors.get(TestDataUtils.JSONObjects.CODE) != null
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) != null
    }

    void adminFeeSuccessInfoResponseValidation(JSONObject infos) {
        assert infos.get(TestDataUtils.JSONObjects.CODE) == 'OK'
        assert infos.get(TestDataUtils.JSONObjects.DESCRIPTION).equals(null)
        assert infos.get(TestDataUtils.JSONObjects.MESSAGE) == 'Data Retrieved'
    }

    void adminFeeSuccessResultsResponseValidation(JSONObject results , String fee) {
        assert results.get(TestDataUtils.JSONObjects.POLICY_NO) != null
        assert results.get(TestDataUtils.JSONObjects.POLICY_VERSION) != null
        assert results.get(TestDataUtils.JSONObjects.MTA_TRANSACTION_TYPE).equals(null)
        assert results.get(TestDataUtils.JSONObjects.MOTOR_FEE).toString() == fee

    }

    void adminFeeErrorsResponseValidation400(JSONObject errors ,String payload) {
        jsonObject = toJSONObject(payload)
        assert errors.get(TestDataUtils.JSONObjects.CODE) == "MOTOR-FEES-002"
        assert errors.get(TestDataUtils.JSONObjects.DESCRIPTION).equals(null)
        if (jsonObject.get(TestDataUtils.JSONObjects.EFFECTIVE_DATE) == "")
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "effectiveDate: must not be null"
        else if (jsonObject.get(TestDataUtils.JSONObjects.POLICY_NO) == "")
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "policyNo: must match \"^[0-9]{1,10}\$\""
        else if (jsonObject.isNull("brandCode"))
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "brandCode: must not be null"
        else if (jsonObject.isNull("policyNo"))
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "policyNo: must not be null"
        else if (jsonObject.isNull("effectiveDate"))
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "effectiveDate: must not be null"
        else if (jsonObject.isNull("channel"))
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "channel: must not be null"
        else if(jsonObject.get(TestDataUtils.JSONObjects.POLICY_NO) != null)
            assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "Text '"+jsonObject.get(TestDataUtils.JSONObjects.EFFECTIVE_DATE)+"' could not be parsed at index 4"
    }

    void adminFeeErrorsResponseValidation404(JSONObject errors ,String payload) {
        jsonObject = toJSONObject(payload)
        assert errors.get(TestDataUtils.JSONObjects.CODE) == "MOTOR-FEES-003"
        assert errors.get(TestDataUtils.JSONObjects.DESCRIPTION).equals(null)
        assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "No motor fees found for channel: "+jsonObject.get(TestDataUtils.JSONObjects.CHANNEL)+"," +
                                                                " brand: "+jsonObject.get(TestDataUtils.JSONObjects.BRAND_CODE)+", effective on date: "+jsonObject.get(TestDataUtils.JSONObjects.EFFECTIVE_DATE)
    }

    void  adminFeeEmptyErrorsResponseValidation400(JSONArray errors) {
        (0..errors.length()-1).each{
            list.add(errors.getJSONObject(it))
        }
        List<String> msgs = list.stream()
                                .map{li -> li.get(TestDataUtils.JSONObjects.MESSAGE)}
                                .collect(Collectors.toList())
        msgs.stream()
              .forEach{
                        it ->
                        assert it != null
                       }
            }
    }

