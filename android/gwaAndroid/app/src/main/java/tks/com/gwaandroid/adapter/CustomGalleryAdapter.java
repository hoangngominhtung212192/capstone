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
import tks.com.gwaandroid.model.Modelimage;

/**
 * Created by Tung Hoang Ngo Minh on 11/15/2018.
 */

public class CustomGalleryAdapter extends RecyclerView.Adapter<CustomGalleryAdapter.CustomViewHolder> {

    private List<Modelimage> modelimageList;
    private Context context;

    public CustomGalleryAdapter(List<Modelimage> modelimageList, Context context) {
        this.modelimageList = modelimageList;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View gView;

        private ImageView gImage;
        private TextView gImageType;

        CustomViewHolder(View itemView) {
            super(itemView);
            gView = itemView;

            gImage = (ImageView) gView.findViewById(R.id.rGalleryImage);
            gImageType = (TextView) gView.findViewById(R.id.rGalleryImageType);
        }
    }

    // create view holder, initialize
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_detail, parent, false);
        return new CustomViewHolder(view);
    }

    // render data
    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Modelimage modelimage = modelimageList.get(position);

        // set model name
        holder.gImageType.setText(modelimage.getImagetype().getName());

        // download image from url
        Picasso picasso = Picasso.get();
        if (modelimage.getImageUrl().contains("localhost:8080")) {
            String imageUrl = modelimage.getImageUrl().replace("localhost",AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.gImage);
        } else {
            picasso.load(modelimage.getImageUrl())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.gImage);
        }
    }

    // get item count (this is important)
    @Override
    public int getItemCount() {
        return modelimageList.size();
    }
}
