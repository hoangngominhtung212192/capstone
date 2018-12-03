package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.api.EventAPI;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class EventRegisterActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private RelativeLayout linearProgressBar;

    private TextView name, date, remaining;
    private EditText mytickets, mycard;
//    private RelativeLayout layout_star_rating;

    private SharedPreferences sharedPreferences;
    private int eventid;
    private int userid;
    private int rem;

    private boolean checkmycard = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);

        // initialize
        initialize();

//        name = (TextView) findViewById(R.id.lbl2);
        date = (TextView) findViewById(R.id.lbl3);
        String dateS = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        date.setText("Today is " + dateS);
        remaining = (TextView) findViewById(R.id.lbl4);
        mytickets = (EditText) findViewById(R.id.myticket);
        mycard = (EditText) findViewById(R.id.mycard);
        userid = sharedPreferences.getInt("ACCOUNTID", 0);
        // get eventid from intent params
        Intent intent = getIntent();
        eventid = Integer.parseInt(intent.getStringExtra("eventID"));

        // request api
        // a little bit delay
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        requestApi(eventid);
                    }
                },
                300);
    }

    private void initialize() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBarDetail);
        // left menu
        dl = (DrawerLayout) findViewById(R.id.dl_register_event);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_model_detail);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.myprofile) {
                    Intent intent = new Intent(EventRegisterActivity.this, ProfileActivity.class);
                    EventRegisterActivity.this.startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(EventRegisterActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(EventRegisterActivity.this, MainActivity.class);
                    EventRegisterActivity.this.startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(EventRegisterActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(EventRegisterActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Toast.makeText(EventRegisterActivity.this, "Exchange", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(EventRegisterActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventRegisterActivity.this, LoginActivity.class);
                    EventRegisterActivity.this.startActivity(intent);
                    finish();
                }

                return true;
            }
        });
        // end left menu

    }

    private void checkCard(){
//        boolean check = false;
        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        String cardString = mycard.getText().toString();
        Call<Boolean> call = eventAPI.checkCard(cardString);

        // execute request
        call.enqueue(new Callback<Boolean>() {
            // get json response
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    if (response.body() == true){
                        checkmycard = true;
                    } else {
                        checkmycard = false;
                    }

                } else {
                    Toast.makeText(EventRegisterActivity.this, "Cannot check card", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private boolean validate(){
        boolean check = true;
        int mytic = 0;
        if (mytickets.getText().toString().length() == 0){
            check = false;
            mytickets.setError("Please enter number of tickets!");
        } else if(Integer.parseInt(mytickets.getText().toString()) <= 0) {
            check = false;
            mytickets.setError("Number of ticket must be a positive number");
            } else {
            mytic = Integer.parseInt(mytickets.getText().toString());
        }

        if (mytic>rem){
            check = false;
            mytickets.setError("Number of tickets can not be higher than the remaining tickets!");
        }
        checkCard();
        if (checkmycard == false){
            check = false;
            mycard.setError("Card does not exist!");
        }
        return check;
    }

    public void submitReg(View view){
        if (validate()){
            int amount = Integer.parseInt(mytickets.getText().toString());

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int eventidI = eventid;
            System.out.println("evnt id is"+eventidI);
            requestAPIReg(eventidI, userid, amount, date);
        }
    }

    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void requestApi(int id) {

        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        Call<Event> call = eventAPI.getEventDetail(id);

        // execute request
        call.enqueue(new Callback<Event>() {
            // get json response
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if (response.isSuccessful()) {
                    bindingData(response.body());

                } else {
                    Toast.makeText(EventRegisterActivity.this, "Cannot get event eventid", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void requestAPIReg(int eventid, int userid, int amount, String date){
        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        Call<String> call = eventAPI.registerEvent(eventid, userid, amount, date);

        // execute request
        call.enqueue(new Callback<String>() {
            // get json response
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(EventRegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();
                    redirectToEvent();

                } else {
                    Toast.makeText(EventRegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void bindingData(Event result) {

//        .setText(result.getTitle());
        int rmn = result.getMaxAttendee() - result.getNumberOfAttendee();
        String remainString = "Remaining tickets: " + rmn;
        rem = rmn;
        remaining.setText(remainString);

        linearProgressBar.setVisibility(View.GONE);
    }
    public void redirectToEvent(){

        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("eventID", eventid + "");
        startActivity(intent);
    }

}
