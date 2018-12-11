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
            case PolicyType.OUTSTANDING_BALANCED_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'EM' AND PAYMENT_METHOD ='DD' AND COVER_START_DATE >'"
                        + new Date().previousMonthDayDate() + "'")
            case PolicyType.OUTSTANDING_BALANCED_FALSE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'SW' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE >'"
                        + new Date().previousMonthDayDate() + "'")
            case PolicyType.IN_FORCE_LESS_THAN_A_DAY_TRUE:
                return getFirstResult("SELECT POLICY_NO, POLICY_SEQ_NO from POLICY WHERE EXPIRY_CODE = '1'" +
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'EM' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE ='"
                        + new Date().currentDate() + "'")
        }

    }
}
