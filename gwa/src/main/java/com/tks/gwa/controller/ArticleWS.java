package com.tks.gwa.controller;

import com.tks.gwa.controller.controllerImpl.ArticleWSImpl;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public interface ArticleWS {
    @RequestMapping(value = "/createArticle", method = RequestMethod.POST)
    ResponseEntity<Article> createArticle(@RequestBody Article article);

    @RequestMapping(value = "/searchArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> searchArticle(@RequestBody String title);

    @RequestMapping(value = "/getArticle", method = RequestMethod.POST)
    ResponseEntity<Article> getArticle(@RequestBody Integer id);

    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    ResponseEntity<Article> updateArticle(@RequestBody Article article);

    @RequestMapping(value = "/deleteArticle", method = RequestMethod.POST)
    ResponseEntity<String> deleteArticle(@RequestBody Article article);

    @RequestMapping(value = "/getAllArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> searchAllArticle();

    @RequestMapping(value = "/changeStatusOneArticle", method = RequestMethod.POST)
    ResponseEntity<Article> changeStatusArticle(@RequestParam int articleid, @RequestParam String apprstatus);

    @RequestMapping(value = "/changeStatusManyArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> changeStatusManyArticle(@RequestParam List<Integer> idlist,
                                                          @RequestParam String status);

    @RequestMapping(value = "/searchArticleByStatusAndPage", method = RequestMethod.POST)
    ResponseEntity<List<Object>> searchArticleByStatusAndPage(@RequestParam("title") String title,
                                                            @RequestParam("status") String status,
                                                            @RequestParam("sorttype") String sorttype,
                                                            @RequestParam("pageNum") int pageNum);
    @RequestMapping(value = "/getMyArticle", method = RequestMethod.POST)
    ResponseEntity<List<Object>> getMyArticle(@RequestParam("id") int id,
                                              @RequestParam("sorttype") String sorttype,
                                              @RequestParam("pageNum") int pageNum);
}
