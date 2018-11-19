package com.tks.gwa.service;

import com.tks.gwa.dto.LogCrawl;
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
    List<Object> searchArticleWithSortAndPageByStatus(String title, String cate, String status, String sorttype, int pageNum);
    List<Object> getMyArticleByPageAndStatus(int id, String sorttype, int pageNum);

    /**
     *
     * @param article
     * @return
     */
    Article createCrawlArticle(Article article);

    /**
     *
     * @return
     */
    List<LogCrawl> getLogCrawlFromFile();

    /**
     *
     * @return
     */
    public List<Object> searchPendingArticle(int pageNumber, String type, String txtSearch, String orderBy);

    /**
     *
     * @return
     */
    public Article approveArticle(int id);
}
