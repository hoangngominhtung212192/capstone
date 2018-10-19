package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.entity.Article;
import com.tks.gwa.repository.ArticleRepository;
import com.tks.gwa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository; /*trùng tên bean trong spring container :)*/

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

    public Article getArticleByID(Integer id) {
        return articleRepository.findArticleByID(id);
    }
}
