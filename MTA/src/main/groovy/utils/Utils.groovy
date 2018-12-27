package utils

import groovy.util.logging.Log
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import org.json.JSONArray
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml

import java.util.stream.Collectors


@Log
class Utils {

    public static String MTA_RULES_ENDPOINT = environment + TestDataUtils.Endpoint.MTA_RULES_ENDPOINT
    public static String PRO_RATA_ENDPOINT = environment + TestDataUtils.Endpoint.PRO_RATA_ENDPOINT
    public static String ADMIN_FEE_ENDPOINT = environment + TestDataUtils.Endpoint.MOTOR_FEE_ENDPOINT

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
        try {
            switch (getSystemEnvironment()) {
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

    static String getSystemEnvironment() {
        return System.getProperty("branch")
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

    static String readMotorFeeYML(String effDate , String bCode , String channel){
        List<JSONObject> list= new ArrayList<JSONObject>()
        List<String> admin = new ArrayList<String>()
        Yaml yaml = new Yaml()
        InputStream document = new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/motorfee.yml"))
        Object data = yaml.load(document)
        JSONObject jsonObject = new JSONObject(data)
        JSONObject motorFee = jsonObject.get("motor-fees")
        JSONArray adminFee = motorFee.get("fees")
        (0..adminFee.length()-1).each{
            list.add(adminFee.getJSONObject(it))
        }
        admin = list.stream()
            .filter{f->f.get("effectiveDate")<=effDate && f.get("brandCode")==bCode && f.get("channel")==channel}
            .map{f->f.get("fee")}
            .collect(Collectors.toList())
        admin.get(admin.size()-1)

        }

}
