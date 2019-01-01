package rmnvich.apps.notes.presentation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateHelper {

    public static long getCurrentTimeInMills() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String convertLongTimeToString(long time) {
        return new SimpleDateFormat("dd MMMM yyyy",
                Locale.getDefault()).format(time);
    }
}
