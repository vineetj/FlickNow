package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConvertUtils {

    public static String formatDate(String sourceFormatString, String desiredFormatString, String dateString) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(sourceFormatString, Locale.ENGLISH);
        SimpleDateFormat desiredFormat = new SimpleDateFormat(desiredFormatString, Locale.ENGLISH);

        Date date = null;
        try {
            date = sourceFormat.parse(dateString);
            return desiredFormat.format(date.getTime());

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateString;
    }
}
