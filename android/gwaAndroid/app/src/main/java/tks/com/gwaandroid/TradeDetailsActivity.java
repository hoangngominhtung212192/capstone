package tks.com.gwaandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomSlideAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.dialog.SendOrderDialog;
import tks.com.gwaandroid.dialog.UpdateQuantityDialog;
import tks.com.gwaandroid.model.SendOrderRequest;
import tks.com.gwaandroid.model.TradeDetailsResponse;
import tks.com.gwaandroid.model.Tradepost;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class TradeDetailsActivity extends AppCompatActivity
        implements SendOrderDialog.SendOrderDialogListener,
        UpdateQuantityDialog.UpdateQuantityDialogListener {

    ViewPager image_slider;
    LinearLayout slider_indicator;
    int numberOfImages;
    ImageView[] indicator_items;
    CustomSlideAdapter customSlideAdapter;

    String[] image_urls;
    Tradepost tradepostDetails;
    int totalOrder;

    //GET EXTRA
    int tradepostId;

    //TextView For details
    TextView tv_title, tv_price, tv_raters, tv_owner, tv_type, tv_condition, tv_brand, tv_model,
            tv_quantity, tv_location, tv_datePosted;
    //Rating bar
    RatingBar ratingBar;

    //button
    Button btn_send_order, btn_manage_order, btn_manage_update_quantity, btn_manage_edit, btn_manage_delete;

    //TEXT NOTI TRADEPOST STATUS
    TextView txt_status;

    //get Accoutn info
    private SharedPreferences sharedPreferences;
    int accountId;

    //preview Intent
    Intent preIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_details);
        setTitle("TRADE DETAILS");
        switch (getIntent().getStringExtra("CALLINGACTIVITY")){
            case "TRADING":
                preIntent = new Intent(TradeDetailsActivity.this, TradingActivity.class);
                break;
            case "MYTRADE":
                preIntent = new Intent(TradeDetailsActivity.this, MyTradeActivity.class);
                break;
        }





        //get AccountID
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accountId = sharedPreferences.getInt("ACCOUNTID", 0);

        //Get extra
        tradepostId = getIntent().getIntExtra("TRADEPOSTID", 0);

        //init image slider
        image_slider = (ViewPager) findViewById(R.id.trade_details_slide);
        slider_indicator = (LinearLayout) findViewById(R.id.slide_indicator);

        //init tradepost details
        tv_title = (TextView) findViewById(R.id.trade_details_title);
        tv_price = (TextView) findViewById(R.id.trade_details_price);
        tv_raters = (TextView) findViewById(R.id.trade_details_raters);
        tv_owner = (TextView) findViewById(R.id.trade_details_username);
        tv_type = (TextView) findViewById(R.id.trade_details_type);
        tv_condition = (TextView) findViewById(R.id.trade_details_condition);
        tv_brand = (TextView) findViewById(R.id.trade_details_brand);
        tv_model = (TextView) findViewById(R.id.trade_details_model);
        tv_quantity = (TextView) findViewById(R.id.trade_details_quantity);
        tv_location = (TextView) findViewById(R.id.trade_details_address);
        tv_datePosted = (TextView) findViewById(R.id.trade_details_postedDate);

        ratingBar = (RatingBar) findViewById(R.id.trade_details_rating);

        btn_send_order = (Button) findViewById(R.id.btn_send_order);
        btn_manage_order = (Button) findViewById(R.id.btn_manage_order);
        btn_manage_update_quantity = (Button) findViewById(R.id.btn_manage_update_quantity);
        btn_manage_edit = (Button) findViewById(R.id.btn_manage_edit);
        btn_manage_delete = (Button) findViewById(R.id.btn_manage_delete);
        txt_status = (TextView) findViewById(R.id.txt_tradedetails_status);


        loadTradepostData();


    }

    private void loadImageSlider() {

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


    private void loadTradepostData() {

        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<TradeDetailsResponse> trademarketAPICall = trademarketAPI.getTradeDetails(tradepostId);
        trademarketAPICall.enqueue(new Callback<TradeDetailsResponse>() {
            @Override
            public void onResponse(Call<TradeDetailsResponse> call, Response<TradeDetailsResponse> response) {
                TradeDetailsResponse result = response.body();
                image_urls = result.getImages();
                loadImageSlider();
                totalOrder = result.getTotalOrder();
                tradepostDetails = result.getTradepost();
                bindingTradepostDetails();

            }

            @Override
            public void onFailure(Call<TradeDetailsResponse> call, Throwable t) {
                Log.d("APITRADEDETAILS", "onFailure: " + t.getMessage());

            }
        });


    }

    private void bindingTradepostDetails() {
        tv_title.setText(tradepostDetails.getTitle());
        String price = tradepostDetails.getPrice() + "$";
        if (tradepostDetails.getPriceNegotiable() == 1) {
            price += " (Negotiable)";
        }
        tv_price.setText(price);
        tv_raters.setText("(" + tradepostDetails.getNumberOfRater() + ")");
        tv_owner.setText(tradepostDetails.getAccount().getUsername());
        if (tradepostDetails.getTradeType() == AppConstant.INT_TYPE_SELL) {
            tv_type.setText("Want to SELL");
        } else {
            tv_type.setText("Want to BUY");
        }
        if (tradepostDetails.getCondition() == AppConstant.INT_TRADE_CONDITION_NEW) {
            tv_condition.setText("New");
        } else {
            tv_condition.setText("Used");
        }
        tv_brand.setText(tradepostDetails.getBrand());
        if (tradepostDetails.getModel().equals("")) {
            tv_model.setText("N/A");
        } else {
            tv_model.setText(tradepostDetails.getModel());
        }
        String quantity = "OUT OF STOCK";
        if (tradepostDetails.getQuantity() > 0) {
            quantity = tradepostDetails.getQuantity() + "";

        } else {
            btn_send_order.setEnabled(false);
            btn_send_order.setBackgroundColor((int) R.color.common_google_signin_btn_text_light_disabled);
            btn_send_order.setText("OUT OF STOCK");
        }
        tv_quantity.setText(quantity);
        tv_location.setText(tradepostDetails.getLocation());
        tv_datePosted.setText(tradepostDetails.getPostedDate());

        //Rating bar
        Float rating = (tradepostDetails.getNumberOfStar() * 1.0f) / (tradepostDetails.getNumberOfRater());
        ratingBar.setRating(rating);

        //Check tradepost Owner
        if (accountId == tradepostDetails.getAccount().getId()) {
            //remove button send order
            btn_send_order.setVisibility(View.GONE);
            //add text status
            txt_status.setVisibility(View.VISIBLE);

            String status = tradepostDetails.getApprovalStatus();

            //Check status is pending approve
            if (status.equals(AppConstant.PENDING_STATUS)) {
                txt_status.setText("TRADE POST IS PENDING APPROVAL");
                btn_manage_edit.setVisibility(View.VISIBLE);
                btn_manage_delete.setVisibility(View.VISIBLE);
            }
            //check status is declined
            if (status.equals(AppConstant.DECLINED_STATUS)) {
                txt_status.setText("TRADE POST IS REJECTED");
                btn_manage_edit.setVisibility(View.VISIBLE);
                btn_manage_delete.setVisibility(View.VISIBLE);
            }
            //check status is approve
            if (status.equals(AppConstant.APPROVED_STATUS)) {
                //Check trade post is no trading activity
                if (totalOrder < 1) {
                    txt_status.setText("NO TRADING ACTIVITY");
                    btn_manage_edit.setVisibility(View.VISIBLE);
                    btn_manage_delete.setVisibility(View.VISIBLE);
                } else {
                    txt_status.setText("TRADE POST IS TRADING");
                    btn_manage_order.setVisibility(View.VISIBLE);
                    btn_manage_update_quantity.setVisibility(View.VISIBLE);
                }
            }
        }

    }


    public void viewDescription(View view) {
    }

    public void getDirection(View view) {
        Intent intent = new Intent(TradeDetailsActivity.this, GetDirectionActivity.class);
        intent.putExtra("LOCATION", tradepostDetails.getLocation());
        startActivity(intent);
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(TradeDetailsActivity.this, ProfileActivity.class);
        intent.putExtra("PROFILEID", tradepostDetails.getAccount().getId());
        startActivity(intent);
    }

    public void clickToOpenSendOrderDialog(View view) {
        SendOrderDialog sendOrderDialog = new SendOrderDialog();
        Bundle args = new Bundle();
        args.putInt("QUANTITYSTOCK", tradepostDetails.getQuantity());
        args.putInt("LOGINACCOUNT", accountId);
        args.putInt("TRADEPOSTID", tradepostDetails.getId());
        sendOrderDialog.setArguments(args);
        sendOrderDialog.show(getSupportFragmentManager(), "Send order Dialog");
    }

    public void clickToManageOrder(View view) {
        Intent intent = new Intent(TradeDetailsActivity.this, ManageOrderActivity.class);
        intent.putExtra("TRADEPOSTID", tradepostId);
        startActivity(intent);
    }

    public void clickToOpenUpdateQuantityDialog(View view) {
        UpdateQuantityDialog updateQuantityDialog = new UpdateQuantityDialog();
        Bundle args = new Bundle();
        args.putInt("QUANTITYSTOCK", tradepostDetails.getQuantity());
        args.putInt("TRADEPOSTID", tradepostDetails.getId());
        updateQuantityDialog.setArguments(args);
        updateQuantityDialog.show(getSupportFragmentManager(), "Update Quantity Dialog");
    }

    public void clickToDeleteTrade(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete this trade?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
                        Call<String> call = trademarketAPI.deleteTradepost(tradepostId);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Toast.makeText(TradeDetailsActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                startActivityForResult(preIntent, 1);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("DELETETRADEAPI", "onFailure: " + t.getMessage());
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public void clickToEditTrade(View view) {
    }

    @Override
    public void onSendOrderDialogConfirm(SendOrderRequest sendOrderData) {

        //CAll api
        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
        Call<String> call = trademarketAPI.sendOrder(sendOrderData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(TradeDetailsActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("SENDORDERAPI", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onUpdateQuantityDialogConfirm(int tradepostId, int newQuantity) {
        if (newQuantity == tradepostDetails.getQuantity()){
            Toast.makeText(this, "New quanity is equal stock quantity", Toast.LENGTH_SHORT).show();
            return;
        }
        //Call Api
        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance().create(TrademarketAPI.class);
        Call<String> call = trademarketAPI.updateQuantity(tradepostId,newQuantity);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(TradeDetailsActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("UPDATEQUANTITYAPI", "onFailure: " + t.getMessage());
            }
        });
    }

}
