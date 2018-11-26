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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tks.com.gwaandroid.R;
import tks.com.gwaandroid.TradeDetailsActivity;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.MyTradeModel;

public class CustomMyTradeAdapter  extends RecyclerView.Adapter<CustomMyTradeAdapter.ViewHolder>{
    private static final String TAG = "CustomMyTradeAdapter";

    private List<MyTradeModel> models;
    private Context mContext;

    public CustomMyTradeAdapter(List<MyTradeModel> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_mytrade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        Log.d(TAG, "onBindViewHolder: " + models.size());
        final MyTradeModel model = models.get(position);

        Picasso picasso = Picasso.get();
        if (model.getThumbnail().contains("localhost:8080")) {
            String imageUrl = model.getThumbnail().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mytrade_image);
        } else {
            picasso.load(model.getThumbnail())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.mytrade_image);
        }

        holder.mytrade_title.setText(model.myTradePost.getTitle());
        String type = "";
        if(model.getMyTradePost().getTradeType() == AppConstant.INT_TYPE_SELL){
            type= "SELL";
        }else {
            type= "BUY";
            holder.mytrade_tradeType.setTextColor(Color.GREEN);
        }
        if(model.getMyTradePost().approvalStatus.equals(AppConstant.DECLINED_STATUS)){
            type= "REJECTED";
            holder.mytrade_tradeType.setTextColor(Color.GRAY);
        }
        if(model.getMyTradePost().approvalStatus.equals(AppConstant.PENDING_STATUS)){
            type= "PENDING";
            holder.mytrade_tradeType.setTextColor(Color.BLUE);
        }
        holder.mytrade_tradeType.setText(type);
        holder.mytrade_request.setText("Requesting: "+ model.numOfPendingRequest);
        holder.mytrade_payment.setText("On payment: "+model.numOfOnPaymentRequest);
        holder.mytrade_succeed.setText("Succeed: "+model.numOfSucceedRequest);

        holder.list_mytrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TradeDetailsActivity.class);
                intent.putExtra("TRADEPOSTID", model.getMyTradePost().getId());
                intent.putExtra("CALLINGACTIVITY", "MYTRADE");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mytrade_image;
        TextView mytrade_title,mytrade_tradeType,mytrade_request,mytrade_payment,mytrade_succeed;
        View list_mytrade;

        public ViewHolder(View itemView) {
            super(itemView);

            mytrade_image = (ImageView) itemView.findViewById(R.id.item_mytrade_image);
            mytrade_title = (TextView) itemView.findViewById(R.id.item_mytrade_title);
            mytrade_tradeType = (TextView) itemView.findViewById(R.id.item_mytrade_tradeType);
            mytrade_request = (TextView) itemView.findViewById(R.id.item_mytrade_request);
            mytrade_payment = (TextView) itemView.findViewById(R.id.item_mytrade_payment);
            mytrade_succeed = (TextView) itemView.findViewById(R.id.item_mytrade_succeed);

            list_mytrade = itemView;

        }
    }
}
