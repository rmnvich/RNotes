package rmnvich.apps.notes.presentation.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;

import rmnvich.apps.notes.R;

public class DialogMoreActions extends BottomSheetDialog {

    public interface DialogMoreCallback {

        void onClickPickImage();

        void onClickColor();

        void onClickDate();

        void onClickLabel();

        void onClickShare();
    }

    private DialogMoreCallback mCallback = null;

    @SuppressLint("InflateParams")
    public DialogMoreActions(@NonNull Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        View view = inflater.inflate(R.layout.add_edit_note_menu_dialog, null);

        view.findViewById(R.id.layout_select_image).setOnClickListener(button -> {
            mCallback.onClickPickImage();
            this.dismiss();
        });

        view.findViewById(R.id.layout_select_color).setOnClickListener(button -> {
            mCallback.onClickColor();
            this.dismiss();
        });

        view.findViewById(R.id.layout_select_date).setOnClickListener(button -> {
            mCallback.onClickDate();
            this.dismiss();
        });

        view.findViewById(R.id.layout_select_label).setOnClickListener(button -> {
            mCallback.onClickLabel();
            this.dismiss();
        });

        view.findViewById(R.id.layout_share_note).setOnClickListener(button -> {
            mCallback.onClickShare();
            this.dismiss();
        });

        this.setContentView(view);
    }

    public void show(final DialogMoreCallback callback) {
        if (mCallback == null)
            mCallback = callback;
        this.show();
    }
}
