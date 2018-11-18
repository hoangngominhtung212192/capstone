package tks.com.gwaandroid.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.ModelDTO;
import tks.com.gwaandroid.model.ModelSDTO;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public interface ModelAPI {

    @POST("api/model/search")
    Call<ModelSDTO> search(@Body RequestBody requestBody);

    @GET("api/model/getDetail")
    Call<ModelDTO> getModelDetail(@Query("id") int id);
}
