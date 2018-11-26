package tks.com.gwaandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import tks.com.gwaandroid.ManageOrderActivity;
import tks.com.gwaandroid.ProfileActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.TradeDetailsActivity;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Orderrequest;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class CustomManageOrderAdapter extends RecyclerView.Adapter<CustomManageOrderAdapter.ViewHolder>{
    private static final String TAG = "CustomManageOrderAdapter";

    private List<Orderrequest> models;
    private Context mContext;

    public CustomManageOrderAdapter(List<Orderrequest> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_manage_order, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        Log.d(TAG, "onBindViewHolder: " + models.size());

        final Orderrequest model = models.get(position);

        Picasso picasso = Picasso.get();
        if (model.getAccount().getAvatar().contains("localhost:8080")) {
            String imageUrl = model.getAccount().getAvatar().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.avatar);
        } else {
            picasso.load(model.getAccount().getAvatar())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.avatar);
        }

        holder.username.setText(model.getAccount().getUsername());
        holder.requestDate.setText(model.getOrderDate());
        holder.quantity.setText(model.getQuantity()+"");


        if(model.getStatus().equals(AppConstant.APPROVED_STATUS)){
            holder.stateTitle.setText("Accepted On");
            holder.stateDate.setText(model.getStateSetDate());
            holder.stateTitle.setVisibility(View.VISIBLE);
            holder.stateDate.setVisibility(View.VISIBLE);

            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_decline.setVisibility(View.GONE);

            holder.btn_done.setVisibility(View.VISIBLE);
            holder.btn_cancel.setVisibility(View.VISIBLE);
        }
        if(model.getStatus().equals(AppConstant.SUCCEED_STATUS)){
            holder.stateTitle.setText("Succeed On");
            holder.stateDate.setText(model.getStateSetDate());
            holder.stateTitle.setVisibility(View.VISIBLE);
            holder.stateDate.setVisibility(View.VISIBLE);

            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_decline.setVisibility(View.GONE);

            if(model.isRated()){
                holder.btn_rated.setVisibility(View.VISIBLE);
            }else {
                holder.btn_rating.setVisibility(View.VISIBLE);
            }

        }
        if(model.getStatus().equals(AppConstant.CANCELLED_STATUS)){
            holder.stateTitle.setText("Cancelled On");
            holder.stateDate.setText(model.getStateSetDate());
            holder.stateTitle.setVisibility(View.VISIBLE);
            holder.stateDate.setVisibility(View.VISIBLE);

            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_decline.setVisibility(View.GONE);


            holder.btn_reason.setVisibility(View.VISIBLE);

        }


        //Click to view profile
        holder.user_wrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("PROFILEID", model.getAccount().getId());
                mContext.startActivity(intent);
            }
        });


        //Click to accept order
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure to accept this trade?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                Call<String> call = trademarketAPI.acceptOrder(model.getId());
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                        ManageOrderActivity activity = (ManageOrderActivity) mContext;
                                        activity.finish();
                                        mContext.startActivity(activity.getIntent());
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("ACCEPTORDERAPI", "onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();

            }
        });


        //Click to decline order
        holder.btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ManageOrderActivity activity = (ManageOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_send_reason, null);

                final EditText txt_reason = (EditText) view.findViewById(R.id.txt_send_reason);


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure to decline this request?")
                        .setView(view)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String reason = txt_reason.getText().toString();
                                TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                Call<String> call = trademarketAPI.declineOrder(model.getId(), reason);
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


        //Click to done order
        holder.btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure to confirm this trade success?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                Call<String> call = trademarketAPI.confirmSucceedOrder(model.getId());
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                        ManageOrderActivity activity = (ManageOrderActivity) mContext;
                                        activity.finish();
                                        mContext.startActivity(activity.getIntent());
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("ACCEPTORDERAPI", "onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });

        //Click to cancel order
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ManageOrderActivity activity = (ManageOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_send_reason, null);

                final EditText txt_reason = (EditText) view.findViewById(R.id.txt_send_reason);


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure to cancel this order?")
                        .setView(view)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String reason =  "CANCEL FROM OWNER: " + txt_reason.getText().toString();
                                TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                                Call<String> call = trademarketAPI.cancelOrder(model.getId(), reason);
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

        //Click to Rating
        holder.btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ManageOrderActivity activity = (ManageOrderActivity) mContext;

                LayoutInflater inflater = activity.getLayoutInflater();

                View view = inflater.inflate(R.layout.layout_rating_form, null);

                final EditText txt_feedback = (EditText) view.findViewById(R.id.txt_send_feedback);
                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_order);


                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Rating trader")
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
                                    Call<String> call = trademarketAPI.ratingTrade(model.getId(),
                                            AppConstant.FEEDBACK_TYPE_OWNER_TO_TRADER,rating,feedback);

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
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("View cancel reason")
                        .setMessage(model.getCancelReason())
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

        ImageView avatar;
        TextView username,requestDate,stateTitle,stateDate,quantity;
        RelativeLayout user_wrap;

        View list_manageorder;

        Button btn_accept,btn_decline,btn_done,btn_cancel,btn_rating,btn_rated,btn_reason;

        public ViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.item_manageorder_avatar);

            username = (TextView) itemView.findViewById(R.id.item_manageorder_username);
            requestDate = (TextView) itemView.findViewById(R.id.item_manageorder_requestDate);
            stateTitle = (TextView) itemView.findViewById(R.id.item_manageorder_stateTitle);
            stateDate = (TextView) itemView.findViewById(R.id.item_manageorder_stateDate);
            quantity = (TextView) itemView.findViewById(R.id.item_manageorder_quantity);

            user_wrap = (RelativeLayout) itemView.findViewById(R.id.manageorder_user_wrap);

            btn_accept = (Button) itemView.findViewById(R.id.btn_manageorder_accept);
            btn_decline = (Button) itemView.findViewById(R.id.btn_manageorder_decline);
            btn_done = (Button) itemView.findViewById(R.id.btn_manageorder_done);
            btn_cancel = (Button) itemView.findViewById(R.id.btn_manageorder_cancel);
            btn_rating = (Button) itemView.findViewById(R.id.btn_manageorder_rating);
            btn_rated = (Button) itemView.findViewById(R.id.btn_manageorder_rated);
            btn_reason = (Button) itemView.findViewById(R.id.btn_manageorder_reason);

            list_manageorder = itemView;
        }
    }

}
