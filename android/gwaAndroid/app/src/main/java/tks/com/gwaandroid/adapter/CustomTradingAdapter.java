package tks.com.gwaandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import tks.com.gwaandroid.R;
import tks.com.gwaandroid.TradeDetailsActivity;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.TradingModel;

public class CustomTradingAdapter extends RecyclerView.Adapter<CustomTradingAdapter.ViewHolder>{
    private static final String TAG = "CustomTradingAdapter";

    private List<TradingModel> models;
    private Context mContext;

    public CustomTradingAdapter(List<TradingModel> models, Context mContext) {
        this.models = models;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView trading_image;
        TextView trading_price,trading_type,trading_condition, trading_title, trading_raters ;
        RatingBar trading_rating;
        View list_trading;

        public ViewHolder(View itemView) {
            super(itemView);
            trading_image = (ImageView) itemView.findViewById(R.id.item_trading_image);
            trading_title =  (TextView) itemView.findViewById(R.id.item_trading_title);
            trading_price =  (TextView) itemView.findViewById(R.id.item_trading_price);
            trading_type =  (TextView) itemView.findViewById(R.id.item_trading_type);
            trading_raters = (TextView) itemView.findViewById(R.id.item_trading_raters);
            trading_condition =  (TextView) itemView.findViewById(R.id.item_trading_condition);
            trading_rating = (RatingBar) itemView.findViewById(R.id.item_trading_rating);
            list_trading = itemView;

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_trading, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Called");
        Log.d(TAG, "onBindViewHolder: " + models.size());
        final TradingModel model = models.get(position);

        Picasso picasso = Picasso.get();
        if (model.getThumbnail().contains("localhost:8080")) {
            String imageUrl = model.getThumbnail().replace("localhost", AppConstant.HOST_NAME);
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.trading_image);
        } else {
            picasso.load(model.getThumbnail())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(holder.trading_image);
        }

        holder.trading_title.setText(model.getTradepost().getTitle());
        String price = model.getTradepost().getPrice() + "$";
        if (model.getTradepost().getPriceNegotiable() == 1){
            price+= " (*)";
        }
        holder.trading_price.setText(price);


        if(model.getTradepost().getCondition() == AppConstant.INT_TRADE_CONDITION_NEW){
            holder.trading_condition.setText("NEW");

        }else {
            holder.trading_condition.setText("USED");
        }

        if(model.getTradepost().getTradeType() == AppConstant.INT_TYPE_SELL){
            holder.trading_type.setText("SELL");

        }else {
            holder.trading_type.setText("BUY");
            holder.trading_type.setTextColor((int) R.color.buying);
        }
        Log.d(TAG, "onBindViewHolder: numberRater=" + model.getTradepost().getNumberOfRater());
        holder.trading_raters.setText("(" +model.getTradepost().getNumberOfRater()+ ")");

        Float rating = (model.getTradepost().getNumberOfStar() * 1.0f) / ( model.getTradepost().getNumberOfRater() * 1.0f);

        holder.trading_rating.setRating(rating);



        holder.list_trading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TradeDetailsActivity.class);
                intent.putExtra("TRADEPOSTID", model.getTradepost().getId());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
