package com.tks.gwa.controller;

import com.tks.gwa.controller.controllerImpl.ArticleWSImpl;
import com.tks.gwa.dto.ArticleSDTO;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.entity.Article;
import com.tks.gwa.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/getArticleAlt", method = RequestMethod.GET)
    ResponseEntity<Article> getArticleAlt(@RequestParam("id") Integer id);

    @RequestMapping(value = "/updateArticle", method = RequestMethod.POST)
    ResponseEntity<Article> updateArticle(@RequestBody Article article);

    @RequestMapping(value = "/deleteArticle", method = RequestMethod.GET)
    ResponseEntity<String> deleteArticle(@RequestParam("id") int id);

    @RequestMapping(value = "/getAllArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> searchAllArticle();

    @RequestMapping(value = "/changeStatusOneArticle", method = RequestMethod.POST)
    ResponseEntity<Article> changeStatusArticle(@RequestParam int articleid, @RequestParam String apprstatus);

    @RequestMapping(value = "/changeStatusManyArticle", method = RequestMethod.POST)
    ResponseEntity<List<Article>> changeStatusManyArticle(@RequestParam List<Integer> idlist,
                                                          @RequestParam String status);

    @RequestMapping(value = "/searchArticleByStatusAndPage", method = RequestMethod.POST)
    ResponseEntity<List<Object>> searchArticleByStatusAndPage(@RequestParam("title") String title,
                                                            @RequestParam("cate") String cate,
                                                            @RequestParam("status") String status,
                                                            @RequestParam("sorttype") String sorttype,
                                                            @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/searchArticleByAlt", method = RequestMethod.GET)
    ResponseEntity<ArticleSDTO> searchArticleByAlt(@RequestParam("title") String title,
                                                   @RequestParam("cate") String cate,
                                                   @RequestParam("status") String status,
                                                   @RequestParam("sorttype") String sorttype,
                                                   @RequestParam("pageNum") int pageNum);

    @RequestMapping(value = "/getMyArticle", method = RequestMethod.POST)
    ResponseEntity<List<Object>> getMyArticle(@RequestParam("id") int id,
                                              @RequestParam("sorttype") String sorttype,
                                              @RequestParam("pageNum") int pageNum);
    @RequestMapping(value = "/uploadArticleImage", method = RequestMethod.POST)
    ResponseEntity<Article> updateArticleImage(@RequestParam(value = "photoBtn", required = false) MultipartFile photoBtn,
                                           @RequestParam("id") int id);

    @RequestMapping(value = "/crawl/getLog", method = RequestMethod.GET)
    ResponseEntity<List<LogCrawl>> getLogCrawl();

    @RequestMapping(value = "/crawl", method = RequestMethod.GET)
    ResponseEntity<String> crawlArticle();

    @RequestMapping(value = "/crawl/getStatus", method = RequestMethod.GET)
    ResponseEntity<LogCrawl> getCrawlStatus();

    @RequestMapping(value = "/searchPending", method = RequestMethod.GET)
    ResponseEntity<List<Object>> searchPendingArticle(@RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("type") String type,
                                                    @RequestParam("txtSearch") String txtSearch,
                                                    @RequestParam("orderBy") String orderBy);

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    ResponseEntity<Article> approvePendingArticle(@RequestParam("id") int id);
}
