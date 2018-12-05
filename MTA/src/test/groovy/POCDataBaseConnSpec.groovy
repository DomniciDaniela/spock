import spock.lang.Specification

class POCDataBaseConnSpec extends Specification {

    def "test connection"() {
        when: "Connect to DB"
            DataBase db = new DataBase()
            String query = "SELECT POLICY_NO, POLICY_SEQ_NO from POLICY" +
                    " where EXPIRY_CODE='9'AND PAYMENT_METHOD='CARD' AND NEWEST ='Y' and CENTER_CODE='EM'"
            String[] result = db.getFirstResult(query)
            String[] results = db.executeSqlQuery(query)

            String policyNo = result[0]
            String POLICY_SEQ_NO = result[1]
            println(policyNo)
            println(POLICY_SEQ_NO)

            println(results)
        then:
            println("Done")
    }
}
