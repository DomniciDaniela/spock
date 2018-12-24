package database

import groovy.sql.Sql
import utils.TestDataUtils
import utils.Utils

class AuroraDataBase extends DataBase {

    static final String environment = Utils.getSystemEnvironment()

    String getJdbcUrl() {
        try {
            switch (environment) {
                case "deve13":
                    return  "jdbc:postgresql://" + TestDataUtils.AuroraDatabase.DB_HOSTNAME_DEV + ":" + TestDataUtils.AuroraDatabase.DB_PORT + "/" +  TestDataUtils.AuroraDatabase.DB_NAME_DEVE
                    break
                case "tste13":
                    return  "jdbc:postgresql://" + TestDataUtils.AuroraDatabase.DB_HOSTNAME_TEST + ":" + TestDataUtils.AuroraDatabase.DB_PORT + "/" +  TestDataUtils.AuroraDatabase.DB_NAME_TSTE
                    break
                default:
                    throw new Exception("Invalid environment" + environment)
                }
            } catch (Exception e) {
            return null
        }
    }

    @Override
    Sql setupDataBaseConnection() {
        switch (environment) {
            case "deve13":
                return Sql.newInstance(getJdbcUrl(), TestDataUtils.AuroraDatabase.DB_USERNAME_DEV,
                        TestDataUtils.AuroraDatabase.DB_PASSWORD_DEV, TestDataUtils.AuroraDatabase.DRIVER)
            case "tste13":
                return Sql.newInstance(getJdbcUrl(), TestDataUtils.AuroraDatabase.DB_USERNAME_TEST,
                        TestDataUtils.AuroraDatabase.DB_PASSWORD_TEST, TestDataUtils.AuroraDatabase.DRIVER)
            default:
                throw new Exception("Invalid environment" + environment)
        }
    }

    String getMtaData() {
        def row = getFirstResult("Select mta_data from motor_mta_quotes order by creation_date DESC limit 1")
        return(row.mta_data)
    }

}
