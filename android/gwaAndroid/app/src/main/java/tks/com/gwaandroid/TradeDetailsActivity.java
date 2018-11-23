package tks.com.gwaandroid;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomSlideAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.model.TradeDetailsResponse;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class TradeDetailsActivity extends AppCompatActivity {

    ViewPager image_slider;
    LinearLayout slider_indicator;
    int numberOfImages;
    ImageView[] indicator_items;
    CustomSlideAdapter customSlideAdapter;

    String[] image_urls;

    int tradepostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_details);

        //Get extra String
        tradepostId = getIntent().getIntExtra("TRADEPOSTID",0);

        //init
        image_slider = (ViewPager) findViewById(R.id.trade_details_slide);
        slider_indicator = (LinearLayout) findViewById(R.id.slide_indicator);


        loadTradingData();


    }
    private void loadImageSlider(){

        customSlideAdapter = new CustomSlideAdapter(TradeDetailsActivity.this, image_urls);

        image_slider.setAdapter(customSlideAdapter);

        numberOfImages = customSlideAdapter.getCount();

        indicator_items = new ImageView[numberOfImages];

        for (int i = 0; i < numberOfImages; i++) {
            indicator_items[i] = new ImageView(this);
            if (i == 0) {
                indicator_items[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            } else {
                indicator_items[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            slider_indicator.addView(indicator_items[i], params);
        }

        image_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < numberOfImages; i++) {
                    if (i == position) {
                        indicator_items[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    } else {
                        indicator_items[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void loadTradingData() {

        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<TradeDetailsResponse> trademarketAPICall = trademarketAPI.getTradeDetails(tradepostId);
        trademarketAPICall.enqueue(new Callback<TradeDetailsResponse>() {
            @Override
            public void onResponse(Call<TradeDetailsResponse> call, Response<TradeDetailsResponse> response) {
                TradeDetailsResponse result = response.body();
                image_urls = result.getImages();
                loadImageSlider();

            }

            @Override
            public void onFailure(Call<TradeDetailsResponse> call, Throwable t) {
                Log.d("APITRADEDETAILS", "onFailure: " + t.getMessage());

            }
        });


    }


    public void viewDescription(View view) {
    }

    public void getDirection(View view) {
        Intent intent = new Intent(TradeDetailsActivity.this, GetDirectionActivity.class);
        startActivity(intent);
    }

    public void viewProfile(View view) {
    }
}
