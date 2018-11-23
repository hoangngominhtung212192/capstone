package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import tks.com.gwaandroid.api.ModelAPI;
import tks.com.gwaandroid.model.Model;
import tks.com.gwaandroid.model.ModelDTO;
import tks.com.gwaandroid.model.Modelimage;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class ModelDetailActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private RelativeLayout linearProgressBar;

    private TextView name, scale, series, manu, released, price;
    private ImageView thumbImg, star_one, star_two, star_three, star_four, star_five;
    private RelativeLayout layout_star_rating;

    private SharedPreferences sharedPreferences;
    private int id;

    private List<Modelimage> modelimageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_detail);

        // initialize
        initialize();

        // get id from intent params
        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra("modelID"));

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
        dl = (DrawerLayout) findViewById(R.id.dl_model_detail);
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
                    Intent intent = new Intent(ModelDetailActivity.this, ProfileActivity.class);
                    ModelDetailActivity.this.startActivity(intent);
                } else if (id == R.id.notification) {
                    Toast.makeText(ModelDetailActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.gundam) {
                    Intent intent = new Intent(ModelDetailActivity.this, MainActivity.class);
                    ModelDetailActivity.this.startActivity(intent);
                } else if (id == R.id.article) {
                    Intent intent = new Intent(ModelDetailActivity.this, ArticleActivity.class);
                    startActivity(intent);
                } else if (id == R.id.event) {
                    Intent intent = new Intent(ModelDetailActivity.this, EventActivity.class);
                    startActivity(intent);
                }  else if (id == R.id.exchange) {
                    Toast.makeText(ModelDetailActivity.this, "Exchange", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.signout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Toast.makeText(ModelDetailActivity.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ModelDetailActivity.this, LoginActivity.class);
                    ModelDetailActivity.this.startActivity(intent);
                }

                return true;
            }
        });
        // end left menu

        name = (TextView) findViewById(R.id.detailName);
        scale = (TextView) findViewById(R.id.detailScale);
        series = (TextView) findViewById(R.id.detailSeries);
        manu = (TextView) findViewById(R.id.detailManu);
        released = (TextView) findViewById(R.id.detailReleased);
        price = (TextView) findViewById(R.id.detailPrice);

        thumbImg = (ImageView) findViewById(R.id.detailImage);
        star_one = (ImageView) findViewById(R.id.star_one_detail);
        star_two = (ImageView) findViewById(R.id.star_two_detail);
        star_three = (ImageView) findViewById(R.id.star_three_detail);
        star_four = (ImageView) findViewById(R.id.star_four_detail);
        star_five = (ImageView) findViewById(R.id.star_five_detail);

        layout_star_rating = (RelativeLayout) findViewById(R.id.layout_star_rating);
    }

    // on options left menu selected event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void requestApi(int id) {

        ModelAPI modelAPI = RetrofitClientInstance.getRetrofitInstance().create(ModelAPI.class);

        Call<ModelDTO> call = modelAPI.getModelDetail(id);

        // execute request
        call.enqueue(new Callback<ModelDTO>() {
            // get json response
            @Override
            public void onResponse(Call<ModelDTO> call, Response<ModelDTO> response) {

                if (response.isSuccessful()) {
                    bindingData(response.body());

                    Log.d("Response Model Detail", response.body().toString());
                } else {
                    Toast.makeText(ModelDetailActivity.this, "Cannot get model detail", Toast.LENGTH_SHORT).show();
                }
            }

            // error
            @Override
            public void onFailure(Call<ModelDTO> call, Throwable t) {
//                linearProgressBar.setVisibility(View.GONE);
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void bindingData(ModelDTO result) {
        Model model = result.getModel();
        modelimageList = result.getModelimageList();

        name.setText(model.getName());

        Picasso picasso = Picasso.get();
        if (model.getThumbImage().contains("localhost:8080")) {
            String imageUrl = model.getThumbImage().replace("localhost", "192.168.1.6");
            picasso.load(imageUrl)
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(thumbImg);
        } else {
            picasso.load(model.getThumbImage())
                    .placeholder((R.drawable.loading_icon))
                    .fit()
                    .into(thumbImg);
        }

        scale.setText(model.getProductseries().getName());
        series.setText(model.getSeriestitle().getName());

        if (model.getManufacturer() == null) {
            manu.setText("N/A");
        } else {
            manu.setText(model.getManufacturer().getName());
        }

        if (model.getPrice() == null) {
            price.setText("N/A");
        } else {
            price.setText(model.getPrice());
        }

        if (model.getReleasedDate() == null) {
            released.setText("N/A");
        } else {
            released.setText(model.getReleasedDate());
        }

        if (model.getNumberOfRater() != 0) {
            layout_star_rating.setVisibility(View.VISIBLE);
            int avgRating = Math.round((float) model.getNumberOfRating() / (float) model.getNumberOfRater());

            if (avgRating >= 1) {
                star_one.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 2) {
                star_two.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 3) {
                star_three.setVisibility(View.VISIBLE);
            }
            if (avgRating >= 4) {
                star_four.setVisibility(View.VISIBLE);
            }
            if (avgRating == 5) {
                star_five.setVisibility(View.VISIBLE);
            }
        }

        linearProgressBar.setVisibility(View.GONE);
    }

    public void viewGallery(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("GALLERY", (Serializable) modelimageList);
        startActivity(intent);
    }
}
