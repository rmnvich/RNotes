package rmnvich.apps.notes.presentation.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import rmnvich.apps.notes.R;

public class DialogMoreActions extends BottomSheetDialog {

    public interface DialogMoreCallback {

        void onClickPickImage();

        void onClickColor();

        void onClickDate();

        void onClickLabel();

        void onClickFavorite();

        void onClickLock();

        void onClickShare();
    }

    private DialogMoreCallback mCallback = null;

    private ImageView mIvLock;
    private ImageView mIvFavorite;

    private TextView mTvLock;
    private TextView mTvFavorite;

    @SuppressLint("InflateParams")
    public DialogMoreActions(@NonNull Context context) {
        super(context, R.style.BottomSheetDialog);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        View view = inflater.inflate(R.layout.add_edit_note_menu_dialog, null);

        mIvLock = view.findViewById(R.id.iv_dialog_lock);
        mIvFavorite = view.findViewById(R.id.iv_dialog_favorite);

        mTvLock = view.findViewById(R.id.tv_dialog_lock);
        mTvFavorite = view.findViewById(R.id.tv_dialog_favorite);

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

        view.findViewById(R.id.layout_favorite_note).setOnClickListener(button -> {
            mCallback.onClickFavorite();
            this.dismiss();
        });

        view.findViewById(R.id.layout_lock_note).setOnClickListener(button -> {
            mCallback.onClickLock();
            this.dismiss();
        });

        view.findViewById(R.id.layout_share_note).setOnClickListener(button -> {
            mCallback.onClickShare();
            this.dismiss();
        });

        this.setContentView(view);
    }

    public void show(boolean isFavoriteNote, boolean isLockedNote,
                     final DialogMoreCallback callback) {
        if (mCallback == null)
            mCallback = callback;

        setFavoriteLayoutResources(isFavoriteNote);
        setLockLayoutResources(isLockedNote);

        this.show();
    }

    private void setFavoriteLayoutResources(boolean isFavoriteNote) {
        String textResource;
        int imageResource;

        if (isFavoriteNote) {
            textResource = getContext().getString(R.string.unfavorite_note);
            imageResource = R.drawable.ic_action_star_border_inverted;
        } else {
            textResource = getContext().getString(R.string.favorite_note);
            imageResource = R.drawable.ic_action_star_inverted;
        }

        mTvFavorite.setText(textResource);
        mIvFavorite.setImageResource(imageResource);
    }

    private void setLockLayoutResources(boolean isLockedNote) {
        String textResource;
        int imageResource;

        if (isLockedNote) {
            textResource = getContext().getString(R.string.unlock_note);
            imageResource = R.drawable.ic_action_unpin_inverted;
        } else {
            textResource = getContext().getString(R.string.lock_note);
            imageResource = R.drawable.ic_action_pin_inverted;
        }

        mTvLock.setText(textResource);
        mIvLock.setImageResource(imageResource);
    }
}
