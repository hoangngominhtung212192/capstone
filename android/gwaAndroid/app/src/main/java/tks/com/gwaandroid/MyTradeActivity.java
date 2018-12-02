package tks.com.gwaandroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
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
import tks.com.gwaandroid.adapter.CustomMyTradeAdapter;
import tks.com.gwaandroid.api.TrademarketAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.MyTradeDataResponse;
import tks.com.gwaandroid.model.MyTradeModel;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class MyTradeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    int currentPage, totalPage, sortType, accountId;
    String status;

    RecyclerView mytrade_list;
    TextView empty_text;
    CustomMyTradeAdapter adapter;
    List<MyTradeModel> mytradeData;
    int currentVisiblePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trade);
        setTitle("MY TRADE");


        //Init
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accountId = sharedPreferences.getInt("ACCOUNTID", 0);
        status = AppConstant.APPROVED_STATUS;
        currentPage = 1;
        sortType = AppConstant.SORT_DATE_DESC;
        currentVisiblePosition = 0;

        mytradeData = new ArrayList<MyTradeModel>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.mytrade_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_mytrade_ontrading:
                        status = AppConstant.APPROVED_STATUS;
                        break;
                    case R.id.nav_mytrade_pending:
                        status = AppConstant.PENDING_STATUS;
                        break;
                    case R.id.nav_mytrade_rejected:
                        status = AppConstant.DECLINED_STATUS;
                        break;
                }
                currentPage = 1;
                currentVisiblePosition = 0;
                mytradeData = new ArrayList<MyTradeModel>();
                loadMyTradeData();
                return true;
            }
        });

        mytrade_list = findViewById(R.id.list_mytrade);
        mytrade_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < totalPage) {
                        currentVisiblePosition = ((LinearLayoutManager) mytrade_list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        // a little bit delay
                        currentPage++;
                        loadMyTradeData();
                    }
                }
            }
        });

        //load Data
        loadMyTradeData();


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
                    Intent intent = new Intent(MyTradeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(MyTradeActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(MyTradeActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(MyTradeActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(MyTradeActivity.this, EventActivity.class);
                    startActivity(intent);
                } else if (id == R.id.exchange) {
                    Intent intent = new Intent(MyTradeActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(MyTradeActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyTradeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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


    private void loadMyTradeData() {
        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<MyTradeDataResponse> trademarketAPICall = trademarketAPI.getMyTradeData(accountId,status,currentPage,sortType);

        trademarketAPICall.enqueue(new Callback<MyTradeDataResponse>() {
            @Override
            public void onResponse(Call<MyTradeDataResponse> call, Response<MyTradeDataResponse> response) {
                MyTradeDataResponse result = response.body();
                totalPage = result.getTotalPage();
                Log.d("MYTRADEAPI", "totalPage: " + totalPage);
                Log.d("MYTRADEAPI", "listSize: " + result.getData().size());
                List<MyTradeModel> pageData = result.getData();
                mytradeData.addAll(pageData);
                renderMyTradeData(mytradeData);
            }

            @Override
            public void onFailure(Call<MyTradeDataResponse> call, Throwable t) {
                Log.d("MYTRADEAPI", "onFailure: " + t.getMessage());
            }
        });

    }

    private void renderMyTradeData(List<MyTradeModel> mytradeData) {

        empty_text = (TextView) findViewById(R.id.txt_empty_mytrade);

        if(mytradeData == null || mytradeData.size() < 1){
            empty_text.setText("EMPTY");
            empty_text.setVisibility(View.VISIBLE);
            mytrade_list.setVisibility(View.GONE);
        }else {
            mytrade_list.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.GONE);
            adapter = new CustomMyTradeAdapter(mytradeData, MyTradeActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyTradeActivity.this);
            mytrade_list.setLayoutManager(layoutManager);
            mytrade_list.setAdapter(adapter);

            // scroll to the last position
            ((LinearLayoutManager) mytrade_list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            loadMyTradeData();
        }
    }
}
