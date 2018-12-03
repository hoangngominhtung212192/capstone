package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tks.com.gwaandroid.R;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Traderating;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class CustomUserRatingAdapter extends RecyclerView.Adapter<CustomUserRatingAdapter.CustomViewHolder> {

    private List<Traderating> traderatingList;
    private Context context;

    public CustomUserRatingAdapter(List<Traderating> traderatingList, Context context) {
        this.traderatingList = traderatingList;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View uView;

        TextView uName;
        TextView uDatetime;
        TextView uComment;
        ImageView uAvatar;
        ImageView star_one;
        ImageView star_two;
        ImageView star_three;
        ImageView star_four;
        ImageView star_five;

        CustomViewHolder(View itemView) {
            super(itemView);
            uView = itemView;

            uName = (TextView) uView.findViewById(R.id.userName);
            uDatetime = (TextView) uView.findViewById(R.id.datetime);
            uComment = (TextView) uView.findViewById(R.id.comment);
            uAvatar = (ImageView) uView.findViewById(R.id.userAvatar);

            // get star imageview
            star_one = (ImageView) uView.findViewById(R.id.user_star_one);
            star_two = (ImageView) uView.findViewById(R.id.user_star_two);
            star_three = (ImageView) uView.findViewById(R.id.user_star_three);
            star_four = (ImageView) uView.findViewById(R.id.user_star_four);
            star_five = (ImageView) uView.findViewById(R.id.user_star_five);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_exchange_evaluation, parent, false);
        return new CustomUserRatingAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Traderating traderating = traderatingList.get(position);

        // download image from url
        Picasso picasso = Picasso.get();
        if (traderating.getFromUser().getAvatar().contains("localhost:8080")) {
            String imageUrl = traderating.getFromUser().getAvatar().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.uAvatar);
        } else {
            picasso.load(traderating.getFromUser().getAvatar())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.uAvatar);
        }

        // set user's fullname
        holder.uName.setText(traderating.getFromUser().getUsername());

        // set rating
        if (traderating.getRating() >= 1) {
            holder.star_one.setVisibility(View.VISIBLE);
        }
        if (traderating.getRating() >= 2) {
            holder.star_two.setVisibility(View.VISIBLE);
        }
        if (traderating.getRating() >= 3) {
            holder.star_three.setVisibility(View.VISIBLE);
        }
        if (traderating.getRating() >= 4) {
            holder.star_four.setVisibility(View.VISIBLE);
        }
        if (traderating.getRating() == 5) {
            holder.star_five.setVisibility(View.VISIBLE);
        }

        // set datetime
        holder.uDatetime.setText(traderating.getRatingDate());

        // set comment
        holder.uComment.setText(traderating.getComment());

    }

    @Override
    public int getItemCount() {
        return traderatingList.size();
    }
}
