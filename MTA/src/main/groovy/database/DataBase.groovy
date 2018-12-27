package database

import groovy.sql.Sql

import java.sql.SQLException

class DataBase {

    static final String HOSTNAME
    static final String USERNAME
    static final String PASSWORD
    static final String DRIVER

    Sql setupDataBaseConnection( ) {
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
