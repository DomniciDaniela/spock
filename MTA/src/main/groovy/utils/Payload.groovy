package utils
import net.minidev.json.parser.JSONParser

import groovy.json.JsonOutput
import org.json.JSONObject

trait Payload {

    String asJsonString() {
        JsonOutput.toJson(this)
    }

    JSONObject toJSONObject(String text) {
        JSONParser parser = new JSONParser()
        (JSONObject) parser.parse(text)
    }

}