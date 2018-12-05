package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.ArticleDetailActivity;
import tks.com.gwaandroid.EventDetailActivity;
import tks.com.gwaandroid.ManageOrderActivity;
import tks.com.gwaandroid.ModelDetailActivity;
import tks.com.gwaandroid.MyOrderActivity;
import tks.com.gwaandroid.ProfileActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.TradeDetailsActivity;
import tks.com.gwaandroid.api.NotificationAPI;
import tks.com.gwaandroid.model.Notification;
import tks.com.gwaandroid.model.NotificationDTO;
import tks.com.gwaandroid.network.RetrofitClientInstance;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationAdapter.CustomViewHolder> {

    List<Notification> notificationList;
    private Context context;

    public CustomNotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View nView;

        TextView description;
        RelativeLayout layout_notification;
        ImageView noti_icon;

        CustomViewHolder(View itemView) {
            super(itemView);
            nView = itemView;

            description = (TextView) nView.findViewById(R.id.description);
            layout_notification = (RelativeLayout) nView.findViewById(R.id.layout_notification);
            noti_icon = (ImageView) nView.findViewById(R.id.iconNoti);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_notification, parent, false);
        return new CustomNotificationAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        final Notification notification = notificationList.get(position);
        final String type = notification.getNotificationType().getName();

        holder.description.setText(notification.getDescription());

        if (notification.getSeen() == 0) {
            holder.layout_notification.setBackgroundColor(Color.parseColor("#FAFAD2"));
        } else {
            holder.layout_notification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (type.equalsIgnoreCase("Profile")) {
            holder.noti_icon.setImageResource(R.drawable.ic_person_black_24dp);
        } else if (type.equalsIgnoreCase("Model")) {
            holder.noti_icon.setImageResource(R.drawable.gundam_icon);
        } else if (type.equalsIgnoreCase("Tradepost")) {
            holder.noti_icon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        } else if (type.equalsIgnoreCase("OrderSent")) {
            holder.noti_icon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        } else if (type.equalsIgnoreCase("OrderReceived")) {
            holder.noti_icon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        } else if (type.equalsIgnoreCase("Article")) {
            holder.noti_icon.setImageResource(R.drawable.acticleicon);
        } else if (type.equalsIgnoreCase("Event")) {
            holder.noti_icon.setImageResource(R.drawable.eventicon);
        }

        holder.nView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // check seen
                checkSeenNoti(notification.getId());

                if (type.equalsIgnoreCase("Profile")) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("PROFILEID", notification.getObjectID() + "");
                    context.startActivity(intent);
                } else if (type.equalsIgnoreCase("Model")) {
                    Intent intent = new Intent(context, ModelDetailActivity.class);
                    intent.putExtra("modelID", notification.getObjectID() + "");
                    context.startActivity(intent);
                } else if (type.equalsIgnoreCase("Tradepost")) {
                    Intent intent = new Intent(context, TradeDetailsActivity.class);
                    intent.putExtra("TRADEPOSTID", notification.getObjectID() + "");
                    context.startActivity(intent);
                } else if (type.equalsIgnoreCase("OrderSent")) {
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    context.startActivity(intent);

                } else if (type.equalsIgnoreCase("OrderReceived")) {
                    Intent intent = new Intent(context, ManageOrderActivity.class);
                    intent.putExtra("TRADEPOSTID", notification.getObjectID() + "");
                    context.startActivity(intent);

                } else if (type.equalsIgnoreCase("Article")) {
                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra("articleID", notification.getObjectID() + "");
                    context.startActivity(intent);
                } else if (type.equalsIgnoreCase("Event")) {
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("eventID", notification.getObjectID() + "");
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private void checkSeenNoti(int notiID) {
        NotificationAPI notificationAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationAPI.class);

        Call<String> call = notificationAPI.checkSeen(notiID);

        // execute request
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Response CheckSeen", response.body());
            }

            // error
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error response", t.getMessage());
            }
        });
    }
}
