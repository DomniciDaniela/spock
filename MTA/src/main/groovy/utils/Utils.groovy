package utils

import groovy.util.logging.Log
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient


@Log
class Utils {

    public static String MTA_RULES_ENDPOINT = environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    public static String PRO_RATA_ENDPOINT = environment + TestDataUtils.Endpoint.PRO_RATA_ENDPOINT

    def createPOSTRequest(String endpoint, String apiKey, String body) {
        try {
            RESTClient restClient = new RESTClient(endpoint)
            log.info "\nCreate post request " + "\nEndpoint: " + endpoint + "\nAPI key: " + apiKey + "\nPayload: "+ body +"\n"
            restClient.post(
                    requestContentType: "application/json",
                    headers: ["apiKey": apiKey],
                    body: body
            )
        } catch (HttpResponseException e) {
            log.info "The response is\n " + e.response.data.toString()
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
