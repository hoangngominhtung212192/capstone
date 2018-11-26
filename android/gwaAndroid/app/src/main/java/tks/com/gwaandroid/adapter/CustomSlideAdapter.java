package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import tks.com.gwaandroid.constant.AppConstant;

public class CustomSlideAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrls;


    public CustomSlideAdapter(Context context, String[] imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }


    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        if (imageUrls[position].contains("localhost:8080")) {
            String imageUrl = imageUrls[position].replace("localhost", AppConstant.HOST_NAME);
            Log.d("IMAGEDETAIL", "instantiateItem: " + imageUrl);
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .into(imageView);
        } else {
            Log.d("IMAGEDETAIL", "instantiateItem: " + imageUrls[position]);
            Picasso.get()
                    .load(imageUrls[position])
                    .fit()
                    .into(imageView);
        }



        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
