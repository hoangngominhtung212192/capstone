package tks.com.gwaandroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.adapter.CustomModelAdapter;
import tks.com.gwaandroid.api.ModelAPI;
import tks.com.gwaandroid.model.ModelDTO;
import tks.com.gwaandroid.model.ModelSDTO;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    private RecyclerView mRecycleView;
    private CustomModelAdapter adapter;
    private RelativeLayout linearProgressBar;
    private int lastPage;
    private int currentPage;
    private ModelSDTO lastSearch;

    private int currentVisiblePosition = 0;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize
        currentPage = 1;
        lastSearch = new ModelSDTO();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // get recyclerview
        mRecycleView = findViewById(R.id.recycleView);

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
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.notification) {
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(MainActivity.this, EventActivity.class);
                    startActivity(intent);
                } else if (id == R.id.exchange) {
                    Intent intent = new Intent(MainActivity.this, TrademarketActivity.class);
                    startActivity(intent);
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(MainActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
        // end left menu

        // request api and get last page first
        apiRequest(currentPage);

        // on scroll event
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentPage < lastPage) {
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
        ModelAPI modelAPI = RetrofitClientInstance.getRetrofitInstance().create(ModelAPI.class);

        // post request with json body parameters
        String json = "{\n" +
                "\t\"searchValue\": \"\",\n" +
                "\t\"productseries\": \"All\",\n" +
                "\t\"seriestitle\": \"All\",\n" +
                "\t\"price\": \"All\",\n" +
                "\t\"manufacturer\": \"All\",\n" +
                "\t\"pagination\": {\n" +
                "\t\t\"currentPage\": " + pageNumber + ",\n" +
                "\t\t\"type\": \"\"\n" +
                "\t},\n" +
                "\t\"orderBy\": \"Rating\",\n" +
                "\t\"cending\": \"Descending\"\n" +
                "}";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Call<ModelSDTO> call = modelAPI.search(requestBody);

        // execute request
        call.enqueue(new Callback<ModelSDTO>() {
            // get json response
            @Override
            public void onResponse(Call<ModelSDTO> call, Response<ModelSDTO> response) {
                // append data
                appendData(response.body());

                Log.d("Response Model Search", response.body().toString());
            }

            // error
            @Override
            public void onFailure(Call<ModelSDTO> call, Throwable t) {
                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void appendData(ModelSDTO searchResult) {
        lastSearch.setSeriestitle(searchResult.getSeriestitle());
        lastSearch.setProductseries(searchResult.getProductseries());
        lastSearch.setPrice(searchResult.getPrice());
        lastSearch.setSearchValue(searchResult.getSearchValue());
        lastSearch.setOrderBy(searchResult.getOrderBy());
        lastSearch.setPagination(searchResult.getPagination());
        lastSearch.setManufacturer(searchResult.getManufacturer());

        List<ModelDTO> modelDTOList = lastSearch.getModelDTOList();

        if (modelDTOList == null) {
            modelDTOList = new ArrayList<ModelDTO>();
        }

        // append current data to list
        modelDTOList.addAll(searchResult.getModelDTOList());

        lastSearch.setModelDTOList(modelDTOList);

        generateData(lastSearch);
    }

    // binding data to view
    private void generateData(ModelSDTO modelSDTO) {
        this.lastPage = modelSDTO.getPagination().getLastPage();

        adapter = new CustomModelAdapter(modelSDTO, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(adapter);

        // scroll to the last position
        ((LinearLayoutManager) mRecycleView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        linearProgressBar.setVisibility(View.GONE);
    }

    public void searchClick(View view) {
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
    }
}
