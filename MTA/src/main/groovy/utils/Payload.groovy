package utils

import groovy.json.JsonOutput

trait Payload {
    String asJsonString() {
        JsonOutput.toJson(this)
    }

}