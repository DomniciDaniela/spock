package utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class Date {

    DateTime date = new DateTime()
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern('dd-MMM-yyyy')

    String currentDate() {
        return dateFormatter.print(date)
    }

    String previousMonthDayDate() {
        return dateFormatter.print(date.minusMonths(1))
    }

    String fiveMonthsAgo() {
        return dateFormatter.print(date.minusMonths(5))
    }
}
