package com.tks.gwa.repository;

import com.tks.gwa.entity.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends GenericRepository<Article, Integer>{
    List<Article> getAllArticle();
    Article addNewArticle(Article article);
    Article updateArticle(Article article);
    boolean deleteArticle(Article article);
    Article findArticleByID(Integer id);
    List<Article> findArticleByTitle(String title);
    List<Article> findArticleByCategory(String category);
    Article changeStatusArticle(Integer id, String status);
    List<Article> changeStatusManyArticle(List<Integer> idlist, String status);

    /**
     *
     * @param modelName
     * @return
     */
    List<Article> getTop5ArticleByModelName(String modelName);
}
