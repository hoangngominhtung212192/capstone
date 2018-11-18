package tks.com.gwaandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tks.com.gwaandroid.api.UserAPI;
import tks.com.gwaandroid.constant.AppConstant;
import tks.com.gwaandroid.model.Account;
import tks.com.gwaandroid.network.RetrofitClientInstance;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, email, firstname, lastname, password, rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialize
        username = (EditText) findViewById(R.id.signup_username);
        email = (EditText) findViewById(R.id.signup_email);
        firstname = (EditText) findViewById(R.id.signup_firstname);
        lastname = (EditText) findViewById(R.id.signup_lastname);
        password = (EditText) findViewById(R.id.signup_password);
        rePassword = (EditText) findViewById(R.id.signup_confirmpassword);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009688")));
        actionBar.hide();
    }

    public void signUp(View view) {
        if (checkValidation()) {
            requestApi();
        }
    }

    private void requestApi() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        UserAPI userAPI = RetrofitClientInstance.getRetrofitInstance().create(UserAPI.class);

        String json = "{\n" +
                "\t\"username\": \"" + username.getText().toString() + "\",\n" +
                "\t\"password\": \"" + password.getText().toString() + "\",\n" +
                "\t\"firstname\": \"" + firstname.getText().toString() + "\",\n" +
                "\t\"lastname\": \"" + lastname.getText().toString() + "\",\n" +
                "\t\"email\": \"" + email.getText().toString() + "\"\n" +
                "}";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Call<Account> call = userAPI.register(requestBody);

        // execute request
        call.enqueue(new Callback<Account>() {
            // get json response
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    SignUpActivity.this.startActivity(intent);
                } else {
                    // get error message from response error body
                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(SignUpActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void redirectToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean checkValidation() {
        boolean check = true;

        if (username.length() == 0) {
            username.setError("Please input your username");
            check = false;
        } else {
            if (username.length() > 50 || username.length() < 3) {
                username.setError("Out of range, maximum: 50 characters, minimum: 3 characters");
                check = false;
            } else {
                Pattern p = Pattern.compile(AppConstant.checkUsername);
                Matcher m = p.matcher(username.getText());
                if (!m.find()) {
                    username.setError("Only characters and no space");
                    check = false;
                }
            }
        }

        if (email.length() == 0) {
            email.setError("Please input your email");
            check = false;
        } else {
            Pattern p = Pattern.compile(AppConstant.checkEmail);
            Matcher m = p.matcher(email.getText());
            if (!m.find()) {
                email.setError("Please input valid email");
                check = false;
            }
        }

        if (firstname.length() == 0) {
            firstname.setError("Please input your first name");
            check = false;
        } else if (firstname.length() < 2 || firstname.length() > 50) {
            firstname.setError("Out of range, maximum: 50 characters, minimum: 2 characters");
            check = false;
        } else {
            Pattern p = Pattern.compile(AppConstant.checkString);
            Matcher m = p.matcher(firstname.getText());
            if (!m.find()) {
                firstname.setError("Only characters");
                check = false;
            }
        }

        if (lastname.length() == 0) {
            lastname.setError("Please input your last name");
            check = false;
        } else if (lastname.length() < 2 || lastname.length() > 50) {
            lastname.setError("Out of range, maximum: 50 characters, minimum: 2 characters");
            check = false;
        } else {
            Pattern p = Pattern.compile(AppConstant.checkString);
            Matcher m = p.matcher(lastname.getText());
            if (!m.find()) {
                lastname.setError("Only characters");
                check = false;
            }
        }

        if (password.length() == 0) {
            password.setError("Please input your password");
            check = false;
        } else {
            if (password.length() < 6 || password.length() > 50) {
                password.setError("Out of range, maximum: 50 characters, minimum: 6 characters");
                check = false;
            }
        }

        if (rePassword.length() == 0) {
            rePassword.setError("Please retype the password to confirm");
            check = false;
        } else {
            if (!rePassword.getText().toString().equals(password.getText().toString())) {
                rePassword.setError("Confirm password does not match password");
                check = false;
            }
        }

        return check;
    }
}
