package tks.com.gwaandroid.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.Notification;
import tks.com.gwaandroid.model.NotificationDTO;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public interface NotificationAPI {

    @GET("api/notification/getAll")
    Call<NotificationDTO> getNotificationByAccountID(@Query("pageNumber") int pageNumber,
                                                     @Query("accountID") int accountID);

    @POST("api/notification/update")
    Call<String> checkSeen(@Query("notificationID") int notificationID);

    @POST("api/notification/addNew")
    Call<Notification> addNew(@Body RequestBody requestBody);
}
