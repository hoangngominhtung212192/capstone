package tks.com.gwaandroid.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.GetDirectionActivity;
import tks.com.gwaandroid.MyOrderActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.TradeDetailsActivity;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.MyOrderModel;
import tks.com.gwaandroid.model.Orderrequest;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class CustomMyOrderAdapter extends RecyclerView.Adapter<CustomMyOrderAdapter.ViewHolder> {
    private static final String TAG = "CustomMyOrderAdapter";
    private OnPhoneCallListener phoneCallListener;


    private List<MyOrderModel> models;
    private Context mContext;

    public CustomMyOrderAdapter(List<MyOrderModel> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
    }


    public void setPhoneCallListener(OnPhoneCallListener phoneCallListener) {
        this.phoneCallListener = phoneCallListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_myorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        Log.d(TAG, "onBindViewHolder: " + models.size());

        final MyOrderModel model = models.get(position);

        Picasso picasso = Picasso.get();
        if (model.getTradepostThumbnail().contains("localhost:8080")) {
            String imageUrl = model.getTradepostThumbnail().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.thumbnail);
        } else {
            picasso.load(model.getTradepostThumbnail())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.thumbnail);
        }

        holder.title.setText(model.getTradepostTitle());
        holder.requestDate.setText(model.getOrderedDate());
        holder.quantityTotal.setText(model.getOrderQuantity() + " - " + model.getOrderPay() + "$");

        if (model.getOrderStatus().equals(AppConstant.APPROVED_STATUS)) {
            holder.stateTitle.setText("Accepted On:");
            holder.stateDate.setText(model.getOrderSetDate());

        }
        if (model.getOrderStatus().equals(AppConstant.PENDING_STATUS)) {
            holder.stateTitle.setText("Waiting approve");
            holder.stateTitle.setTextColor(Color.GREEN);
            holder.stateDate.setVisibility(View.GONE);

            holder.btn_contact.setVisibility(View.GONE);

        }
        if (model.getOrderStatus().equals(AppConstant.SUCCEED_STATUS)) {
            holder.stateTitle.setText("Succeed On:");
            holder.stateDate.setText(model.getOrderSetDate());

            holder.btn_contact.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);


            if (model.isRated()) {
                holder.btn_rated.setVisibility(View.VISIBLE);
            } else {
                holder.btn_rating.setVisibility(View.VISIBLE);
            }

        }
        if (model.getOrderStatus().equals(AppConstant.CANCELLED_STATUS)) {
            holder.stateTitle.setText("Cancelled On:");
            holder.stateDate.setText(model.getOrderSetDate());

            holder.btn_contact.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);

            holder.btn_reason.setVisibility(View.VISIBLE);

        }
        if (model.getOrderStatus().equals(AppConstant.DECLINED_STATUS)) {

            holder.stateTitle.setText("Declined On:");
            holder.stateDate.setText(model.getOrderSetDate());

            holder.btn_contact.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);

            holder.btn_reason.setVisibility(View.VISIBLE);

        }


        //Click to view tradepost details
        holder.trade_wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TradeDetailsActivity.class);
                intent.putExtra("TRADEPOSTID", model.getTradepostId());
                intent.putExtra("CALLINGACTIVITY", "MYORDER");
                mContext.startActivity(intent);

            }
        });

        //Click to view contact
        holder.btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyOrderActivity activity = (MyOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_view_contact, null);

                TextView txt_name = (TextView) view.findViewById(R.id.txt_view_contact_name);
                final TextView txt_phone = (TextView) view.findViewById(R.id.txt_view_contact_phone);
                TextView txt_email = (TextView) view.findViewById(R.id.txt_view_contact_email);
                TextView txt_address = (TextView) view.findViewById(R.id.txt_view_contact_address);

                txt_name.setText(model.getOwnerName());
                txt_phone.setText(model.getOwnerPhone());
                txt_email.setText(model.getOwnerEmail());
                txt_address.setText(model.getOwnerAddress());

                ImageButton btn_phone = (ImageButton) view.findViewById(R.id.btn_view_contact_phone);
                ImageButton btn_mail = (ImageButton) view.findViewById(R.id.btn_view_contact_email);
                ImageButton btn_direction = (ImageButton) view.findViewById(R.id.btn_view_contact_direction);

                //Click to call
                btn_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(phoneCallListener!= null){
                            phoneCallListener.onPhoneCallClick(model.getOwnerPhone());
                        }

                    }
                });


                //Click to email
                btn_mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",model.getOwnerEmail(), null));
                        mContext.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    }
                });

                //Click to direction
                btn_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GetDirectionActivity.class);
                        intent.putExtra("LOCATION", model.getOwnerAddress());
                        mContext.startActivity(intent);

                    }
                });




                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Contact Information")
                        .setView(view)
                        .setNegativeButton("Close", null);
                builder.create().show();

            }
        });

        //Click to cancel order
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyOrderActivity activity = (MyOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_send_reason, null);

                final EditText txt_reason = (EditText) view.findViewById(R.id.txt_send_reason);


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure to cancel this order?")
                        .setView(view)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String reason =  "CANCEL FROM TRADER: " + txt_reason.getText().toString();
                                TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                Call<String> call = trademarketAPI.cancelOrder(model.getOrderId(), reason);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                        activity.finish();
                                        mContext.startActivity(activity.getIntent());
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("DECLINEORDERAPI", "onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        //Click to rating
        holder.btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyOrderActivity activity = (MyOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_rating_form, null);

                final EditText txt_feedback = (EditText) view.findViewById(R.id.txt_send_feedback);
                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_order);


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Rating owner")
                        .setView(view)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String feedback = txt_feedback.getText().toString();
                                int rating = (int) ratingBar.getRating();

                                if (rating < 1){
                                    dialog.cancel();
                                    Toast.makeText(mContext, "Please select your rating", Toast.LENGTH_SHORT).show();
                                }else {

                                    TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                    Call<String> call = trademarketAPI.ratingTrade(model.getOrderId(),
                                            AppConstant.FEEDBACK_TYPE_TRADER_TO_OWNER,rating,feedback);

                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                            activity.finish();
                                            mContext.startActivity(activity.getIntent());
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("DECLINEORDERAPI", "onFailure: " + t.getMessage());
                                        }
                                    });

                                }

                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();

            }
        });

        //Click to view reason

        holder.btn_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "View decline reason";
                if (model.getOrderStatus().equals(AppConstant.CANCELLED_STATUS)){
                    title = "View cancel reason";
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(title)
                        .setMessage(model.getOrderReason())
                        .setNegativeButton("Close", null);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View list_myorder;

        ImageView thumbnail;
        TextView title,requestDate,stateTitle,stateDate,quantityTotal;
        RelativeLayout trade_wrap;

        Button btn_contact,btn_cancel,btn_rating,btn_rated,btn_reason;


        public ViewHolder(View itemView) {
            super(itemView);
            list_myorder = itemView;

            thumbnail = (ImageView) itemView.findViewById(R.id.item_myorder_thumbnail);

            title = (TextView) itemView.findViewById(R.id.item_myorder_title);
            requestDate = (TextView) itemView.findViewById(R.id.item_myorder_requestDate);
            stateDate = (TextView) itemView.findViewById(R.id.item_myorder_stateDate);
            stateTitle = (TextView) itemView.findViewById(R.id.item_myorder_stateTitle);
            quantityTotal = (TextView) itemView.findViewById(R.id.item_myorder_quantity_total);
            trade_wrap = (RelativeLayout) itemView.findViewById(R.id.myorder_trade_wrap);

            btn_contact = (Button) itemView.findViewById(R.id.btn_myorder_contact);
            btn_cancel = (Button) itemView.findViewById(R.id.btn_myorder_cancel);
            btn_rating = (Button) itemView.findViewById(R.id.btn_myorder_rating);
            btn_rated = (Button) itemView.findViewById(R.id.btn_myorder_rated);
            btn_reason = (Button) itemView.findViewById(R.id.btn_myorder_reason);

        }


    }

    public interface OnPhoneCallListener{
        void onPhoneCallClick(String phoneNumber);
    }


}
