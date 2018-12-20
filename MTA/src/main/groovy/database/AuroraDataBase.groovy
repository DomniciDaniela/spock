package database

import groovy.sql.Sql

import java.sql.SQLException

class AuroraDataBase {

    String DB_HOSTNAME_DEV = "awie-rdspg-dev-motor-01.cjjj0qqeowok.eu-west-1.rds.amazonaws.com"
    String DB_USERNAME_DEV = "api_jva_motor_mta_deve"
    String DB_PASSWORD_DEV = "UQfGo34dwEK85dvf"
    String DB_NAME_DEVE = "deve"

    String DB_HOSTNAME_TEST = "awie-rdspg-test-motor-01.c9jas8kzsysw.eu-west-1.rds.amazonaws.com"
    String DB_USERNAME_TEST = "api_jva_motor_mta_tste"
    String DB_PASSWORD_TEST = "wKIpnLKNfdBGc47d"
    String DB_NAME_TSTE = "tste"

    String DB_PORT = "5432"
    String DRIVER = "org.postgresql.Driver"

    String getJdbcUrl() {
        try {
            String environment = System.getProperty("branch")
            switch (environment) {
                case "deve13":
                    return  "jdbc:postgresql://" + DB_HOSTNAME_DEV + ":" + DB_PORT + "/" +  DB_NAME_DEVE
                    break
                case "tste13":
                    return  "jdbc:postgresql://" + DB_HOSTNAME_TEST + ":" + DB_PORT + "/" +  DB_NAME_TSTE
                    break
                }
            } catch (Exception e) {
            return null
        }
    }

    Sql setupConnection() {
        Sql sql = null
        try {
            String environment = System.getProperty("branch")
            switch (environment) {
                case "deve13":
                    sql = Sql.newInstance(getJdbcUrl(), DB_USERNAME_DEV, DB_PASSWORD_DEV, DRIVER)
                    break
                case "tste13":
                    sql = Sql.newInstance(getJdbcUrl(), DB_USERNAME_TEST, DB_PASSWORD_TEST, DRIVER)
                    break
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace()
        } catch (SQLException e) {
            e.printStackTrace()
        }
        return sql
    }

    def getFirstResult(String query) {
        Sql sql = setupConnection()
        def row = sql.firstRow(query)
        return row
    }

    String getMtaData() {
        def row = getFirstResult("Select mta_data from motor_mta_quotes order by creation_date DESC limit 1")
        return(row.mta_data)
    }

}
