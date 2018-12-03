package tks.com.gwaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomUserRatingAdapter;
import tks.com.gwaandroid.api.UserAPI;
import tks.com.gwaandroid.model.Traderating;
import tks.com.gwaandroid.model.UserRatingDTO;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class UserRatingActivity extends AppCompatActivity {

    private RecyclerView uRatingRecyclerView;
    private CustomUserRatingAdapter adapter;
    private RelativeLayout linearProgressBar;
    private int lastPage = 1;
    private int currentPage;
    private UserRatingDTO lastLoad;
    private int accountID;

    private int currentVisiblePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rating);

        // initialize
        initialize();

        // request api and get last page first
        apiRequest(currentPage);

        // on scroll event
        uRatingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < lastPage) {
                        linearProgressBar.setVisibility(View.VISIBLE);
                        // save scroll last position
                        currentVisiblePosition = ((LinearLayoutManager) uRatingRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

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
        currentPage = 1;
        lastLoad = new UserRatingDTO();
        uRatingRecyclerView = findViewById(R.id.uRatingRecycleView);
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBar);
        linearProgressBar.setVisibility(View.VISIBLE);

        // get id from intent params
        Intent intent = getIntent();
        accountID = Integer.parseInt(intent.getStringExtra("ACCOUNTID"));
    }

    private void apiRequest(int pageNumber) {
        UserAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserAPI.class);

        Call<UserRatingDTO> call = userAPI.getUserRating(pageNumber, accountID);

        // execute request
        call.enqueue(new Callback<UserRatingDTO>() {
            // get json response
            @Override
            public void onResponse(Call<UserRatingDTO> call, Response<UserRatingDTO> response) {
                // append data
                appendData(response.body());
                Log.d("Response User rating", response.body().toString());
            }

            // error
            @Override
            public void onFailure(Call<UserRatingDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void appendData(UserRatingDTO result) {
        lastLoad.setLastPage(result.getLastPage());

        List<Traderating> traderatingList = lastLoad.getTraderatingList();

        if (traderatingList == null) {
            traderatingList = new ArrayList<Traderating>();
        }

        // append current data to list
        traderatingList.addAll(result.getTraderatingList());

        lastLoad.setTraderatingList(traderatingList);

        generateData(lastLoad);
    }

    private void generateData(UserRatingDTO dto) {
        this.lastPage = dto.getLastPage();

        adapter = new CustomUserRatingAdapter(dto.getTraderatingList(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserRatingActivity.this);
        uRatingRecyclerView.setLayoutManager(layoutManager);
        uRatingRecyclerView.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) uRatingRecyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        linearProgressBar.setVisibility(View.GONE);
    }
}
