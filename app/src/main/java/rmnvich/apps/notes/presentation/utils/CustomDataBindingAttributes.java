package rmnvich.apps.notes.presentation.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import rmnvich.apps.notes.R;

public class CustomDataBindingAttributes {

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(new File(url))
                .into(view);
    }

    @BindingAdapter({"app:color"})
    public static void setColor(View view, int color) {
        Drawable drawable = view.getBackground();
        ((GradientDrawable) drawable).setColor(color);
        view.setBackground(drawable);
    }

    @BindingAdapter({"app:repeatType"})
    public static void setRepeatType(TextView view, int repeatType) {
        view.setText(view.getContext().getResources()
                .getStringArray(R.array.repeat_types)[repeatType]);
    }

    @BindingAdapter({"app:date"})
    public static void setDate(TextView view, long time) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(time);
        int givenDay = calendar.get(Calendar.DAY_OF_MONTH);
        int givenMonth = calendar.get(Calendar.MONTH);
        int givenYear = calendar.get(Calendar.YEAR);

        String text = new SimpleDateFormat("dd MMMM yyyy",
                Locale.getDefault()).format(time);

        if (currentYear == givenYear && currentMonth == givenMonth) {
            if (currentDay == givenDay)
                text = view.getContext().getString(R.string.today);
            else if (currentDay == givenDay + 1)
                text = view.getContext().getString(R.string.yesterday);
            else if (currentDay == givenDay - 1) {
                text = view.getContext().getString(R.string.tomorrow);
            }
        }

        view.setText(text);
    }
}

