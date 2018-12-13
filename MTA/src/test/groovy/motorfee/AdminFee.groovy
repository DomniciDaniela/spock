package motorfee

import spock.lang.Specification


import static motorfee.AdminFeeDefault.defaultAdminFeePayload

class AdminFee extends Specification{

    def "Success - All field are correct" (){
        given: "Request has all the correct field(policy number, channel, effective date, brand-code)"
        def PAYLOAD = defaultAdminFeePayload().build().asJsonString()
        println(PAYLOAD)
        when: "Request is send to the service"
        then: "Response should contain the response coe 200 and all valid values"

    }
}
