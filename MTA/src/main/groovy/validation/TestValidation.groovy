package validation

import org.json.JSONArray
import utils.TestDataUtils


class TestValidation {

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
}
