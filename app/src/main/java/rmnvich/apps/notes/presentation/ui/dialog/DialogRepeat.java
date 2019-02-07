package rmnvich.apps.notes.presentation.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import rmnvich.apps.notes.R;

public class DialogRepeat extends MaterialDialog.Builder implements MaterialDialog.ListCallbackSingleChoice {

    public interface SelectRepeatCallback {
        void onRepeatTypeSelected(int repeatType);
    }

    private SelectRepeatCallback mCallback = null;

    public DialogRepeat(Context context) {
        super(context);
        this.title(context.getResources().getString(R.string.repeat))
                .titleColor(Color.BLACK)
                .positiveText(R.string.select)
                .autoDismiss(false)
                .cancelable(true)
                .items(context.getResources().getStringArray(R.array.repeat_types));
    }

    public void show(int repeatType, SelectRepeatCallback callback) {
        this.itemsCallbackSingleChoice(repeatType, this);
        if (mCallback == null) {
            mCallback = callback;
        }
        this.show();
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        mCallback.onRepeatTypeSelected(which);
        dialog.dismiss();
        return false;
    }
}
