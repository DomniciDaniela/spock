package validation

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
        //assert errors.get(TestDataUtils.JSONObjects.MESSAGE) == "Text '"+jsonObject.get(TestDataUtils.JSONObjects.EFFECTIVE_DATE)+"' could not be parsed at index 4"
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

