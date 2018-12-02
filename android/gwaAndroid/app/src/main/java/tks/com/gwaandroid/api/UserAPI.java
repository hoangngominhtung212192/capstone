package tks.com.gwaandroid.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.Account;
import tks.com.gwaandroid.model.Profile;
import tks.com.gwaandroid.model.StatisticDTO;
import tks.com.gwaandroid.model.UserRatingDTO;

/**
 * Created by Tung Hoang Ngo Minh on 11/14/2018.
 */

public interface UserAPI {

    @POST("api/user/login")
    Call<Account> login(@Body RequestBody requestBody);

    @POST("api/user/register")
    Call<Account> register(@Body RequestBody requestBody);

    @POST("api/user/profile")
    Call<Profile> getUserProfile(@Query("accountID") int accountID);

    @GET("api/user/getMBStatistic")
    Call<StatisticDTO> getStatistic(@Query("accountID") int accountID);

    @GET("api/user/rating/mobile/getAll")
    Call<UserRatingDTO> getUserRating(@Query("pageNumber") int pageNumber,
                                      @Query("accountID") int accountID);
}
