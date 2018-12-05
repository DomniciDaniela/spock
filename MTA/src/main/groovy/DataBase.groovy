import groovy.sql.Sql

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
}
