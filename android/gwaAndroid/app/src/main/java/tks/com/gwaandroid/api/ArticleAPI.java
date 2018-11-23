package tks.com.gwaandroid.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tks.com.gwaandroid.model.Article;
import tks.com.gwaandroid.model.ArticleSDTO;
import tks.com.gwaandroid.model.Event;
import tks.com.gwaandroid.model.EventSDTO;

public interface ArticleAPI {
    @GET("api/article/searchArticleByAlt")
    Call<ArticleSDTO> searchArticle(@Query("title") String title,
                                    @Query("status") String status,
                                    @Query("cate") String cate,
                                    @Query("sorttype") String sorttype,
                                    @Query("pageNum") int pageNum);

    @GET("api/article/getArticleAlt")
    Call<Article> getArticle(@Query("id") int id);
}
