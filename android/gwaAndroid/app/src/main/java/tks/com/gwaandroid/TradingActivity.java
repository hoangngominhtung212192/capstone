package tks.com.gwaandroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomTradingAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.TradingDataResponse;
import tks.com.gwaandroid.model.TradingModel;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class TradingActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    int currentPage, totalPage, sortType;
    String tradeType;

    RecyclerView trading_list;
    CustomTradingAdapter adapter;
    List<TradingModel> tradingData;
    int currentVisiblePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        //Init
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tradeType = AppConstant.TYPE_ALL;
        currentPage = 1;
        sortType = AppConstant.SORT_DATE_DESC;
        currentVisiblePosition = 0;

        tradingData = new ArrayList<TradingModel>();



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.trading_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_trading_all:
                        tradeType = AppConstant.TYPE_ALL;
                        break;
                    case R.id.nav_trading_buy:
                        tradeType = AppConstant.TYPE_BUY;
                        break;
                    case R.id.nav_trading_sell:
                        tradeType = AppConstant.TYPE_SELL;
                        break;
                }
                currentPage = 1;
                currentVisiblePosition = 0;
                tradingData = new ArrayList<TradingModel>();
                loadTradingData();
                return true;
            }
        });


        trading_list = findViewById(R.id.list_trading);
        trading_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPage) {
                        currentVisiblePosition = ((LinearLayoutManager) trading_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        // a little bit delay
                        currentPage++;
                        loadTradingData();
                    }
                }
            }
        });


        //load Data
        loadTradingData();


        // left menu
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.myprofile) {
                    Intent intent = new Intent(TradingActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(TradingActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(TradingActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.exchange) {
                    Intent intent = new Intent(TradingActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(TradingActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TradingActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        // end left menu
    }
    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void loadTradingData() {

        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<TradingDataResponse> trademarketAPICall = trademarketAPI.getTradingData(tradeType, currentPage, sortType);

        trademarketAPICall.enqueue(new Callback<TradingDataResponse>() {
            @Override
            public void onResponse(Call<TradingDataResponse> call, Response<TradingDataResponse> response) {
                TradingDataResponse result = response.body();
                totalPage = result.getTotalPage();
                Log.d("APITRADE", "totalPage: " + totalPage);
                Log.d("APITRADE", "listSize: " + result.getData().size());
                List<TradingModel> pageData = result.getData();
                tradingData.addAll(pageData);
                renderTradingData(tradingData);
            }

            @Override
            public void onFailure(Call<TradingDataResponse> call, Throwable t) {
                Log.d("APITRADE", "onFailure: " + t.getMessage());

            }
        });

    }

    private void renderTradingData(List<TradingModel> tradingData) {
        adapter = new CustomTradingAdapter(tradingData, TradingActivity.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TradingActivity.this, 2);
        trading_list.setLayoutManager(layoutManager);
        trading_list.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) trading_list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }
}
