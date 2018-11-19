package tks.com.gwaandroid;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class TradeMarketActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_market);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.trade_market_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.nav_trading:
                        selectedFragment = TradingFragment.newInstance();
                        break;
                    case R.id.nav_mytrade:
                        selectedFragment = MyTradeFragment.newInstance();
                        break;
                    case R.id.nav_myorder:
                        selectedFragment = MyOrderFragment.newInstance();
                        break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, selectedFragment)
                        .commit();
                return true;
            }
        });
        //set Default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, TradingFragment.newInstance())
                .commit();
    }
}
