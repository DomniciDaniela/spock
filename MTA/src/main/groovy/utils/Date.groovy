package utils

import org.codehaus.groovy.runtime.DateGroovyMethods

class Date {

    String currentDate() {
        def date = new java.util.Date()
        return DateGroovyMethods.format(date, 'dd-MMM-yyyy')
    }

    String previousMonthDayDate() {
        Calendar originalDate = Calendar.getInstance()
        Calendar previousMonthDay = (Calendar) originalDate.clone()
        previousMonthDay.add(Calendar.MONTH, - 1)

        return DateGroovyMethods.format(previousMonthDay.getTime(), 'dd-MMM-yyyy')
    }

    String fiveMonthsAgo() {
        Calendar originalDate = Calendar.getInstance()
        Calendar previousMonthDay = (Calendar) originalDate.clone()
        previousMonthDay.add(Calendar.MONTH, - 5)

        return DateGroovyMethods.format(previousMonthDay.getTime(), 'dd-MMM-yyyy')
    }
}
