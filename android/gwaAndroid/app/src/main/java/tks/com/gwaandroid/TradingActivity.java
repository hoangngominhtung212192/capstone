package tks.com.gwaandroid;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    Location myLocation = null;
    String myAddress, locationSearch, searchKeyword;
    long searchRange;
    LinearLayout search_noti_wrap;
    TextView search_noti_keyword,search_noti_range;

    boolean isSearch, isSearchLocation;

    BottomNavigationView bottomNavigationView;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private SharedPreferences sharedPreferences;

    int currentPage, totalPage, sortType;
    String tradeType;

    RecyclerView trading_list;
    TextView empty_text;
    CustomTradingAdapter adapter;
    List<TradingModel> tradingData;
    int currentVisiblePosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);
        setTitle("TRADING");


        //Init
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tradeType = AppConstant.TYPE_ALL;
        currentPage = 1;
        sortType = AppConstant.SORT_DATE_DESC;
        currentVisiblePosition = 0;

        //For search
        isSearch = false;
        isSearchLocation = false;
        search_noti_wrap = (LinearLayout) findViewById(R.id.trading_search_noti);
        search_noti_keyword = (TextView) findViewById(R.id.trading_search_keyword);
        search_noti_range = (TextView) findViewById(R.id.trading_search_range);

        tradingData = new ArrayList<TradingModel>();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.trading_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
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
                if (isSearch) {
                    if (isSearchLocation) {
                        loadSearchTradingWithLocationData();

                    } else {
                        loadSearchTradingData();
                    }
                }else {
                    loadTradingData();
                }

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
                    Intent intent = new Intent(TradingActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(TradingActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(TradingActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(TradingActivity.this, EventActivity.class);
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
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearchDialog();
                return true;
            case R.id.action_post:

                return true;

            default:
                return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

        }
//        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void openSearchDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_search_location, null);
        final EditText txt_search = (EditText) view.findViewById(R.id.txt_trading_search);
        final Switch locationSwitch = (Switch) view.findViewById(R.id.switch_location);
        final LinearLayout location_wrap = (LinearLayout) view.findViewById(R.id.search_location_wrap);
        final TextView txt_currentLocation = (TextView) view.findViewById(R.id.location_search_address);
        final TextView txt_range = (TextView) view.findViewById(R.id.location_search_range);
        final SeekBar seek_range = (SeekBar) view.findViewById(R.id.seek_range);
        seek_range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    seek_range.setProgress(1);
                }
                txt_range.setText(progress + " KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    location_wrap.setVisibility(View.VISIBLE);

                    //Get My Location
                    askPermissionsAndGetMyLocation();
                    txt_currentLocation.setText(myAddress);


                } else {
                    location_wrap.setVisibility(View.GONE);
                }
            }
        });

        if(isSearch){
            txt_search.setText(searchKeyword);
        }
        if(isSearchLocation){
            txt_search.setText(searchKeyword);
            locationSwitch.setChecked(true);
            location_wrap.setVisibility(View.VISIBLE);
            txt_currentLocation.setText(myAddress);
            seek_range.setProgress((int)searchRange/1000);
            txt_range.setText(seek_range.getProgress() + " KM");
        }

        builder.setView(view)
                .setTitle("Search in trading")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchKeyword = txt_search.getText().toString();
                        if (searchKeyword.isEmpty()) {
                            Toast.makeText(TradingActivity.this, "Please enter keyword", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentVisiblePosition = 0;
                        currentPage = 1;
                        if (locationSwitch.isChecked()) {
                            isSearch =true;
                            isSearchLocation = true;
                            searchRange = seek_range.getProgress() * 1000; //Convert km -> m
                            locationSearch = myLocation.getLatitude() + "," + myLocation.getLongitude();
                            tradingData.clear();
                            loadSearchTradingWithLocationData();

                            //Show noti
                            search_noti_wrap.setVisibility(View.VISIBLE);
                            search_noti_keyword.setText("Search with keyword: "+searchKeyword);
                            search_noti_keyword.setVisibility(View.VISIBLE);
                            search_noti_range.setText("Range: " + seek_range.getProgress() + " KM");
                            search_noti_range.setVisibility(View.VISIBLE);

                        } else {
                            isSearchLocation = false;
                            isSearch = true;
                            tradingData.clear();
                            loadSearchTradingData();

                            //Show noti
                            search_noti_wrap.setVisibility(View.VISIBLE);
                            search_noti_keyword.setText("Search with keyword: "+searchKeyword);
                            search_noti_keyword.setVisibility(View.VISIBLE);
                            search_noti_range.setVisibility(View.GONE);
                        }


                    }
                })
                .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isSearch || isSearchLocation){

                            isSearch = false;
                            isSearchLocation =false;

                            tradingData.clear();
                            currentVisiblePosition = 0;
                            currentPage = 1;
                            loadTradingData();

                            //Hide noti
                            search_noti_wrap.setVisibility(View.GONE);
                        }

                    }
                });
        builder.create().show();


    }

    private void askPermissionsAndGetMyLocation() {


        // Với API >= 23, bạn phải hỏi người dùng cho phép xem vị trí của họ.
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Các quyền cần người dùng cho phép.
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};

                // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                return;
            }
        }

        // Hiển thị vị trí hiện thời trên bản đồ.
        this.getMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {

                // Chú ý: Nếu yêu cầu bị bỏ qua, mảng kết quả là rỗng.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();


                    this.getMyLocation();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // Tìm một nhà cung cấp vị trị hiện thời đang được mở.
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        // Tiêu chí để tìm một nhà cung cấp vị trí.
        Criteria criteria = new Criteria();

        // Tìm một nhà cung vị trí hiện thời tốt nhất theo tiêu chí trên.
        // ==> "gps", "network",...
        String bestProvider = locationManager.getBestProvider(criteria, true);

        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show();
            Log.i("GETDIRECTION", "No location provider enabled!");
            return null;
        }
        return bestProvider;
    }


    private void getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {
            return;
        }

        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;


        try {
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

            // Lấy ra vị trí.
            myLocation = locationManager
                    .getLastKnownLocation(locationProvider);
        }
        // Với Android API >= 23 phải catch SecurityException.
        catch (SecurityException e) {
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("GETDIRECTION", "Show My Location Error:" + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {
            //Geocode My Location to Address text
            Geocoder geocoder = new Geocoder(TradingActivity.this, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);
                myAddress = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                Toast.makeText(TradingActivity.this, "Can't get your address line", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
            Log.i("GETDIRECTION", "Location not found");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
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
                Log.d("TRADINGAPI", "totalPage: " + totalPage);
                Log.d("TRADINGAPI", "listSize: " + result.getData().size());
                List<TradingModel> pageData = result.getData();
                tradingData.addAll(pageData);
                renderTradingData(tradingData);
            }

            @Override
            public void onFailure(Call<TradingDataResponse> call, Throwable t) {
                Log.d("TRADINGAPI", "onFailure: " + t.getMessage());

            }
        });

    }

    private void loadSearchTradingData() {

        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<TradingDataResponse> trademarketAPICall = trademarketAPI.getSearchTradingData(tradeType, currentPage, sortType, searchKeyword);

        trademarketAPICall.enqueue(new Callback<TradingDataResponse>() {
            @Override
            public void onResponse(Call<TradingDataResponse> call, Response<TradingDataResponse> response) {
                TradingDataResponse result = response.body();
                totalPage = result.getTotalPage();
                List<TradingModel> pageData = result.getData();
                tradingData.addAll(pageData);
                renderTradingData(tradingData);
            }

            @Override
            public void onFailure(Call<TradingDataResponse> call, Throwable t) {
                Log.d("SEARCHTRAINGAPI", "onFailure: " + t.getMessage());

            }
        });

    }

    private void loadSearchTradingWithLocationData() {

        TrademarketAPI trademarketAPI = RetrofitClientInstance.getRetrofitInstance()
                .create(TrademarketAPI.class);

        Call<TradingDataResponse> trademarketAPICall = trademarketAPI.getSearchTradingWithLocationData(tradeType,sortType,searchKeyword,locationSearch,searchRange);

        trademarketAPICall.enqueue(new Callback<TradingDataResponse>() {
            @Override
            public void onResponse(Call<TradingDataResponse> call, Response<TradingDataResponse> response) {
                TradingDataResponse result = response.body();
                totalPage = result.getTotalPage();
                List<TradingModel> pageData = result.getData();
                tradingData.addAll(pageData);
                renderTradingData(tradingData);
            }

            @Override
            public void onFailure(Call<TradingDataResponse> call, Throwable t) {
                Log.d("SEARCHTRAINGAPI", "onFailure: " + t.getMessage());

            }
        });

    }

    private void renderTradingData(List<TradingModel> tradingData) {
        empty_text = (TextView) findViewById(R.id.txt_empty_trading);

        if (tradingData == null || tradingData.size() < 1) {
            empty_text.setText("EMPTY");
            empty_text.setVisibility(View.VISIBLE);
            trading_list.setVisibility(View.GONE);
        } else {
            trading_list.setVisibility(View.VISIBLE);
            empty_text.setVisibility(View.GONE);
            adapter = new CustomTradingAdapter(tradingData, TradingActivity.this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TradingActivity.this, 2);
            trading_list.setLayoutManager(layoutManager);
            trading_list.setAdapter(adapter);

            // scroll to the last position
            ((LinearLayoutManager) trading_list.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            loadTradingData();
        }
    }

}
