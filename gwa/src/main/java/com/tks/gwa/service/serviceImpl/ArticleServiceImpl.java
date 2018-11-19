package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Article;
import com.tks.gwa.repository.ArticleRepository;
import com.tks.gwa.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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
    public List<Object> searchArticleWithSortAndPageByStatus(String title, String cate, String status, String sorttype, int pageNum) {
        List<Article> eventList = articleRepository.searchArticleByStatusAndSort(title, cate, status, sorttype, pageNum);

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

    @Override
    public Article createCrawlArticle(Article article) {

        if (articleRepository.findArticleByTitle(article.getTitle()).size() == 0) {
            Account account = new Account();
            account.setId(3);
            article.setAccount(account);

            article.setApprovalStatus(AppConstant.CRAWL_PENDING);
            article.setDescription("This article is crawled by system !!!");

            String currentTime = getCurrentTimeStamp();
            article.setDate(currentTime);

            Article newArticle = articleRepository.create(article);
            System.out.println("Save article " + article.getTitle() + " to database successfully with ID: " + newArticle.getId());
            return newArticle;
        }

        System.out.println("Article " + article.getTitle() + " has been existed!");
        return null;
    }

    @Override
    public List<LogCrawl> getLogCrawlFromFile() {
        List<LogCrawl> listLogs = new ArrayList<LogCrawl>();
        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            f = new File(AppConstant.LOG_FILE_ARTICLE_CRAWL);
            if (!f.exists()) {
                return listLogs;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(details, ";");
                int id = Integer.parseInt(stk.nextToken());
                String logDateTime = stk.nextToken();
                String numberOfRecords = stk.nextToken();
                String numberOfNewRecords = stk.nextToken();
                String status = stk.nextToken();

                LogCrawl logCrawl = new LogCrawl(id, logDateTime, numberOfRecords, numberOfNewRecords, status);
                listLogs.add(logCrawl);
            }
            System.out.println("Finish loading from file with result: " + listLogs + " and size: " + listLogs.size());

            return listLogs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listLogs;
    }

    @Override
    public List<Object> searchPendingArticle(int pageNumber, String type, String txtSearch, String orderBy) {
        List<Object> result = new ArrayList<>();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCountResultAndLastPagePending(AppConstant.PAGE_SIZE, txtSearch);
        countTotal = (int) resultList[0];
        lastPage = (int) resultList[1];

        if (type.equals("First")) {
            currentPage = 1;
        } else if (type.equals("Prev")) {
            currentPage = pageNumber - 1;
        } else if (type.equals("Next")) {
            currentPage = pageNumber + 1;
        } else if (type.equals("Last")) {
            currentPage = lastPage;
        } else {
            currentPage = pageNumber;
        }

        if (currentPage <= 5) {
            beginPage = 1;
        } else if (currentPage % 5 != 0) {
            beginPage = ((int) (currentPage / 5)) * 5 + 1;
        } else {
            beginPage = ((currentPage / 5) - 1) * 5 + 1;
        }

        Pagination pagination = new Pagination(countTotal, currentPage, lastPage, beginPage);
        result.add(pagination);

        List<Article> articleList = articleRepository.searchPending(currentPage, AppConstant.PAGE_SIZE, txtSearch, orderBy);

        if (articleList == null) {
            articleList = new ArrayList<Article>();
        }
        result.add(articleList);

        return result;
    }

    @Override
    public Article approveArticle(int id) {

        Article article = articleRepository.findArticleByID(id);

        article.setApprovalStatus("Approved");

        Article newArticle = articleRepository.update(article);

        return newArticle;
    }

    public int[] getCountResultAndLastPagePending(int pageSize, String txtSearch) {

        int[] listResult = new int[2];
        int countResult = articleRepository.getCountSearchPending(txtSearch);
        listResult[0] = countResult;

        int lastPage = 1;

        if (countResult % pageSize == 0) {
            lastPage = countResult / pageSize;
        } else {
            lastPage = ((countResult / pageSize) + 1);
        }
        listResult[1] = lastPage;

        return listResult;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }
}
