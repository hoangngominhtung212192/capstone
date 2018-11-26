package tks.com.gwaandroid.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.ManageOrderDataResponse;
import tks.com.gwaandroid.model.MyOrderDataResponse;
import tks.com.gwaandroid.model.MyTradeDataResponse;
import tks.com.gwaandroid.model.SendOrderRequest;
import tks.com.gwaandroid.model.TradeDetailsResponse;
import tks.com.gwaandroid.model.TradingDataResponse;

public interface TrademarketAPI {
    @GET("api/tradepost/get-trade-listing")
    Call<TradingDataResponse> getTradingData(@Query("tradeType") String tradeType,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("sortType") int sortType);

    @GET("api/tradepost/search-trade-listing")
    Call<TradingDataResponse> getSearchTradingData(@Query("tradeType") String tradeType,
                                                   @Query("pageNumber") int pageNumber,
                                                   @Query("sortType") int sortType,
                                                   @Query("keyword") String keyword);

    @GET("api/tradepost/search-location-trade-listing")
    Call<TradingDataResponse> getSearchTradingWithLocationData(@Query("tradeType") String tradeType,
                                                               @Query("sortType") int sortType,
                                                               @Query("keyword") String keyword,
                                                               @Query("location") String location,
                                                               @Query("range") long range);


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

    @POST("api/tradepost/accept-order")
    Call<String> acceptOrder(@Query("orderId") int orderId);


    @POST("api/tradepost/decline-order")
    Call<String> declineOrder(@Query("orderId") int orderId,
                              @Query("reason") String reason);

    @POST("api/tradepost/confirm-succeed-order")
    Call<String> confirmSucceedOrder(@Query("orderId") int orderId);

    @POST("api/tradepost/cancel-order")
    Call<String> cancelOrder(@Query("orderId") int orderId,
                             @Query("reason") String reason);


    @POST("api/tradepost/rating-trade")
    Call<String> ratingTrade(@Query("orderId") int orderId,
                             @Query("feedbackType") int feedbackType,
                             @Query("rating") int value,
                             @Query("comment") String comment);


    @GET("api/tradepost/get-my-order")
    Call<MyOrderDataResponse> getMyOrderData(@Query("accountId") int accountId,
                                             @Query("status") String status,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("sortType") int sortType);






}
