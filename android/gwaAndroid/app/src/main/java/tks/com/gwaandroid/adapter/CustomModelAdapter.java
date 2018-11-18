package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import tks.com.gwaandroid.ModelDetailActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.model.Model;
import tks.com.gwaandroid.model.ModelSDTO;

/**
 * Created by Tung Hoang Ngo Minh on 11/13/2018.
 */

public class CustomModelAdapter extends RecyclerView.Adapter<CustomModelAdapter.CustomViewHolder> {

    private ModelSDTO modelSDTO;
    private Context context;

    public CustomModelAdapter(ModelSDTO modelSDTO, Context context) {
        this.modelSDTO = modelSDTO;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView mModelName;
        TextView mSeriestitle;
        TextView mProductseries;
        TextView mRating;
        private ImageView mImage;
        ImageView star_one;
        ImageView star_two;
        ImageView star_three;
        ImageView star_four;
        ImageView star_five;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mModelName = (TextView) mView.findViewById(R.id.rModelName);
            mSeriestitle = (TextView) mView.findViewById(R.id.rSeriestitle);
            mProductseries = (TextView) mView.findViewById(R.id.rProductseries);
            mRating = (TextView) mView.findViewById(R.id.rRating);
            mImage = (ImageView) mView.findViewById(R.id.rImageView);

            // get star imageview
            star_one = (ImageView) mView.findViewById(R.id.star_one);
            star_two = (ImageView) mView.findViewById(R.id.star_two);
            star_three = (ImageView) mView.findViewById(R.id.star_three);
            star_four = (ImageView) mView.findViewById(R.id.star_four);
            star_five = (ImageView) mView.findViewById(R.id.star_five);
        }
    }

    // create view holder, initialize
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row, parent, false);
        return new CustomViewHolder(view);
    }

    // render data
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Model model = modelSDTO.getModelDTOList().get(position).getModel();

        // set model name
        holder.mModelName.setText(model.getName());

        // download image from url
        Picasso picasso = Picasso.with(context);
        if (model.getThumbImage().contains("localhost:8080")) {
            String imageUrl = model.getThumbImage().replace("localhost", "192.168.1.6");
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        } else {
            picasso.load(model.getThumbImage())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        }

        // set series title
        holder.mSeriestitle.setText("Series title: " + model.getSeriestitle().getName());
        // set scale
        holder.mProductseries.setText("Scale: " + model.getProductseries().getName());

        // set rating
        if (model.getNumberOfRater() != 0) {
            holder.mRating.setText("Rating: ");
            int avgRating = Math.round((float) model.getNumberOfRating() / (float) model.getNumberOfRater());

            if (avgRating >= 1) {
                holder.star_one.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 2) {
                holder.star_two.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 3) {
                holder.star_three.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 4) {
                holder.star_four.setVisibility(View.VISIBLE);
            }
            if (avgRating == 5) {
                holder.star_five.setVisibility(View.VISIBLE);
            }
        } else {
            // non-rating
            holder.star_one.setVisibility(View.GONE);
            holder.star_two.setVisibility(View.GONE);
            holder.star_three.setVisibility(View.GONE);
            holder.star_four.setVisibility(View.GONE);
            holder.star_five.setVisibility(View.GONE);
            holder.mRating.setText("Rating: N/A");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModelDetailActivity.class);
                intent.putExtra("modelID", modelSDTO.getModelDTOList().get(position).getModel().getId() + "");
                context.startActivity(intent);
            }
        });
    }

    // get item count (this is important)
    @Override
    public int getItemCount() {
        return modelSDTO.getModelDTOList().size();
    }


}
