package tks.com.gwaandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.api.UserAPI;
import tks.com.gwaandroid.model.Account;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginBtn;
    private TextView signUp;
    private CheckBox show_hide_password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signUp = (TextView) findViewById(R.id.signUpBtn);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // If it is checkec then show password else hide
                // password
                if (b) {

                    show_hide_password.setText(R.string.hide_pwd);// change
                    // checkbox
                    // text
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                } else {
                    show_hide_password.setText(R.string.show_pwd);// change
                    // checkbox
                    // text
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password

                }
            }
        });
    }


    public void login(View view) {
        if (checkValidation()) {
            requestApi();
        }
    }

    private boolean checkValidation() {

        boolean check = true;

        String txtUsername = username.getText().toString();
        String txtPassword = password.getText().toString();

        if (txtUsername.length() == 0) {
            Toast.makeText(this, "Please input username", Toast.LENGTH_SHORT).show();
            check = false;
        }

        if (txtPassword.length() == 0) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_SHORT).show();
            check = false;
        }

        return check;
    }

    private void requestApi() {
        UserAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserAPI.class);

        String json = "{\n" +
                "\t\"username\": \"" + username.getText().toString() + "\",\n" +
                "\t\"password\": \"" + password.getText().toString() + "\"\n" +
                "}";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Call<Account> call = userAPI.login(requestBody);

        // execute request
        call.enqueue(new Callback<Account>() {
            // get json response
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    // save current username to sharedpreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("USERNAME", response.body().getUsername());
                    editor.commit();

                    Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                } else {
                    // get error message from response error body
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            // error
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Log.d("Error response", t.getMessage());
            }
        });
    }
}
