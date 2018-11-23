package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleSDTO {
    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("articleList")
    private List<Article> articleList;

    public ArticleSDTO(){}

    public ArticleSDTO(int totalPage, List<Article> articleList) {
        this.totalPage = totalPage;
        this.articleList = articleList;
    }

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
