package tks.com.gwaandroid.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tks.com.gwaandroid.constant.AppConstant;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(AppConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
