package tks.com.gwaandroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomNotificationAdapter;
import tks.com.gwaandroid.api.NotificationAPI;
import tks.com.gwaandroid.model.Notification;
import tks.com.gwaandroid.model.NotificationDTO;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notiRecyclerView;
    private CustomNotificationAdapter adapter;
    private RelativeLayout linearProgressBar;
    private int lastPage = 1;
    private int currentPage;
    private NotificationDTO lastLoad;
    private int accountID;
    private TextView numberOfNotSeen;

    private SharedPreferences sharedPreferences;
    private int currentVisiblePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // initialize
        initialize();

        // request api and get last page first
        apiRequest(currentPage);

        // on scroll event
        notiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < lastPage) {
                        linearProgressBar.setVisibility(View.VISIBLE);
                        // save scroll last position
                        currentVisiblePosition = ((LinearLayoutManager) notiRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

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

    private void initialize() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accountID = sharedPreferences.getInt("ACCOUNTID", 0);

        currentPage = 1;
        lastLoad = new NotificationDTO();
        notiRecyclerView = findViewById(R.id.notiRecycleView);
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBar);
        linearProgressBar.setVisibility(View.VISIBLE);

        numberOfNotSeen = (TextView) findViewById(R.id.numberOfNotSeen);
    }

    private void apiRequest(int pageNumber) {
        NotificationAPI notificationAPI = RetrofitClientInstance.getRetrofitInstance().create(NotificationAPI.class);

        Call<NotificationDTO> call = notificationAPI.getNotificationByAccountID(pageNumber, accountID);

        // execute request
        call.enqueue(new Callback<NotificationDTO>() {
            // get json response
            @Override
            public void onResponse(Call<NotificationDTO> call, Response<NotificationDTO> response) {
                // append data
                appendData(response.body());
                Log.d("Response Notification", response.body().toString());
            }

            // error
            @Override
            public void onFailure(Call<NotificationDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void appendData(NotificationDTO result) {
        lastLoad.setLastPage(result.getLastPage());

        List<Notification> notificationList = lastLoad.getNotificationList();

        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }

        // append current data to list
        notificationList.addAll(result.getNotificationList());

        lastLoad.setNotificationList(notificationList);

        lastLoad.setNotSeen(result.getNotSeen());

        generateData(lastLoad);
    }

    private void generateData(NotificationDTO dto) {
        this.lastPage = dto.getLastPage();

        adapter = new CustomNotificationAdapter(dto.getNotificationList(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
        notiRecyclerView.setLayoutManager(layoutManager);
        notiRecyclerView.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) notiRecyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        linearProgressBar.setVisibility(View.GONE);

        this.numberOfNotSeen.setText("You have " + dto.getNotSeen() + " new notifications!");
    }
}
