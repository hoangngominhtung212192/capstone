package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
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
        System.out.println("REPO changing status of article idL"+id);
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

    @Override
    public List<Article> getTop5ArticleByModelName(String modelName) {

        String sql = "SELECT a FROM " + Article.class.getName() + " AS a WHERE a.title LIKE :modelName AND a.approvalStatus=" +
                "'approved' ORDER BY a.date DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("modelName", "%" + modelName + "%");
        query.setMaxResults(5);

        List<Article> result = query.getResultList();

        return result;
    }

    @Override
    public int countArticleBySearchStatus(String title, String status) {
        String sql = "SELECT COUNT(e) FROM " + Article.class.getName()+" AS e WHERE e.approvalStatus =:status AND e.title LIKE :title";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", status);
        query.setParameter("title", "%"+title+"%");
        long result = 0;
        try{
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("no article foudn");
            return 0;
        }

        return (int) result;
    }
    @Override
    public List<Article> searchArticleByStatusAndSort(String title, String status, String sorttype, int pageNum) {
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY date ASC";
        } else {
            sortSql = " ORDER BY date DESC";
        }
        String sql = "FROM " + Article.class.getName()+ " WHERE title LIKE :title AND approvalStatus = :status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("title", "%"+title+"%");
        query.setParameter("status", status);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Article> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

    @Override
    public List<Article> searchArticleByAuthorSort(int id, String sorttype, int pageNum) {
        String sortSql = "";
        if (sorttype.equalsIgnoreCase("asc")){
            sortSql = " ORDER BY date ASC";
        } else {
            sortSql = " ORDER BY date DESC";
        }
        String sql = "FROM " + Article.class.getName()+ " WHERE accountID = :accountID" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", id);
        query.setFirstResult((pageNum-1) * AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        query.setMaxResults(AppConstant.EVENT_MAX_RECORD_PER_PAGE);
        List<Article> listres = null;

        try {
            listres = query.getResultList();
            System.out.println("got "+listres.size() +" results");
        } catch (NoResultException e) {
            System.out.println("no event found");
            return listres;
        }
        return listres;
    }

}
