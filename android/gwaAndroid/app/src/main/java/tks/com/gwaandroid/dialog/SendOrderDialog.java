package tks.com.gwaandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.api.UserAPI;
import tks.com.gwaandroid.model.Profile;
import tks.com.gwaandroid.model.SendOrderRequest;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class SendOrderDialog extends AppCompatDialogFragment {
    private EditText txtName, txtEmail, txtPhone, txtAddress;
    private ElegantNumberButton quantityBtn;
    private ImageButton btn_my_location;
    private int stockQuantity, loginAccount, tradepostId;
    private  SendOrderDialogListener listener;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_send_order, null);

        //init
        stockQuantity = getArguments().getInt("QUANTITYSTOCK");
        loginAccount = getArguments().getInt("LOGINACCOUNT");
        tradepostId = getArguments().getInt("TRADEPOSTID");

        quantityBtn = (ElegantNumberButton) view.findViewById(R.id.btn_order_quantity);
        btn_my_location = (ImageButton) view.findViewById(R.id.btn_order_my_location);
        txtName = (EditText) view.findViewById(R.id.txt_order_name);
        txtEmail = (EditText) view.findViewById(R.id.txt_order_email);
        txtPhone = (EditText) view.findViewById(R.id.txt_order_phone);
        txtAddress = (EditText) view.findViewById(R.id.txt_order_address);

        //setup Quantity Btn
        quantityBtn.setRange(1,stockQuantity);

        //LOAD PROFILE DATA
        getProfileData(loginAccount);

        builder.setView(view)
                .setTitle("Your order information")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })
                .setPositiveButton("Send Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quantity = Integer.parseInt(quantityBtn.getNumber());
                        String name = txtName.getText().toString();
                        String email = txtEmail.getText().toString();
                        String phone = txtPhone.getText().toString();
                        String address = txtAddress.getText().toString();
                        SendOrderRequest sendOrderRequest = new SendOrderRequest(loginAccount,email,phone,address,tradepostId,quantity);

                        listener.onSendOrderDialogConfirm(sendOrderRequest);


                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SendOrderDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SendOrderDialogListener");
        }

    }

    private void getProfileData(int accountID) {
        UserAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserAPI.class);

        Call<Profile> call = userAPI.getUserProfile(accountID);

        // execute request
        call.enqueue(new Callback<Profile>() {
            // get json response
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    bindingProfile(response.body());
                } else {
                    Toast.makeText(getContext(), "Response error", Toast.LENGTH_SHORT).show();
                }
            }
            // error
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void bindingProfile(Profile result) {
        String firstName = result.getFirstName();
        String middleName = result.getMiddleName();
        String lastName = result.getLastName();
        String fullName = "";

        if (middleName != null) {
            fullName = lastName + " " + middleName + " " + firstName;
        } else {
            fullName = lastName + " " + firstName;
        }
        txtName.setText(fullName);
        txtEmail.setText(result.getEmail());
        txtPhone.setText(result.getTel());
        txtAddress.setText(result.getAddress());

    }

    public interface SendOrderDialogListener{
        void onSendOrderDialogConfirm(SendOrderRequest sendOrderData);
    }
}
