package rmnvich.apps.notes.presentation.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper {

    public static long getCurrentTimeInMills() {
        return Calendar.getInstance().getTimeInMillis();
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertLongTimeToString(long time) {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(time);
    }

}
