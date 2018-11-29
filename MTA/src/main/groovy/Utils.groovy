import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

class Utils {

    def createPOSTRequest(String endpoint, String apiKey, String body) {
        try {
            RESTClient restClient = new RESTClient(endpoint)
            restClient.post(
                    requestContentType: "application/json",
                    headers: ["apiKey": apiKey],
                    body: body
            )
        } catch (HttpResponseException e) {
            e.response
        }
    }

     static String getEnvironment() {
        String environment = System.getProperty("branch")
        try {
            switch (environment) {
                case "deve13":
                    return "https://ops-kong-deve13.escloud.co.uk/"

                case "tste13":
                    return "https://ops-kong-tste13.escloud.co.uk/"

                default:
                    System.out.println("Invalid environment" + environment)
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }

     static String getApiKey() {
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
    
     static String getMockedApiKey() {
        String environment = System.getProperty("branch")
        try {
            switch (environment) {
                case "deve13":
                    return "1er3ink809m7fmzjc7m2ub3t62sm5c6n"

                case "tste13":
                    return "u6s08jm6m88deh2uy5ohv59drl5aen55"

                default:
                    System.out.println("Invalid key" + environment)
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }

    def createGETRequest(String endpoint, String apiKey) {
        try {
            RESTClient restClient = new RESTClient(endpoint)
            restClient.get(
                    headers: ["apiKey": apiKey]
            )
        } catch (HttpResponseException e) {
            e.response
        }
    }


}
