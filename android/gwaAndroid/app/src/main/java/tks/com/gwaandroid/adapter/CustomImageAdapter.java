package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tks.com.gwaandroid.R;
import tks.com.gwaandroid.model.ImageModel;

public class CustomImageAdapter extends BaseAdapter {
    Context context;
    ArrayList<ImageModel> models;

    public CustomImageAdapter(Context context, ArrayList<ImageModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image_upload,parent,false);
        }
        final ImageModel model = (ImageModel) this.getItem(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.upload_image);
        ImageButton removeImage = (ImageButton) convertView.findViewById(R.id.btn_remove_image);

        Picasso.get()
                .load(model.getUri())
                .placeholder(R.drawable.loading_icon)
                .into(image);

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Remove Image " + position, Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }
}
