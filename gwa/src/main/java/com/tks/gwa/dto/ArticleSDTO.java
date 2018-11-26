package com.tks.gwa.dto;

import com.tks.gwa.entity.Article;

import java.util.List;

public class ArticleSDTO {
    private int totalPage;

    private List<Article> articleList;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
