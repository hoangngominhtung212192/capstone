package tks.com.gwaandroid;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomMyOrderAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.MyOrderDataResponse;
import tks.com.gwaandroid.model.MyOrderModel;
import tks.com.gwaandroid.network.RetrofitClientInstance;
import tks.com.gwaandroid.utils.BottomNavigationViewHelper;

public class MyOrderActivity extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 12;
    private String clickPhoneNumber;

    BottomNavigationView bottomNavigationView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    int currentPage, totalPage, sortType, accountId;
    String status;

    RecyclerView myorder_list;
    TextView empty_text;
    CustomMyOrderAdapter adapter;
    List<MyOrderModel> myOrderData;
    int currentVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setTitle("MY ORDER");

        //Init
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accountId = sharedPreferences.getInt("ACCOUNTID", 0);
        status = AppConstant.APPROVED_STATUS;
        currentPage = 1;
        sortType = AppConstant.SORT_DATE_DESC;
        currentVisiblePosition = 0;

        myOrderData = new ArrayList<MyOrderModel>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.myorder_nav);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_myorder_payment:
                        status = AppConstant.APPROVED_STATUS;
                        break;
                    case R.id.nav_myorder_peding:
                        status = AppConstant.PENDING_STATUS;
                        break;
                    case R.id.nav_myorder_succeed:
                        status = AppConstant.SUCCEED_STATUS;
                        break;
                    case R.id.nav_myorder_rejected:
                        status = "others";
                        break;
                }
                currentPage = 1;
                currentVisiblePosition = 0;
                myOrderData = new ArrayList<MyOrderModel>();
                loadMyOrderData();
                return true;
            }
        });

        myorder_list = findViewById(R.id.list_myorder);
        myorder_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPage) {
                        currentVisiblePosition = ((LinearLayoutManager) myorder_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        // a little bit delay
                        currentPage++;
                        loadMyOrderData();
                    }
                }
            }
        });


        //load Data
        loadMyOrderData();


        // left menu
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.myprofile) {
                    Intent intent = new Intent(MyOrderActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Intent intent = new Intent(MyOrderActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(MyOrderActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(MyOrderActivity.this, EventActivity.class);
                    startActivity(intent);
                } else if (id == R.id.exchange) {
                    Intent intent = new Intent(MyOrderActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(MyOrderActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyOrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
        // end left menu
    }

    private void loadMyOrderData() {
        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<MyOrderDataResponse> call = trademarketAPI.getMyOrderData(accountId, status, currentPage, sortType);

        call.enqueue(new Callback<MyOrderDataResponse>() {
            @Override
            public void onResponse(Call<MyOrderDataResponse> call, Response<MyOrderDataResponse> response) {
                MyOrderDataResponse result = response.body();
                totalPage = result.getTotalPage();
                List<MyOrderModel> pageData = result.getData();
                myOrderData.addAll(pageData);
                renderMyOrderData(myOrderData);

            }

            @Override
            public void onFailure(Call<MyOrderDataResponse> call, Throwable t) {
                Log.d("MYORDERAPI", "onFailure: " + t.getMessage());
            }
        });
    }

    private void renderMyOrderData(List<MyOrderModel> myOrderData) {
        empty_text = (TextView) findViewById(R.id.txt_empty_myorder);

        if (myOrderData == null || myOrderData.size() < 1) {
            empty_text.setText("EMPTY");
            empty_text.setVisibility(View.VISIBLE);
            myorder_list.setVisibility(View.GONE);
        } else {
            myorder_list.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.GONE);
            adapter = new CustomMyOrderAdapter(myOrderData, MyOrderActivity.this);

            //Handle click to call from dialog contact info
            adapter.setPhoneCallListener(new CustomMyOrderAdapter.OnPhoneCallListener() {
                @Override
                public void onPhoneCallClick(String phoneNumber) {
                    clickPhoneNumber = phoneNumber;
                    callPhoneNumber(clickPhoneNumber);

                }
            });


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this);
            myorder_list.setLayoutManager(layoutManager);
            myorder_list.setAdapter(adapter);

            // scroll to the last position
            ((LinearLayoutManager) myorder_list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        }

    }

    private void callPhoneNumber(String phoneNumber){
        if (ActivityCompat.checkSelfPermission(MyOrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MyOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
                    callPhoneNumber(clickPhoneNumber);
                }
                else
                {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
