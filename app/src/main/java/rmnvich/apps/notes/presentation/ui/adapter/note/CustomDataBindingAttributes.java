package rmnvich.apps.notes.presentation.ui.adapter.note;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

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
}

