package com.tks.gwa.controller;

import com.tks.gwa.controller.controllerImpl.ArticleWSImpl;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public interface ArticleWS {
    @RequestMapping(value = "/createArticle", method = RequestMethod.POST)
    ResponseEntity<String> createArticle(@RequestBody Article article);
}
