package tks.com.gwaandroid.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.ManageOrderDataResponse;
import tks.com.gwaandroid.model.MyTradeDataResponse;
import tks.com.gwaandroid.model.SendOrderRequest;
import tks.com.gwaandroid.model.TradeDetailsResponse;
import tks.com.gwaandroid.model.TradingDataResponse;

public interface TrademarketAPI {
    @GET("api/tradepost/get-trade-listing")
    Call<TradingDataResponse> getTradingData(@Query("tradeType") String tradeType,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("sortType") int sortType);
    @GET("api/tradepost/view-trade-post")
    Call<TradeDetailsResponse> getTradeDetails(@Query("tradepostId") int tradepostId);

    @GET("api/tradepost/get-my-trade")
    Call<MyTradeDataResponse> getMyTradeData(@Query("accountId") int accountId,
                                             @Query("status") String status,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("sortType") int sortType);
    @POST("api/tradepost/send-order")
    Call<String> sendOrder(@Body SendOrderRequest sendOrderRequest);

    @POST("api/tradepost/update-quantity")
    Call<String> updateQuantity(@Query("tradepostId") int tradepostId,
                                @Query("newQuantity") int quantity);


    @POST("api/tradepost/delete-trade-post")
    Call<String> deleteTradepost(@Query("tradepostId") int tradepostId);

    @GET("api/tradepost/get-order-by-trade-post")
    Call<ManageOrderDataResponse> getManageOrderData(@Query("tradepostId") int tradepostId,
                                                     @Query("status") String status,
                                                     @Query("pageNumber") int pageNumber,
                                                     @Query("sortType") int sortType);


}
