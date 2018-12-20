package utils

class ApiKeys {

    static String getMTAApiKey() {
        String environment = System.getProperty("branch")
        try {
            switch (environment) {
                case "deve13":
                    return "xcvoui5kn6kmhhs86npw3bytir4znxor"

                case "tste13":
                    return "qpkr79rwksuawcw5bqvhekvj8l3pml43"

                default:
                    System.out.println("Invalid key" + environment)
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }

    static String getProRataApiKey() {
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

    static String getMotorFeeApiKey() {
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
