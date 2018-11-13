package tks.com.gwaandroid.api;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tks.com.gwaandroid.model.ModelSDTO;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public interface ModelAPI {

    @POST("api/model/search")
    Call<ModelSDTO> search(@Body RequestBody requestBody);
}
