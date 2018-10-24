package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Article;
import com.tks.gwa.repository.ArticleRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
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
        System.out.println("repo getarticle id:" + id);
        Article resultArticle = this.read(id);
        return resultArticle;
    }

    @Override
    public List<Article> findArticleByTitle(String title) {
        String sql = "FROM " + Article.class.getName()+ " WHERE title LIKE :title";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("title", "%" + title + "%");
        List<Article> listres = null;

        try {
            listres = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("search FAILED!!");
            return listres;
        }

        return listres;
    }

    @Override
    public List<Article> findArticleByCategory(String category) {
        String sql = "FROM " + Article.class.getName()+ " WHERE category LIKE :category";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("category", "%" + category + "%");
        List<Article> listres = null;

        try {
            listres = query.getResultList();
        } catch (NoResultException e) {
            System.out.println("search FAILED!!");
            return listres;
        }
        return listres;
    }

    @Override
    public Article changeStatusArticle(Integer id, String status) {
        Article article = this.findArticleByID(id);
        article.setApprovalStatus(status);
        Article updatedArticle = this.update(article);
        return updatedArticle;
    }

    @Override
    public List<Article> changeStatusManyArticle(List<Integer> idlist, String status) {
        List<Article> lista = null;
        for (Integer e : idlist){
            Article article = this.findArticleByID(e);
            article.setApprovalStatus(status);
            lista.add(article);
        }
        return lista;
    }

}
