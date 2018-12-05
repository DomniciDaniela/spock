class TestDataUtils {

    class Code {
        static final String MMTAE_002 = "MMTAE-002"
        static final String MMTAE_004 = "MMTAE-004"
        static final String SUCCESS = "SUCCESS"
    }

    class Description {
        static final String BAD_REQUEST = "Bad Request"
        static final String NOT_FOUND = "Oracle Gateway lookup failed - Not Found"
        static final String INVALID_CREDENTIALS = "Invalid authentication credentials"
        static final String INVALID_COVER_START_DATE = "Invalid coverStartDate. coverStartDate must be greater than yearStartDate"
        static final String INVALID_POLICY_TERM = "Invalid policy term. Please validate yearStartDate and coverEndDate"
    }

    class Endpoint {
        static final String INITIALISE_ENDPOINT = "api-jva-motor-mta/v1/quotes/initialise"
        static final String MTA_RULES_ENDPOINT = "api-jva-motor-mta-eligibility/v1/rules/motormtaeligibility/check"
        static final String PAYMENT_TRANSACTIONS_ENDPOINT = "api-jva-payment-transactions/v1/policies/"
        static final String PAYMENT_TRANSACTIONS_ENDPOINT_PATH = "/paymentTransactions/detail?policyVersionNumber="
        static final String PRO_RATA_ENDPOINT = "api-jva-prorata/v1/prorata/calculate"
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
         static final String COVER_LINES = "coverLines"
         static final String COVER_END_DATE = "coverEndDate"
         static final String COVER_START_DATE = "coverStartDate"
         static final String DESCRIPTION = "description"
         static final String FUEL_TYPE_CODE = "fuelTypeCode"
         static final String MESSAGE = "message"
         static final String MILEAGE = "mileage"
         static final String MILEAGE_DESCRIPTION = "mileageDescription"
         static final String MODIFICATION = "modification1"
         static final String OVERNIGHT_LOCATION = "overnightLocation"
         static final String PREMIUM_VALUE = "premiumValue"
         static final String PRORATABLE = "proratable"
         static final String PRO_RATED_PREMIUM_VALUE = "proRatedPremiumValue"
         static final String QUOTE_EXPIRY_DATE = "quoteExpiryDate"
         static final String QUOTE_EXPIRY_FLAG = "quoteExpiryFlag"
         static final String QUOTE_ID = "quoteId"
         static final String QUOTE_VERSION = "quoteVersion"
         static final String REGISTERED_KEEPER = "registeredKeeper"
         static final String SECURITY_DEVICE = "securityDevice"
         static final String TRACKER_YN = "trackerYn"
         static final String VEHICLE_MAKE = "vehicleMake"
         static final String YEAR_START_DATE = "yearStartDate"
    }

    class Policy {
        static final String POLICY_NO = "1000807"
        static final String POLICY_NO_MULTIPLE_VERSION = "26614630"
        static final String FA_POLICY_NO = "33524912"
        static final String POLICY_NO_HOME = "33532559"
        static final String SW_POLICY_NO = "33518703"
        static final String POLICY_INVALID = "invalid"
    }

    class Prorata {
        static final String YEAR_START_DATE = "2018-10-14T00:00:00Z"
        static final String COVER_END_DATE = "2019-10-14T00:00:00Z"
        static final String COVER_START_DATE = "2018-11-14T00:00:00Z"
    }

    class TransactionTypes{
        static final String[] ALL = ["all"]
        static final String[] ALL_COV = ["all", "changeOfVehicle"]
        static final String[] ALL_TYPES = ["changeOfVehicle", "addTempAddDriver", "addPermAddDriver", "changeOfRegistration", "addMotoringConviction"]
        static final String[] COV = ["changeOfVehicle"]
        static final String[] NONE = ["none"]
        static final String[] NONE_ALL = ["none", "all"]
        static final String[] NONE_COV = ["none", "changeOfVehicle "]
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

