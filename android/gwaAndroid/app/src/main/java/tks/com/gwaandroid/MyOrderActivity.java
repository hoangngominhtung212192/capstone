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
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.utils.BottomNavigationViewHelper;

public class MyOrderActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    int currentPage, totalPage, sortType,accountId;
    String status;

    RecyclerView trading_list;
//    CustomTradingAdapter adapter;
//    List<TradingModel> tradingData;
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

//        tradingData = new ArrayList<TradingModel>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.myorder_nav);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
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
//                tradingData = new ArrayList<TradingModel>();
//                loadTradingData();
                return true;
            }
        });

        //load Data
//        loadTradingData();


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
                    Toast.makeText(MyOrderActivity.this, "Notification", Toast.LENGTH_SHORT).show();
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

}
