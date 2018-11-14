package tks.com.gwaandroid.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tks.com.gwaandroid.model.Account;

/**
 * Created by Tung Hoang Ngo Minh on 11/14/2018.
 */

public interface UserAPI {

    @POST("api/user/login")
    Call<Account> login(@Body RequestBody requestBody);
}
