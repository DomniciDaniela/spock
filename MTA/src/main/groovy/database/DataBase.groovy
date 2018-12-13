package database

import groovy.sql.Sql
import utils.Date

import java.sql.SQLException

class DataBase {

    String HOSTNAME = "jdbc:oracle:thin:" + environment + ".es-dte.co.uk:1521:TIA"
    String USERNAME = "tiasup"
    String PASSWORD = "tiasup"
    def DRIVER = "oracle.jdbc.driver.OracleDriver"

    String getEnvironment() {
        String environment = System.getProperty("branch")
        try {
            switch (environment) {
                case "deve13":
                    return "@e1a-esodb-deve-01"

                case "tste13":
                    return "@e1b-esodb-tste-01"

                default:
                    System.out.println("Invalid environment" + environment)
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }

    Sql setupDataBaseConnection() {
        Sql sql = null
        try {
            sql = Sql.newInstance(HOSTNAME, USERNAME, PASSWORD, DRIVER)
        } catch (ClassNotFoundException e) {
            e.printStackTrace()
        } catch (SQLException e) {
            e.printStackTrace()
        }
        return sql
    }

    def getFirstResult(String query) {
        Sql sql = setupDataBaseConnection()
        def row = sql.firstRow(query)
        return row
    }

    def executeSqlQuery(String query) {
        Sql sql = setupDataBaseConnection()
        def row = sql.rows(query)
        return row
    }

    String[] getPolicyAndVersion(query) {
        switch (query) {
            case PolicyType.NOT_IN_FORCE_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE >'" + new Date().currentDate() + "'")
                break
            case PolicyType.OUTSTANDING_BALANCED_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'EM' AND PAYMENT_METHOD ='DD' AND COVER_START_DATE >'"
                        + new Date().previousMonthDayDate() + "'")
                break
            case PolicyType.TIA_RETURNS_FALSE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'SW' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE >'"
                        + new Date().previousMonthDayDate() + "'")
                break
            case PolicyType.IN_FORCE_LESS_THAN_A_DAY_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'SW' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE ='"
                        + new Date().currentDate() + "'")
            case PolicyType.CUSTOMER_BARRED_ACC:
                return getFirstResult("SELECT PE.POLICY_NO, PE.CUST_NO,P.POLICY_SEQ_NO, " +
                    "BC.BAR_TYPE FROM POLICY_ENTITY PE INNER JOIN CIH.BARRED_CUSTOMERS BC ON " +
                    "PE.CUST_NO = BC.SOURCE_SYSTEM_CUST_ID INNER JOIN POLICY P ON P.POLICY_NO = PE.POLICY_NO " +
                    "where BC.SOURCE_SYSTEM_CUST_ID IN(SELECT SOURCE_SYSTEM_CUST_ID FROM CIH.BARRED_CUSTOMERS " +
                    "GROUP BY SOURCE_SYSTEM_CUST_ID HAVING COUNT (BAR_TYPE) =1)AND BC.BAR_TYPE = 'ACC' AND P.NEWEST = 'Y'")
            case PolicyType.CUSTOMER_BARRED_UW_VAL:
                return getFirstResult("SELECT PE.POLICY_NO, PE.CUST_NO,P.POLICY_SEQ_NO, " +
                    "BC.BAR_TYPE FROM POLICY_ENTITY PE INNER JOIN CIH.BARRED_CUSTOMERS BC ON " +
                    "PE.CUST_NO = BC.SOURCE_SYSTEM_CUST_ID INNER JOIN POLICY P ON P.POLICY_NO = PE.POLICY_NO " +
                    "where BC.SOURCE_SYSTEM_CUST_ID IN(SELECT SOURCE_SYSTEM_CUST_ID FROM CIH.BARRED_CUSTOMERS " +
                    "GROUP BY SOURCE_SYSTEM_CUST_ID HAVING COUNT (BAR_TYPE) =1)AND BC.BAR_TYPE = 'UW-VAL' AND P.NEWEST = 'Y'")
                break
            case PolicyType.CUSTOMER_BARRED_UW:
                return getFirstResult("SELECT PE.POLICY_NO, PE.CUST_NO,P.POLICY_SEQ_NO, " +
                    "BC.BAR_TYPE FROM POLICY_ENTITY PE INNER JOIN CIH.BARRED_CUSTOMERS BC ON " +
                    "PE.CUST_NO = BC.SOURCE_SYSTEM_CUST_ID INNER JOIN POLICY P ON P.POLICY_NO = PE.POLICY_NO " +
                    "where BC.SOURCE_SYSTEM_CUST_ID IN(SELECT SOURCE_SYSTEM_CUST_ID FROM CIH.BARRED_CUSTOMERS " +
                    "GROUP BY SOURCE_SYSTEM_CUST_ID HAVING COUNT (BAR_TYPE) =1)AND BC.BAR_TYPE = 'UW' AND P.NEWEST = 'Y'")
                break
            case PolicyType.RENEWAL_CYCLE_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '9' " +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'EM' AND PAYMENT_METHOD ='CARD' AND TRANSACTION_TYPE ='R'")
                break
            case PolicyType.TSV_POB:
                return getFirstResult("SELECT al.POLICY_NO, al.POLICY_SEQ_NO" +
                        " from AGREEMENT_LINE al " +
                                "inner join POLICY p" +
                                " on p.POLICY_NO = al.POLICY_NO " +
                                "and al.PRODUCT_LINE_ID='EMTSV' AND al.NEWEST='Y'and al.COVER_END_DATE>'"
                                + new Date().currentDate() + "' AND al.CANCEL_CODE=0 AND al.CENTER_CODE ='EM'")
                break
        }

    }
}
