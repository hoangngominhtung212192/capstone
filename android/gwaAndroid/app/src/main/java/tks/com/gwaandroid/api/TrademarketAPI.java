package tks.com.gwaandroid.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.TradeDetailsResponse;
import tks.com.gwaandroid.model.TradingDataResponse;

public interface TrademarketAPI {
    @GET("api/tradepost/get-trade-listing")
    Call<TradingDataResponse> getTradingData(@Query("tradeType") String tradeType,
                                             @Query("pageNumber") int pageNumber,
                                             @Query("sortType") int sortType);
    @GET("api/tradepost/view-trade-post")
    Call<TradeDetailsResponse> getTradeDetails(@Query("tradepostId") int tradepostId);

}
