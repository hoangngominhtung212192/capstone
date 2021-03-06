package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ArticleWS;
import com.tks.gwa.dto.ArticleSDTO;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Article;
import com.tks.gwa.listener.ArticleThreadCrawler;
import com.tks.gwa.service.ArticleService;
import com.tks.gwa.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;


@RestController
public class ArticleWSImpl implements ArticleWS {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleThreadCrawler articleThreadCrawler;

    @Override
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {

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

//        System.out.println("CONTENT "+article.getContent());
        return new ResponseEntity<Article>(article, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> getArticleAlt(@RequestParam Integer id) {
        Article article = articleService.getArticleByID(id);

        return new ResponseEntity<Article>(article, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> updateArticle(@RequestBody Article article) {
        Article getA = articleService.getArticleByID(article.getId());

        Account getacc = getA.getAccount();

        article.setAccount(getacc);
        article.setDate(getA.getDate());

        Article updatedarticle = articleService.updateArticle(article);
        return new ResponseEntity<>(updatedarticle, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteArticle(int id) {
        Article arr = articleService.getArticleByID(id);
        articleService.deleteArticle(arr);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Article>> searchAllArticle() {
        List<Article> result = articleService.getAllArticle();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> changeStatusArticle(@RequestParam int articleid
                                                    , @RequestParam String apprstatus) {
        System.out.println("WS changing status of article idL"+articleid);
        System.out.println("WS stt: " + apprstatus);
        Article resultArticle = articleService.changeStatusArticle(articleid, apprstatus);
        return new ResponseEntity<>(resultArticle, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Article>> changeStatusManyArticle(List<Integer> idlist, String status) {
        List<Article> resultList = articleService.changeStatusManyArticle(idlist, status);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> searchArticleByStatusAndPage(String title, String cate, String status, String sorttype, int pageNum) {
        List<Object> result = articleService.searchArticleWithSortAndPageByStatus(title, cate, status, sorttype, pageNum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArticleSDTO> searchArticleByAlt(String title, String cate, String status, String sorttype, int pageNum) {
        System.out.println("PAGE NUM "+pageNum);
        List<Object> result = articleService.searchArticleWithSortAndPageByStatus(title, cate, status, sorttype, pageNum);
        ArticleSDTO sdto = new ArticleSDTO();

        sdto.setTotalPage((Integer) result.get(0));
        sdto.setArticleList((List<Article>) result.get(1));

        return new ResponseEntity<>(sdto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getMyArticle(int id, String sorttype, int pageNum) {
        List<Object> result = articleService.getMyArticleByPageAndStatus(id, sorttype, pageNum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public ResponseEntity<Article> updateArticleImage(MultipartFile photoBtn, int id) {
        String filename = fileUploadService.storeFile(photoBtn);
        System.out.println("filename: "+filename);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(filename).toUriString();

        Article article = articleService.getArticleByID(id);
        if (article != null){
            article.setThumbImage(fileDownloadUri);
            Article updateArticle = articleService.updateArticle(article);
            if (updateArticle != null){
                return new ResponseEntity<>(updateArticle, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<LogCrawl>> getLogCrawl() {

        System.out.println("[ArticleWS] Begin getLogCrawl()");

        List<LogCrawl> logCrawlList = articleService.getLogCrawlFromFile();

        return new ResponseEntity<>(logCrawlList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> crawlArticle() {

        System.out.println("[ArticleWS] Begin crawlArticle()");

        if (articleThreadCrawler.isInprogress()) {
            return new ResponseEntity<String>("System is already crawling", HttpStatus.valueOf(400));
        }

        Thread articleCrawl = new Thread(articleThreadCrawler, "ArticleThreadCrawler");
        articleCrawl.start();

        return new ResponseEntity<String>("System is beginning crawling", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LogCrawl> getCrawlStatus() {
        System.out.println("[ArticleWS] Begin getCrawlStatus()");

        LogCrawl logCrawl = new LogCrawl();

        boolean inProgress = articleThreadCrawler.isInprogress();
        logCrawl.setInProgress(inProgress);

        int records = articleThreadCrawler.getCrawlRecords();
        logCrawl.setNumberOfRecords(records + "");

        int newRecords = articleThreadCrawler.getNewRecords();
        logCrawl.setNumberOfNewRecords(newRecords + "");

        return new ResponseEntity<>(logCrawl, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> searchPendingArticle(int pageNumber, String type, String txtSearch, String orderBy) {

        System.out.println("[ArticleWS] Begin searchPendingArticle() with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("Type: " + type);
        System.out.println("Search value: " + txtSearch);
        System.out.println("OrderBy: " + orderBy);

        List<Object> resultList = articleService.searchPendingArticle(pageNumber, type, txtSearch, orderBy);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Article> approvePendingArticle(int id) {

        System.out.println("[ArticleWS] Begin approvePendingArticle with articleID: " + id);

        Article article = articleService.approveArticle(id);

        if (article != null) {
            return new ResponseEntity<>(article, HttpStatus.OK);
        }

        return null;
    }
}
