package cav.travelphotomanager.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cav.travelphotomanager.R;

public class NewEditCardDialog extends DialogFragment {

    private NewEditCardDialogListener mCardDialogListener;

    private EditText mName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.newedit_dialog, null);
        mName = (EditText) v.findViewById(R.id.ne_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Новая местоположение")
                .setView(v).setNegativeButton(R.string.dialog_cancel,null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int with) {
                        if (mCardDialogListener != null){
                            mCardDialogListener.changeName(mName.getText().toString());
                        }
                    }
                });
        return builder.create();
    }

    public void setNewEditCardDialogListener (NewEditCardDialogListener listener){
        mCardDialogListener = listener;
    }

    public interface NewEditCardDialogListener {
        public void changeName(String name);
    }
}