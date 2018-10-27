package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ArticleWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ArticleWSImpl implements ArticleWS {

    @Autowired
    ArticleService articleService;

    @Override
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        System.out.println("----Adding article with title:" + article.getTitle());
        article.setApprovalStatus("APPROVED");
        article.setCategory("news");
        article.setDate("1/1/2011");


        Account testacc = new Account();
        testacc.setId(1);
        testacc.setPassword("ab");
        testacc.setStatus("normal");
        testacc.setUsername("test user num1");


        article.setAccount(testacc);

        Article newarticle = articleService.createArticle(article);

        return new ResponseEntity<Article>(newarticle, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Article>> searchArticle(@RequestBody String title) {
        List<Article> result = articleService.findArticleByTitle(title);
        if (result.size() > 0) {
            return new ResponseEntity<List<Article>>(result, HttpStatus.OK);
        } else return new ResponseEntity<List<Article>>(result, HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<Article> getArticle(@RequestBody Integer id) {
        System.out.println("WS getarticle id:" + id);
        Article article = articleService.getArticleByID(id);

        return new ResponseEntity<Article>(article, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> updateArticle(@RequestBody Article article) {
        System.out.println("editing article with id:" + article.getId());
        article.setApprovalStatus("APPROVED");
        article.setCategory("news");
        article.setDate("1/1/2011");


        Account testacc = new Account();
        testacc.setId(1);
        testacc.setPassword("ab");
        testacc.setStatus("normal");
        testacc.setUsername("test user num1");


        article.setAccount(testacc);

        Article updatedarticle = articleService.updateArticle(article);
        return new ResponseEntity<>(updatedarticle, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteArticle(@RequestBody Article article) {
        return null;
    }

    @Override
    public ResponseEntity<List<Article>> searchAllArticle() {
        List<Article> result = articleService.getAllArticle();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> changeStatusArticle(Integer id, String status) {
        Article resultArticle = articleService.changeStatusArticle(id, status);
        return new ResponseEntity<>(resultArticle, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Article>> changeStatusManyArticle(List<Integer> idlist, String status) {
        List<Article> resultList = articleService.changeStatusManyArticle(idlist, status);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
