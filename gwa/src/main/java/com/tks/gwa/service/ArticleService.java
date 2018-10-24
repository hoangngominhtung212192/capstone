package com.tks.gwa.service;

import com.tks.gwa.entity.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {
    List<Article> getAllArticle();
    Article createArticle(Article article);
    Article updateArticle(Article article);
    boolean deleteArticle(Article article);
    Article getArticleByID(Integer id);
    List<Article> findArticleByTitle(String title);
    List<Article> findArticleByCategory(String category);
    Article changeStatusArticle(Integer id, String status);
    List<Article> changeStatusManyArticle(List<Integer> idlist, String status);
}
