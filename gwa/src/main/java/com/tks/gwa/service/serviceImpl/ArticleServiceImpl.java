package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Article;
import com.tks.gwa.repository.ArticleRepository;
import com.tks.gwa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticle() {
        return articleRepository.getAllArticle();
    }

    public Article createArticle(Article article) {
        return articleRepository.addNewArticle(article);
    }

    public Article updateArticle(Article article) {
        return articleRepository.updateArticle(article);
    }

    @Override
    public boolean deleteArticle(Article article) {
        articleRepository.deleteArticle(article);
        return true;
    }

    @Override
    public Article getArticleByID(Integer id) {
        return articleRepository.findArticleByID(id);
    }

    @Override
    public List<Article> findArticleByTitle(String title) {
        return articleRepository.findArticleByTitle(title);
    }

    @Override
    public List<Article> findArticleByCategory(String category) {
        return articleRepository.findArticleByCategory(category);
    }

    @Override
    public Article changeStatusArticle(Integer id, String status) {
        return articleRepository.changeStatusArticle(id, status);
    }

    @Override
    public List<Article> changeStatusManyArticle(List<Integer> idlist, String status) {
        return articleRepository.changeStatusManyArticle(idlist, status);
    }

    @Override
    public List<Object> searchArticleWithSortAndPageByStatus(String title, String status, String sorttype, int pageNum) {
        int totalRecord = articleRepository.countArticleBySearchStatus(title, status);
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();
        result.add(totalPage);
        List<Article> eventList = articleRepository.searchArticleByStatusAndSort(title, status, sorttype, pageNum);
        result.add(eventList);

        return result;
    }

    @Override
    public List<Object> getMyArticleByPageAndStatus(int id, String sorttype, int pageNum) {
        List<Article> eventList = articleRepository.searchArticleByAuthorSort(id, sorttype, pageNum);
        int totalRecord = eventList.size();
        int totalPage = totalRecord / AppConstant.EVENT_MAX_RECORD_PER_PAGE;
        if (totalRecord % AppConstant.EVENT_MAX_RECORD_PER_PAGE > 0){
            totalPage +=1;
        }
        List<Object> result = new ArrayList<>();

        result.add(totalPage);
        result.add(eventList);

        return result;
    }
}
