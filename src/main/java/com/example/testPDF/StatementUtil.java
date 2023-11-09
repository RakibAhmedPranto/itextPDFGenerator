package com.example.testPDF;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatementUtil {

    public static String commaSeparatedAmountLong(Long amount) {

        BigDecimal decimalAmount = amount == null ? BigDecimal.ZERO : new BigDecimal(amount);
        String amountCommaSeparated = commaSeparatedAmount(decimalAmount);
        return amountCommaSeparated.substring(0, amountCommaSeparated.length() - 3);
    }

    public static String commaSeparatedAmountInteger(Integer amount) {

        return commaSeparatedAmountLong(amount.longValue());
    }

    public static String commaSeparatedAmount(String amount) {

        if(amount == null) {
            return commaSeparatedAmount(BigDecimal.ZERO);
        }

        BigDecimal decimalAmount = new BigDecimal(amount);
        return commaSeparatedAmount(decimalAmount);
    }

    private static String commaSeparate(String amount) {

        int len = amount.length();
        String commaSeparatedAmount = "." + amount.split("[.]")[1];
        for (int i = len - 4, j = 0; amount.charAt(0) == '-' ? i >= 1 : i >= 0; i--, j++) {
            if (j >= 3 && (j - 3) % 2 == 0)
                commaSeparatedAmount = ",".concat(commaSeparatedAmount);
            commaSeparatedAmount = amount.charAt(i) + commaSeparatedAmount;
        }
        return amount.charAt(0) == '-' ? "-".concat(commaSeparatedAmount) : commaSeparatedAmount;
    }

    public static String commaSeparatedAmount(BigDecimal amount) {

        if (amount == null)
            return "0.00";

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        return commaSeparate(amount.toString());
    }

    public static Timestamp getStartingTimestampOfYesterday() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getStartingTimestampOfToday() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getTimestampFromString(String time, String format) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parsedDate = dateFormat.parse(time);
        return new Timestamp(parsedDate.getTime());
    }

    public static String getTimeFormatted(String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(System.currentTimeMillis());
    }

    public static String getTimeFormatted(Timestamp timestamp, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }

    public static String getTimeFormatted(Date date, String format) {
        return getTimeFormatted(new Timestamp(date.getTime()), format);
    }
}
