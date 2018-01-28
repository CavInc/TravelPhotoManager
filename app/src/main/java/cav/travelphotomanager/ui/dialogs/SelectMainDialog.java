package cav.travelphotomanager.ui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cav.travelphotomanager.R;
import cav.travelphotomanager.utils.ConstantManager;

public class SelectMainDialog extends DialogFragment implements View.OnClickListener {

    private SelectMainDialogListener mSelectMainDialogListener;

    @Override
    public void onClick(View view) {
        if (mSelectMainDialogListener != null){
            int item = -1;
            switch (view.getId()){
                case R.id.dialog_send_item:
                    item = ConstantManager.SEND_ITEM;
                    break;
                case R.id.dialog_del_item:
                    item = ConstantManager.DELETE_ITEM;
                    break;
            }
            mSelectMainDialogListener.onSelectedItem(item);
            dismiss();
        }
    }

    public interface SelectMainDialogListener {
        public void onSelectedItem(int item);
    }

    public static SelectMainDialog newInstance(){
        SelectMainDialog dialog = new SelectMainDialog();
        return dialog;
    }

    public void setSelectMainDialogListener(SelectMainDialogListener listener) {
        mSelectMainDialogListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.select_main_dialog,container);
        v.findViewById(R.id.dialog_send_item).setOnClickListener(this);
        v.findViewById(R.id.dialog_del_item).setOnClickListener(this);
        return v;
    }
}