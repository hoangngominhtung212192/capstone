package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.api.UserAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Profile;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView avatar, star_one, star_two, star_three, star_four, star_five;
    private TextView fullname, username, birthday, email, numberOfbuy, numberOfSell, role, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialize();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int accountID = sharedPreferences.getInt("ACCOUNTID", 0);
        requestApi(accountID);
    }

    private void initialize() {
        avatar = (ImageView) findViewById(R.id.profile_avatar);
        star_one = (ImageView) findViewById(R.id.profile_star_one);
        star_two = (ImageView) findViewById(R.id.profile_star_two);
        star_three = (ImageView) findViewById(R.id.profile_star_three);
        star_four = (ImageView) findViewById(R.id.profile_star_four);
        star_five = (ImageView) findViewById(R.id.profile_star_five);

        fullname = (TextView) findViewById(R.id.profile_fullname);
        username = (TextView) findViewById(R.id.profile_username);
        birthday = (TextView) findViewById(R.id.profile_birthday);
        email = (TextView) findViewById(R.id.profile_email);
        numberOfbuy = (TextView) findViewById(R.id.profile_numberOfBuy);
        numberOfSell = (TextView) findViewById(R.id.profile_numberOfSell);
        role = (TextView) findViewById(R.id.profile_role);
        rating = (TextView) findViewById(R.id.profile_rating);
    }

    private void requestApi(int accountID) {
        UserAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserAPI.class);

        Call<Profile> call = userAPI.getUserProfile(accountID);

        // execute request
        call.enqueue(new Callback<Profile>() {
            // get json response
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    bindingData(response.body());
                } else {
                    Toast.makeText(ProfileActivity.this, "Response error", Toast.LENGTH_SHORT).show();
                }
            }
            // error
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d("Error response", t.getMessage());
            }
        });
    }

    private void bindingData(Profile result) {
        if (result.getAvatar() != null) {
            Picasso picasso = Picasso.get();

            if (result.getAvatar().contains("localhost:8080")) {
                String imageUrl = result.getAvatar().replace("localhost", AppConstant.HOST_NAME);
                picasso.load(imageUrl)
                        .placeholder((R.drawable.loading_icon))
                        .fit()
                        .into(avatar);
            } else {
                picasso.load(result.getAvatar())
                        .placeholder((R.drawable.loading_icon))
                        .fit()
                        .into(avatar);
            }
        }

        String firstName = result.getFirstName();
        String middleName = result.getMiddleName();
        String lastName = result.getLastName();
        String fullName = "";

        if (middleName != null) {
            fullName = lastName + " " + middleName + " " + firstName;
        } else {
            fullName = lastName + " " + firstName;
        }

        fullname.setText(fullName);

        if (result.getBirthday() != null) {
            birthday.setText(result.getBirthday());
        } else {
            birthday.setText("N/A");
        }

        if (result.getNumberOfRaters() == 0) {
            rating.setVisibility(View.VISIBLE);
            star_one.setVisibility(View.GONE);
            star_two.setVisibility(View.GONE);
            star_three.setVisibility(View.GONE);
            star_four.setVisibility(View.GONE);
            star_five.setVisibility(View.GONE);
        } else {
            rating.setVisibility(View.GONE);

            int avgRating = Math.round((float) result.getNumberOfStars() / (float) result.getNumberOfRaters());

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

        if (result.getAccount().getRole().getName().equalsIgnoreCase("MEMBER")) {
            role.setText("Member");
            role.setTextColor(Color.parseColor("#006600"));
        } else {
            if (result.getAccount().getRole().getName().equalsIgnoreCase("MEMBER")) {
                role.setText("Trader");
                role.setTextColor(Color.parseColor("#996600"));
            } else {
                role.setText("Admin");
                role.setTextColor(Color.parseColor("#660000"));
            }
        }
    }
}
