package tks.com.gwaandroid.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tks.com.gwaandroid.EventDetailActivity;
import tks.com.gwaandroid.R;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.EventSDTO;

public class CustomEventAdapter extends RecyclerView.Adapter<CustomEventAdapter.CustomViewHolder> {

    private EventSDTO sEventObject;
    private Context context;

    public CustomEventAdapter(EventSDTO sEventObject, Context context){
        this.sEventObject = sEventObject;
        this.context = context;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View eView;

        TextView eEventTitle;
        TextView eEventDateStart;
//        TextView mEventDateEnd;
        TextView eEventDescription;
        private ImageView mImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            eView = itemView;

            eEventTitle = (TextView) eView.findViewById(R.id.rEventName);
            eEventDateStart = (TextView) eView.findViewById(R.id.rEventDateStart);
            eEventDescription = (TextView) eView.findViewById(R.id.rEventDescription);
            mImage = (ImageView) eView.findViewById(R.id.rEvThumbView);


        }
    }

    // create view holder, initialize
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
//        List<Event> listEvent = (List<Event>) sEventObject.get(1);
//        List<Event> listEvent = sEventObject.getEventList();
        Event anEvent = sEventObject.getEventList().get(position);

        //set event title
        holder.eEventTitle.setText(anEvent.getTitle());
        System.out.println("title: "+anEvent.getTitle());

        holder.eEventDateStart.setText(anEvent.getStartDate());

        holder.eEventDescription.setText(anEvent.getDescription());
        System.out.println("thumbnail: "+anEvent.getThumbImage());

        // download image from url
        Picasso picasso = Picasso.get();
        if (anEvent.getThumbImage().contains("localhost:8080")) {
            String imageUrl = anEvent.getThumbImage().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        } else {
            picasso.load(anEvent.getThumbImage())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mImage);
        }

        holder.eView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
//                intent.putExtra("eventID", modelSDTO.getModelDTOList().get(position).getModel().getId() + "");
                intent.putExtra("eventID", sEventObject.getEventList().get(position).getId() + "");
                context.startActivity(intent);
            }
        });

    }

    //get item count
    @Override
    public int getItemCount() {
        List<Event> listE = sEventObject.getEventList();
        return listE.size();
    }
}
