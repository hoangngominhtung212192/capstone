package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomEventAdapter;
import tks.com.gwaandroid.api.EventAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Article;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.EventSDTO;
import tks.com.gwaandroid.model.TradingModel;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class EventActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    BottomNavigationView bottomNavigationView;
    private RecyclerView mRecycleView;
    private CustomEventAdapter adapter;
    private RelativeLayout linearProgressBar;
    private int lastPage;
    private int currentPage;
    private String paramTitle = "";
    private String paramSorttype = "desc";
    private String paramStatus = "Active";
    EventSDTO lastSearch ;

    private int currentVisiblePosition = 0;

    private SharedPreferences sharedPreferences;
    private int currentTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // initialize
        currentPage = 1;

        // initialize object EventSDTO : lastSearch
        lastSearch = new EventSDTO();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // get recyclerview
        mRecycleView = findViewById(R.id.erecycleView);

        // display progress bar
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBar);
        linearProgressBar.setVisibility(View.VISIBLE);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.event_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                currentPage = 1;
                currentVisiblePosition = 0;
                switch (menuItem.getItemId()) {
                    case R.id.nav_event_all:
                        lastSearch = new EventSDTO();
                        currentTab = 1;
                        paramStatus = "Active";
                        apiRequest(currentPage);
                        break;
                    case R.id.nav_event_past:
                        lastSearch = new EventSDTO();
                        paramStatus = "Finished";
                        apiRequest(currentPage);
                        currentTab = 3;
                        break;
                    case R.id.nav_my_event:
                        lastSearch = new EventSDTO();
                        apiGetMyEvent(currentPage);
                        currentTab = 2;
                        break;
                }



                return true;
            }
        });


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
                    Intent intent = new Intent(EventActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Intent intent = new Intent(EventActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(EventActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(EventActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(EventActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Intent intent = new Intent(EventActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(EventActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
        // end left menu

// request api and get last page first
        apiRequest(currentPage);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("curPage "+currentPage + " and last page "+lastPage);
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < lastPage) {
                        System.out.println("curPage "+currentPage + " and last page "+lastPage);
                        linearProgressBar.setVisibility(View.VISIBLE);
                        // save scroll last position
                        currentVisiblePosition = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                        // a little bit delay
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        currentPage++;
                                        apiRequest(currentPage);
                                    }
                                },
                                300);

                    }
                }
            }
        });

    }

    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void apiGetMyEvent(int pageNumber){
        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        int accountId = sharedPreferences.getInt("ACCOUNTID", 0);

        Call<EventSDTO> call = eventAPI.getMyListEventAlt(accountId, paramSorttype, pageNumber);
        //execute request
        call.enqueue(new Callback<EventSDTO>() {
            // get json response
            @Override
            public void onResponse(Call<EventSDTO> call, Response<EventSDTO> response) {
                // append data
                if (response.isSuccessful()) {
                    System.out.println("Called my event api");
                    System.out.println("ONRESPONSE total page " + response.body().getTotalPage());
                    System.out.println("ONRESPONSE list size " + response.body().getEventList().size());

                    appendData(response.body());


                    Log.d("Response Event Search", response.body().toString());
                } else {
                    System.out.println("response failed");
                }
            }

            // error
            @Override
            public void onFailure(Call<EventSDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    // api request method
    private void apiRequest(int pageNumber) {

        // api call
        EventAPI eventAPI = RetrofitClientInstance.getRetrofitInstance().create(EventAPI.class);

        Call<EventSDTO> call =  eventAPI.searchEvent(paramTitle, paramStatus, paramSorttype, pageNumber);
        // execute request
        call.enqueue(new Callback<EventSDTO>() {
            // get json response
            @Override
            public void onResponse(Call<EventSDTO> call, Response<EventSDTO> response) {
                // append data
                if (response.isSuccessful()) {
                    System.out.println("Called search event api");
                    System.out.println("ONRESPONSE total page " + response.body().getTotalPage());
                    System.out.println("ONRESPONSE list size " + response.body().getEventList().size());

                    appendData(response.body());


                    Log.d("Response Event Search", response.body().toString());
                } else {
                    System.out.println("response failed");
                }
            }

            // error
            @Override
            public void onFailure(Call<EventSDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });

    }

    private void appendData(EventSDTO resultList) {

        lastSearch.setTotalPage(resultList.getTotalPage());
        System.out.println("total pages: "+resultList.getTotalPage());

        List<Event> eventList = lastSearch.getEventList();
        if (eventList == null){
            eventList = new ArrayList<>();
        }

        //append current data to dea list
        eventList.addAll(resultList.getEventList());
        lastSearch.setEventList(eventList);

        System.out.println("eventlist size  "+eventList.size());
        generateData(lastSearch);

    }

    // binding data to view
    private void generateData(EventSDTO eventSDTO){

//        this.lastPage = (int) listObject.get(0);
        this.lastPage = eventSDTO.getTotalPage();

        adapter = new CustomEventAdapter(eventSDTO, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EventActivity.this);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) mRecycleView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        linearProgressBar.setVisibility(View.GONE);
    }
}
