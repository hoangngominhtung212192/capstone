package tks.com.gwaandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import tks.com.gwaandroid.R;

public class UpdateQuantityDialog extends AppCompatDialogFragment {
    private EditText txt_quantity;
    private int currentQuantity, tradepostId;
    private UpdateQuantityDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_update_quantity, null);

        //init
        currentQuantity = getArguments().getInt("QUANTITYSTOCK");
        tradepostId = getArguments().getInt("TRADEPOSTID");

        txt_quantity = (EditText) view.findViewById(R.id.txt_update_quantity);

        txt_quantity.setText(currentQuantity + "");

        builder.setView(view)
                .setTitle("Update Quantity")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUpdateQuantityDialogConfirm(tradepostId, Integer.parseInt(txt_quantity.getText().toString()));
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (UpdateQuantityDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement UpdateQuantityDialogListener");
        }
    }

    public interface UpdateQuantityDialogListener{
        void onUpdateQuantityDialogConfirm(int tradepostId, int newQuantity);
    }
}
