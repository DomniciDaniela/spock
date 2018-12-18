package database

import java.sql.Connection
import java.sql.DriverManager
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

    Connection getAuroraRemoteConnection() {
        Connection connection = null
        String jdbcUrl = null

        try {
            String environment = System.getProperty("branch")
            Class.forName("org.postgresql.Driver")
            switch (environment) {
                case "deve13":
                    jdbcUrl = "jdbc:postgresql://" + DB_HOSTNAME_DEV + "" + DB_PORT + "/" + DB_NAME_DEVE
                                        + "?user=" + DB_USERNAME_DEV + "&password=" + DB_PASSWORD_DEV
                    break
                case "tste13":
                    jdbcUrl = "jdbc:postgresql://" + DB_HOSTNAME_TEST + "" + DB_PORT + "/" + DB_NAME_TSTE
                    + "?user=" + DB_USERNAME_TEST + "&password=" + DB_PASSWORD_TEST

                    break
            }

            System.out.println("Getting remote connection with connection string from environment variables.")
            connection = DriverManager.getConnection(jdbcUrl)

        } catch (ClassNotFoundException e) {
            e.printStackTrace()
            System.exit(1)
        } catch (SQLException e) {
            e.printStackTrace()
            System.exit(2)
        }
        return connection
    }

}
