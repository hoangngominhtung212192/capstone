package com.tks.gwa.controller;

import com.tks.gwa.controller.controllerImpl.ArticleWSImpl;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public interface ArticleWS {
    @RequestMapping(value = "/createArticle", method = RequestMethod.POST)
    ResponseEntity<String> createArticle(@RequestBody Article article);

    @RequestMapping(value = "/searchArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> searchArticle(@RequestBody String title);

    @RequestMapping(value = "/getArticle", method = RequestMethod.POST)
    ResponseEntity<Article> getArticle(@RequestBody Integer id);

    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    ResponseEntity<Article> updateArticle(@RequestBody Article article);

    @RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
    ResponseEntity<String> deleteArticle(@RequestBody Article article);

    @RequestMapping(value = "/getAllArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> searchArticle();

    @RequestMapping(value = "/changeStatusOneArticle", method = RequestMethod.POST)
    ResponseEntity<Article> changeStatusArticle(@RequestBody Integer id, String status);

    @RequestMapping(value = "/changeStatusManyArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> changeStatusManyArticle(@RequestBody List<Integer> idlist, String status);

}
