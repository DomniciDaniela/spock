package database

import groovy.sql.Sql
import utils.Date
import utils.TestDataUtils
import utils.Utils

class OracleDataBase extends DataBase {

    static String getEnvironment() {
        try {
            switch (Utils.getSystemEnvironment()) {
                case "deve13":
                    return "@e1a-esodb-deve-01"

                case "tste13":
                    return "@e1b-esodb-tste-01"

                default:
                    throw new Exception ("Invalid environment" + Utils.getSystemEnvironment())
            }
            return environment
        } catch (Exception e) {
            return null
        }
    }

    @Override
    Sql setupDataBaseConnection() {
        return  Sql.newInstance(TestDataUtils.OracleDatabase.HOSTNAME, TestDataUtils.OracleDatabase.USERNAME,
                    TestDataUtils.OracleDatabase.PASSWORD, TestDataUtils.OracleDatabase.DRIVER)
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
                        "AND NEWEST = 'Y' AND CENTER_CODE = 'EM' AND PAYMENT_METHOD ='CARD' AND COVER_START_DATE >'"
                        + new Date().fiveMonthsAgo() + "' AND POLICY_STATUS='P'")
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
            // TODO update this query TSV_POB
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

    String getActivePolicyBasedOnCenterCode(centerCode){
        def row = getFirstResult("SELECT PE.POLICY_NO FROM POLICY_ENTITY PE WHERE " +
                "PE.CENTER_CODE = '" + centerCode + "' " +
                "AND PE.POLICY_NO IN(SELECT P.POLICY_NO FROM POLICY P WHERE " +
                "TRANSACTION_TYPE = '" + TestDataUtils.PolicyTransactionType.NEW_BUISNESS + "' " +
                "GROUP BY P.POLICY_NO HAVING COUNT(P.POLICY_NO)=1)")
        return(row.POLICY_NO)
    }

    String getCancelledPolicy(centerCode){
        def row = getFirstResult("SELECT PE.POLICY_NO FROM POLICY_ENTITY PE WHERE " +
                "PE.CENTER_CODE = '" + centerCode + "' " +
                "AND PE.POLICY_NO IN(SELECT P.POLICY_NO FROM POLICY P WHERE " +
                "TRANSACTION_TYPE = '" + TestDataUtils.PolicyTransactionType.CANCELLED + "' " +
                "GROUP BY P.POLICY_NO)")
        return(row.POLICY_NO)
    }

    String getActivePolicyWithMultipleVersions(centerCode){
        def row = getFirstResult("SELECT PE.POLICY_NO FROM POLICY_ENTITY PE WHERE " +
                "PE.CENTER_CODE = '" + centerCode + "' " +
                "AND PE.POLICY_NO IN(SELECT P.POLICY_NO FROM POLICY P GROUP BY P.POLICY_NO HAVING COUNT(P.POLICY_NO)>=2)")
        return(row.POLICY_NO)
    }

    String getPolicyWithPaymentType(paymentType) {
        def row
        switch (paymentType) {
            case TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_DD :
                row = getFirstResult ( "SELECT DISTINCT P.POLICY_NO FROM POLICY P WHERE P.INSTL_PLAN_TYPE IN('1','3','4') ORDER BY POLICY_NO ASC" )
                break
            case TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_CPA :
                row = getFirstResult ( "SELECT DISTINCT P.POLICY_NO FROM POLICY P WHERE P.INSTL_PLAN_TYPE IN('5') ORDER BY POLICY_NO ASC" )
                break
            case TestDataUtils.PolicyPaymentType.PAYMENT_TYPE_CARD :
                row = getFirstResult ( "SELECT DISTINCT P.POLICY_NO FROM POLICY P WHERE P.INSTL_PLAN_TYPE IN('2') ORDER BY POLICY_NO ASC" )
                break
        }
        return ( row.POLICY_NO)
    }
}
