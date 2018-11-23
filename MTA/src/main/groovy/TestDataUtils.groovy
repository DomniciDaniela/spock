class TestDataUtils {

    class Code {
        static final String MMTAE_002 = "MMTAE-002"
        static final String MMTAE_004 = "MMTAE-004"
    }

    class Description {
        static final String BAD_REQUEST = "Bad Request"
        static final String NOT_FOUND = "Oracle Gateway lookup failed - Not Found"
    }

    class Endpoint {
        static final String INITIALISE_ENDPOINT = "api-jva-motor-mta/v1/quotes/initialise"
        static final String MTA_RULES_ENDPOINT = "api-jva-motor-mta-eligibility/v1/rules/motormtaeligibility/check"
        static final String MTA_MOCK_MOCK_ENDPOINT = "api-jva-motor-mta-eligibility-mock/v1/rules/motormtaeligibility/check"
    }

    class JSONObjects {
         static final String ADD_MOTORING_CONVICTION_ALLOWED = "addMotoringConvictionAllowed"
         static final String ADD_PERM_DRIVER_ALLOWED = "addPermAddDriverAllowed"
         static final String ADD_TEMP_DRIVER_ALLOWED = "addTempAddDriverAllowed"
         static final String CHANGE_OF_REGISTRATION_ALLOWED = "changeOfRegistrationAllowed"
         static final String CHANGE_OF_VEHICLE_ALLOWED = "changeOfVehicleAllowed"
         static final String CAR_OWNER = "carOwner"
         static final String CAR_VALUE = "carValue"
         static final String CLASS_OF_USE = "classOfUse"
         static final String CODE = "code"
         static final String DESCRIPTION = "description"
         static final String FUEL_TYPE_CODE = "fuelTypeCode"
         static final String MESSAGE = "message"
         static final String MILEAGE = "mileage"
         static final String MILEAGE_DESCRIPTION = "mileageDescription"
         static final String MODIFICATION = "modification1"
         static final String OVERNIGHT_LOCATION = "overnightLocation"
         static final String QUOTE_EXPIRY_DATE = "quoteExpiryDate"
         static final String QUOTE_EXPIRY_FLAG = "quoteExpiryFlag"
         static final String QUOTE_ID = "quoteId"
         static final String QUOTE_VERSION = "quoteVersion"
         static final String REGISTERED_KEEPER = "registeredKeeper"
         static final String SECURITY_DEVICE = "securityDevice"
         static final String TRACKER_YN = "trackerYn"
         static final String VEHICLE_MAKE = "vehicleMake"
         static final String VEHICLE_MODEL = "vehicleModel"
    }

    class Policy {
        static final String POLICY_NO = "1000807"
        static final String POLICY_NO_MULTIPLE_VERSION = "26614630"
        static final String FA_POLICY_NO = "33524912"
        static final String POLICY_NO_HOME = "33532559"
        static final String SW_POLICY_NO = "33518703"
        static final String POLICY_INVALID = "invalid"
    }

    class TransactionTypes{
        static final String[] ALL = ["all"]
        static final String[] ALL_COV = ["all", "cov"]
        static final String[] ALL_TYPES = ["cov", "atd", "apad", "COR", "aoc"]
        static final String[] COV = ["cov"]
        static final String[] NONE = ["none"]
        static final String[] NONE_ALL = ["none", "all"]
        static final String[] NONE_COV = ["none", "cov"]
        static final String STRING = "string"
        static final String[] TESTING = ["testing"]
    }

    class Version {
        static final IN_FORCE = "IN-FORCE"
        static final LATEST = "LATEST"
        static final String INVALID_SEQUENCE_NO = "1111"
        static final String SEQUENCE_NO = "122136567"
    }
}

