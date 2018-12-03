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
import tks.com.gwaandroid.adapter.CustomArticleAdapter;
import tks.com.gwaandroid.adapter.CustomEventAdapter;
import tks.com.gwaandroid.api.ArticleAPI;
import tks.com.gwaandroid.api.EventAPI;
import tks.com.gwaandroid.model.Article;
import tks.com.gwaandroid.model.ArticleSDTO;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.EventSDTO;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class ArticleActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    private RecyclerView mRecycleView;
    private CustomArticleAdapter adapter;
    private RelativeLayout linearProgressBar;
    private int lastPage;
    private int currentPage;
    private String paramTitle = "";
    private String paramSorttype = "desc";
    private String paramStatus = "Approved";
    private String paramCate = "";
    ArticleSDTO lastSearch ;

    private int currentVisiblePosition = 0;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // initialize
        currentPage = 1;

        // initialize object EventSDTO : lastSearch
        lastSearch = new ArticleSDTO();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // get recyclerview
        mRecycleView = findViewById(R.id.arecycleView);

        // display progress bar
        linearProgressBar = (RelativeLayout) findViewById(R.id.linearProgressBar);
        linearProgressBar.setVisibility(View.VISIBLE);

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
                    Intent intent = new Intent(ArticleActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(ArticleActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(ArticleActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(ArticleActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(ArticleActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Intent intent = new Intent(ArticleActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(ArticleActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ArticleActivity.this, LoginActivity.class);
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

    // api request method
    private void apiRequest(int pageNumber) {

        // api call
        ArticleAPI articleAPI = RetrofitClientInstance.getRetrofitInstance().create(ArticleAPI.class);

        Call<ArticleSDTO> call =  articleAPI.searchArticle(paramTitle, paramStatus, paramCate, paramSorttype, pageNumber);
        // execute request
        call.enqueue(new Callback<ArticleSDTO>() {
            // get json response
            @Override
            public void onResponse(Call<ArticleSDTO> call, Response<ArticleSDTO> response) {
                // append data
                if (response.isSuccessful()) {
//                    System.out.println("Called search event api");
//                    System.out.println("ONRESPONSE total page " + response.body().getTotalPage());
//                    System.out.println("ONRESPONSE list size " + response.body().getEventList().size());

                    appendData(response.body());


                    Log.d("Response Event Search", response.body().toString());
                } else {
                    System.out.println("response failed");
                }
            }

            // error
            @Override
            public void onFailure(Call<ArticleSDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });

    }

    private void appendData(ArticleSDTO resultList) {

        lastSearch.setTotalPage(resultList.getTotalPage());
        System.out.println("total pages: "+resultList.getTotalPage());

        List<Article> articleList = lastSearch.getArticleList();

        if (articleList == null){
            articleList = new ArrayList<>();
        }

        //append current data to dea list
        articleList.addAll(resultList.getArticleList());
        lastSearch.setArticleList(articleList);

//        System.out.println("eventlist size  "+eventList.size());
        generateData(lastSearch);

    }

    // binding data to view
    private void generateData(ArticleSDTO articleSDTO){

//        this.lastPage = (int) listObject.get(0);
        this.lastPage = articleSDTO.getTotalPage();

        adapter = new CustomArticleAdapter(articleSDTO, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ArticleActivity.this);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) mRecycleView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        linearProgressBar.setVisibility(View.GONE);
    }
}
