package tks.com.gwaandroid;

import android.content.Context;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.api.EventAPI;
import tks.com.gwaandroid.api.ModelAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.Eventattendee;
import tks.com.gwaandroid.model.Model;
import tks.com.gwaandroid.model.ModelDTO;
import tks.com.gwaandroid.model.Modelimage;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class EventDetailActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private RelativeLayout linearProgressBar;

    private TextView title, date, location, price, message1, curAtt, minAtt;
    private WebView content;
    private Button btn;
//    private RelativeLayout layout_star_rating;

    private SharedPreferences sharedPreferences;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // initialize
        initialize();

        // get id from intent params
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("eventID"));
        checkUserIsAttendee();
        // request api
        // a little bit delay
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        requestApi(id);
                    }
                },
                300);
    }

    private void initialize() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBarDetail);
        // left menu
        dl = (DrawerLayout) findViewById(R.id.dl_event_detail);
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
                    Intent intent = new Intent(EventDetailActivity.this, ProfileActivity.class);
                    EventDetailActivity.this.startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(EventDetailActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(EventDetailActivity.this, MainActivity.class);
                    EventDetailActivity.this.startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(EventDetailActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(EventDetailActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Toast.makeText(EventDetailActivity.this, "Exchange", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(EventDetailActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventDetailActivity.this, LoginActivity.class);
                    EventDetailActivity.this.startActivity(intent);
                }

                return true;
            }
        });
        // end left menu

        title = (TextView) findViewById(R.id.eventTitle);
        date = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);
        price = (TextView) findViewById(R.id.price);
        message1 = (TextView) findViewById(R.id.lbl6);
        curAtt = (TextView) findViewById(R.id.currentAttendeeNum);
        minAtt = (TextView) findViewById(R.id.minAttendee);

        content = (WebView) findViewById(R.id.eContent);

        btn = (Button) findViewById(R.id.btnRegEvt);

    }

    private void checkUserIsAttendee(){
        int userid = sharedPreferences.getInt("ACCOUNTID", 0);
        int eventid = id;
        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        Call<Eventattendee> call = eventAPI.getAttendeeInEventAlt(userid, eventid);

        call.enqueue(new Callback<Eventattendee>() {
            // get json response
            @Override
            public void onResponse(Call<Eventattendee> call, Response<Eventattendee> response) {

                if (response.isSuccessful()) {
//                    Toast.makeText(EventDetailActivity.this, "Is an attendee", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.GONE);
                    message1.setVisibility(View.VISIBLE);
                } else {
                    message1.setVisibility(View.GONE);
//                    Toast.makeText(EventDetailActivity.this, "Not an attendee", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<Eventattendee> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
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

                    Log.d("Response Model Detail", response.body().toString());
                } else {
                    Toast.makeText(EventDetailActivity.this, "Cannot get model detail", Toast.LENGTH_SHORT).show();
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

    private void bindingData(Event result) {

        title.setText(result.getTitle());

        String dateS = "Starts on " + result.getStartDate() + " to " + result.getEndDate();
        date.setText(dateS);

        String locationS = "Location: "+result.getLocation().split("@")[0];
        location.setText(locationS);

        String priceS = "Price: "+result.getTicketPrice();
        price.setText(priceS);

        String curAttS = "There are " + result.getNumberOfAttendee() + " people attending this event";
        curAtt.setText(curAttS);

        String minAttS = "Minimum amount of attendee required: " + result.getMinAttendee();
        minAtt.setText(minAttS);


        String contentHtml = result.getContent();
        if (contentHtml.contains("localhost:8080")){
            System.out.println("there is localhost in content");
            contentHtml = contentHtml.replace("localhost",AppConstant.HOST_NAME);
        }
        //append data with global style
        content.loadData("<style>img{display: inline;height: auto !important;max-width: 100% !important;}</style>"+contentHtml, "text/html", "UTF-8");

        linearProgressBar.setVisibility(View.GONE);
    }
    private Context context;

    public void redirectReg(View view){

        Intent intent = new Intent(this, EventRegisterActivity.class);
        intent.putExtra("eventID", id + "");
        startActivity(intent);
    }

}
