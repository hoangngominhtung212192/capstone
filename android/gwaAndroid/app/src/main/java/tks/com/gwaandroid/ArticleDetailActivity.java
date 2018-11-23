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
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import tks.com.gwaandroid.api.EventAPI;
import tks.com.gwaandroid.api.ArticleAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Article;
//import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class ArticleDetailActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private RelativeLayout linearProgressBar;

    private TextView title, date, author;
    private WebView content;
//    private RelativeLayout layout_star_rating;

    private SharedPreferences sharedPreferences;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // initialize
        initialize();

        // get id from intent params
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("articleID"));

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
        dl = (DrawerLayout) findViewById(R.id.dl_article_detail);
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
                    Intent intent = new Intent(ArticleDetailActivity.this, ProfileActivity.class);
                    ArticleDetailActivity.this.startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(ArticleDetailActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(ArticleDetailActivity.this, MainActivity.class);
                    ArticleDetailActivity.this.startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(ArticleDetailActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(ArticleDetailActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Toast.makeText(ArticleDetailActivity.this, "Exchange", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(ArticleDetailActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ArticleDetailActivity.this, LoginActivity.class);
                    ArticleDetailActivity.this.startActivity(intent);
                }

                return true;
            }
        });
        // end left menu

        title = (TextView) findViewById(R.id.aArticleTitle);
        date = (TextView) findViewById(R.id.detailArDate);
        author = (TextView) findViewById(R.id.detailAuthor);

        content = (WebView) findViewById(R.id.aContent);

    }

    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void requestApi(int id) {
        ArticleAPI articleAPI = RetrofitClientInstance.getRetrofitInstance().create(ArticleAPI.class);

        Call<Article> call = articleAPI.getArticle(id);

        // execute request
        call.enqueue(new Callback<Article>() {
            // get json response
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {

                if (response.isSuccessful()) {
                    bindingData(response.body());

                    Log.d("Response article Detail", response.body().toString());
                } else {
                    Toast.makeText(ArticleDetailActivity.this, "Cannot get article detail", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<Article> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void bindingData(Article result) {

        title.setText(result.getTitle());

        date.setText(result.getDate());

        String authorS = "Author: "+result.getAccount().getUsername();
        author.setText(authorS);

        String contentHtml = result.getContent();
        if (contentHtml.contains("localhost:8080")){
            System.out.println("there is localhost in content");
            String newcont = contentHtml.replace("localhost",AppConstant.HOST_NAME);
            System.out.println("CHANGED CONTETNT: "+newcont);
            content.loadData(newcont, "text/html", "UTF-8");
        } else{
            System.out.println("no localhost in content");
            content.loadData(contentHtml, "text/html", "UTF-8");
        }

        linearProgressBar.setVisibility(View.GONE);
    }

}
