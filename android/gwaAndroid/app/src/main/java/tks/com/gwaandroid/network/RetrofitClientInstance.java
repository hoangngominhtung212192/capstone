package tks.com.gwaandroid.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String base_url = "http://192.168.1.6:8080/gwa/";

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
