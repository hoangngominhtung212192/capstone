package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class TrademarketActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trademarket);

        setTitle("TRADE MARKET");

        //Init
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
                    Intent intent = new Intent(TrademarketActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Intent intent = new Intent(TrademarketActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(TrademarketActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(TrademarketActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(TrademarketActivity.this, EventActivity.class);
                    startActivity(intent);
                } else if (id == R.id.exchange) {
                    Intent intent = new Intent(TrademarketActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(TrademarketActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TrademarketActivity.this, LoginActivity.class);
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

    public void openMyOrderActivity(View view) {
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    public void openMyTradeActivity(View view) {
        Intent intent = new Intent(this, MyTradeActivity.class);
        startActivity(intent);
    }

    public void openTradingActivity(View view) {
        Intent intent = new Intent(this, TradingActivity.class);
        startActivity(intent);
    }
}
