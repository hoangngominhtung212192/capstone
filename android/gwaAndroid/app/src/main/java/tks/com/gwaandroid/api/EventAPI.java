package tks.com.gwaandroid.api;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.EventSDTO;

public interface EventAPI {
    @GET("api/event/searchEventAlt")
    Call<EventSDTO> searchEvent(@Query("title") String title,
                                @Query("status") String status,
                                @Query("sorttype") String sorttype,
                                @Query("pageNum") int pageNum);

    @GET("api/event/getEventAlt")
    Call<Event> getEventDetail(@Query("id") int id);
}
