package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ArticleWS;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ArticleWSImpl implements ArticleWS {

    @Autowired
    ArticleService articleService;

    @Override
    public ResponseEntity<String> createArticle(@RequestBody Article article) {
        System.out.println("----Adding article with title:" + article.getTitle());
        article.setApprovalStatus("APPROVED");
        article.setCategory("news");
        article.setDate("1/1/2011");


        Account testacc = new Account();
        testacc.setId(1);
        testacc.setPassword("ab");
        testacc.setStatus("normal");
        testacc.setUsername("test user num1");


//        article.set;
        article.setAccount(testacc);

        Article newarticle = articleService.createArticle(article);
//        System.out.println(account.getPassword());

        return ResponseEntity.ok("Successful!");
    }
}
