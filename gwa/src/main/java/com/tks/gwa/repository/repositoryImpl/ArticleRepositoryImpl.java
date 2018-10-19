package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Article;
import com.tks.gwa.repository.ArticleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Article, Integer> implements ArticleRepository {
    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public List<Article> getAllArticle() {
        return this.getAll();
    }

    @Override
    public Article addNewArticle(Article article) {
        System.out.println("ADDING NEW ARTICLE WITH TITLE: " + article.getTitle());
        Article newArticle = this.create(article);

        return newArticle;
    }

    @Override
    public Article updateArticle(Article article) {
        Article updatedArticle = this.update(article);
        return updatedArticle;
    }

    @Override
    public boolean deleteArticle(Article article) {
        this.delete(article);
        return true;
    }

    @Override
    public Article findArticleByID(Integer id) {
        Article resultArticle = this.read(id);
        return resultArticle;
    }
}
