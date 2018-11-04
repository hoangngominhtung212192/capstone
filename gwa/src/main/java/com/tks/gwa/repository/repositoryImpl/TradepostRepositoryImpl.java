package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Tradepost;
import com.tks.gwa.repository.TradepostRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TradepostRepositoryImpl extends GenericRepositoryImpl<Tradepost, Integer> implements TradepostRepository {

    public TradepostRepositoryImpl() {
        super(Tradepost.class);
    }

    @Override
    public List<Tradepost> getAllTradepost() {
        return this.getAll();
    }

    @Override
    public List<Tradepost> getAllTradepostByAccountID(int accountID) {
        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.account.id =:accountID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("accountID", accountID);
        List<Tradepost> result = null;
        try {
            result = (List<Tradepost>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Tradepost> getTradepostPerPageWithSortingAndStatusByAccount(int accountId, String status, int pageNumber, int sortType) {
        String sortSql = "";
        if (AppConstant.TRADEPOST.SORT_DATE_DESC == sortType) {
            sortSql = " ORDER BY t.postedDate DESC";
        } else if (AppConstant.TRADEPOST.SORT_DATE_ASC == sortType) {
            sortSql = " ORDER BY t.postedDate ASC";
        } else if (AppConstant.TRADEPOST.SORT_PRICE_DESC == sortType) {
            sortSql = " ORDER BY t.price DESC";
        } else if (AppConstant.TRADEPOST.SORT_PRICE_ASC == sortType) {
            sortSql = " ORDER BY t.price ASC";
        }
        String sql = "SELECT t FROM " + Tradepost.class.getName()
                + " AS t WHERE t.account.id =:accountID AND t.approvalStatus =:status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("accountID", accountId);
        query.setParameter("status", status);
        query.setFirstResult((pageNumber - 1) * AppConstant.TRADEPOST.MAX_POST_PER_PAGE);
        query.setMaxResults(AppConstant.TRADEPOST.MAX_POST_PER_PAGE);
        List<Tradepost> result = null;
        try {
            result = (List<Tradepost>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Tradepost addNewTradepost(Tradepost tradepost) {
        Tradepost newTradepost = this.create(tradepost);
        return newTradepost;
    }

    @Override
    public Tradepost editTradepost(Tradepost tradepost) {
        Tradepost updatedTradepost = this.update(tradepost);
        return updatedTradepost;
    }

    @Override
    public boolean removeTradepost(Tradepost tradepost) {
        this.delete(tradepost);
        return true;
    }

    @Override
    public Tradepost findTradepostById(int tradepostID) {
        return this.read(tradepostID);
    }

    @Override
    public int getCountAll() {

        String sql = "SELECT count(t.id) FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus='approved'";

        Query query = this.entityManager.createQuery(sql);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public List<Tradepost> get50TradePost(int pageNumber, int pageSize) {

        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus='approved' ORDER BY t.postedDate DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Tradepost> tradepostList = query.getResultList();

        return tradepostList;
    }

    public boolean updateQuantityById(int tradepostId, int quantity) {
        Tradepost tradepost = this.read(tradepostId);
        if (tradepost == null) return false;
        tradepost.setQuantity(quantity);
        this.update(tradepost);
        return true;
    }

    @Override
    public List<Tradepost> getTradePostPerPageWithSortingByTradeType(String tradeType, int pageNumber, int sortType) {
        String sortSql = "";
        if (AppConstant.TRADEPOST.SORT_DATE_DESC == sortType) {
            sortSql = " ORDER BY t.postedDate DESC";
        } else if (AppConstant.TRADEPOST.SORT_DATE_ASC == sortType) {
            sortSql = " ORDER BY t.postedDate ASC";
        } else if (AppConstant.TRADEPOST.SORT_PRICE_DESC == sortType) {
            sortSql = " ORDER BY t.price DESC";
        } else if (AppConstant.TRADEPOST.SORT_PRICE_ASC == sortType) {
            sortSql = " ORDER BY t.price ASC";
        }
        String whereSql = "";
        if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            whereSql = " WHERE t.tradeType =" + AppConstant.TRADEPOST.TYPE_SELL_INT;
        } else if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            whereSql = " WHERE t.tradeType =" + AppConstant.TRADEPOST.TYPE_BUY_INT;
        }

        String sql = "SELECT t FROM " + Tradepost.class.getName()
                + " AS t" + whereSql + sortSql;
        Query query = this.entityManager.createQuery(sql);

        query.setFirstResult((pageNumber - 1) * AppConstant.TRADEPOST.MAX_POST_PER_PAGE);
        query.setMaxResults(AppConstant.TRADEPOST.MAX_POST_PER_PAGE);
        List<Tradepost> result = null;
        try {
            result = (List<Tradepost>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int countNumberOfTradepostByTradeType(String tradeType) {
        int tradeTypeInt = 0;
        if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            tradeTypeInt = AppConstant.TRADEPOST.TYPE_BUY_INT;
        } else if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            tradeTypeInt = AppConstant.TRADEPOST.TYPE_SELL_INT;
        }
        String sql = "";
        Query query = null;
        if (tradeTypeInt != 0) {
            sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName()
                    + " AS t WHERE t.tradeType =:tradeType";
            query = this.entityManager.createQuery(sql);
            query.setParameter("tradeType", tradeTypeInt);
        } else {
            sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName() + " AS t";
            query = this.entityManager.createQuery(sql);
        }
        long result = 0;
        try {
            result = (long) query.getSingleResult();
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
        }
        long totalPage = result / AppConstant.TRADEPOST.MAX_POST_PER_PAGE;
        if (result % AppConstant.TRADEPOST.MAX_POST_PER_PAGE > 0) {
            totalPage = totalPage + 1;
        }
        return (int) totalPage;
    }

    @Override
    public int countNumberOfTradepostByStatusAndAccount(String status, int accountId) {
        String sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName()
                + " AS t WHERE t.account.id =:accountID AND t.approvalStatus =:status";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountId);
        query.setParameter("status", status);
        long result = 0;
        try {
            result = (long) query.getSingleResult();
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
        }
        long totalPage = result / AppConstant.TRADEPOST.MAX_POST_PER_PAGE;
        if (result % AppConstant.TRADEPOST.MAX_POST_PER_PAGE > 0) {
            totalPage = totalPage + 1;
        }
        return (int) totalPage;
    }
}
