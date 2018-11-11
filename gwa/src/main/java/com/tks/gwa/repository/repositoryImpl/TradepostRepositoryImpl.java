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
    public Tradepost addNewTradepost(Tradepost tradepost) {
        Tradepost newTradepost = this.create(tradepost);
        return newTradepost;
    }

    @Override
    public Tradepost updateTradepost(Tradepost tradepost) {
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

    @Override
    public int getCountTradepostByAccountIDAndTradetype(int accountID, int tradeType) {

        String sql = "SELECT count(t.id) FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus='approved' " +
                "AND t.account.id =:accountID AND t.tradeType =:tradeType";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);
        query.setParameter("tradeType", tradeType);

        return (int) (long) query.getSingleResult();
    }

    public boolean updateQuantityById(int tradepostId, int quantity) {
        Tradepost tradepost = this.read(tradepostId);
        if (tradepost == null) return false;
        tradepost.setQuantity(quantity);
        this.update(tradepost);
        return true;
    }


    //GET ALL TRADING POST (APPROVED)
    @Override
    public List<Tradepost> getTradepostByTradeTypeAndSortTypeInPageNumber(String tradeType, int pageNumber, int sortType) {
        String sortSql = getSortSql(sortType);
        String typeSql = "";
        if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            typeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_SELL_INT;
        } else if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            typeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_BUY_INT;
        }

        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus =:status" + typeSql + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.APPROVED_STATUS);

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


    //COUNT TOTAL PAGE TRADING POST
    @Override
    public int countTotalPageByTradeType(String tradeType) {
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
                    + " AS t WHERE t.tradeType =:tradeType  AND t.approvalStatus =:status";
            query = this.entityManager.createQuery(sql);
            query.setParameter("tradeType", tradeTypeInt);
            query.setParameter("status", AppConstant.APPROVED_STATUS);
        } else {
            sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus =:status";
            query = this.entityManager.createQuery(sql);
            query.setParameter("status", AppConstant.APPROVED_STATUS);
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

    //SEARCH ALL TRADING POST
    @Override
    public List<Tradepost> searchTradepostByTradeTypeAndSortTypeWithKeywordInPageNumber(String tradeType, int pageNumber, int sortType, String keyword) {
        String sortSql = getSortSql(sortType);
        String tradeTypeSql = "";
        if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_SELL_INT;
        } else if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_BUY_INT;
        }

        String sql = "SELECT t FROM " + Tradepost.class.getName()
                + " AS t WHERE (upper(t.title) LIKE upper(:keyword) OR upper(t.brand) LIKE upper(:keyword) OR upper(t.model) LIKE upper(:keyword))" +
                " AND t.approvalStatus =:status" + tradeTypeSql + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.APPROVED_STATUS);
        query.setParameter("keyword", "%" + keyword + "%");

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

    //COUNT TOTAL PAGE SEARCH TRADING POST
    @Override
    public int countTotalPageByTradeTypeWithKeyword(String tradeType, String keyword) {
        String tradeTypeSql = "";
        if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_SELL_INT;
        } else if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_BUY_INT;
        }

        String sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName()
                + " AS t WHERE (upper(t.title) LIKE upper(:keyword) OR upper(t.brand) LIKE upper(:keyword) OR upper(t.model) LIKE upper(:keyword))" +
                " AND t.approvalStatus =:status" + tradeTypeSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.APPROVED_STATUS);
        query.setParameter("keyword","%" + keyword + "%");
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


    //GET ALL TRADE POST IN MY TRADE
    @Override
    public List<Tradepost> getTradepostByAccountStatusAndSortTypeInPageNumber(int accountId, String status, int pageNumber, int sortType) {
        String sortSql = getSortSql(sortType);
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

    //COUNT TOTAL PAGE OF MY TRADE
    @Override
    public int countTotalPageByStatusAndAccount(String status, int accountId) {
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

    //SEARCH IN MY TRADE
    @Override
    public List<Tradepost> searchTradepostByAccountStatusAndSortTypeWithKeywordInPageNumber(int accountId, String status, int pageNumber, int sortType, String keyword) {
        String sortSql = getSortSql(sortType);
        String sql = "SELECT t FROM " + Tradepost.class.getName()
                + " AS t WHERE (upper(t.title) LIKE upper(:keyword) OR upper(t.brand) LIKE upper(:keyword) OR upper(t.model) LIKE upper(:keyword))" +
                " AND t.account.id =:accountID AND t.approvalStatus =:status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("accountID", accountId);
        query.setParameter("status", status);
        query.setParameter("keyword", "%"+ keyword + "%");
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
    //COUNT TOTAL PAGE SEARCH MY TRADE
    @Override
    public int countTotalPageByAccountStatusWithKeyword(int accountId, String status, String keyword) {
        String sql = "SELECT COUNT(t) FROM " + Tradepost.class.getName()
                + " AS t WHERE (upper(t.title) LIKE upper(:keyword) OR upper(t.brand) LIKE upper(:keyword) OR upper(t.model) LIKE upper(:keyword))" +
                " AND t.account.id =:accountID AND t.approvalStatus =:status";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountId);
        query.setParameter("status", status);
        query.setParameter("keyword", "%"+ keyword + "%");
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
    public List<Tradepost> searchAllTradepostByAccountStatusAndSortTypeWithKeyword(String tradeType, int sortType, String keyword) {
        String sortSql = getSortSql(sortType);
        String tradeTypeSql = "";
        if (AppConstant.TRADEPOST.TYPE_SELL.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_SELL_INT;
        } else if (AppConstant.TRADEPOST.TYPE_BUY.equals(tradeType)) {
            tradeTypeSql = " AND t.tradeType =" + AppConstant.TRADEPOST.TYPE_BUY_INT;
        }

        String sql = "SELECT t FROM " + Tradepost.class.getName()
                + " AS t WHERE (upper(t.title) LIKE upper(:keyword) OR upper(t.brand) LIKE upper(:keyword) OR upper(t.model) LIKE upper(:keyword))" +
                " AND t.approvalStatus =:status" + tradeTypeSql + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.APPROVED_STATUS);
        query.setParameter("keyword", "%" + keyword + "%");

        List<Tradepost> result = null;
        try {
            result = (List<Tradepost>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Tradepost> searchPendingTradePost(int pageNumber, int pageSize, String txtSearch, String orderBy) {
        boolean txtSearch_flag = false;

        String sql = "SELECT t FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus=:status";

        if (txtSearch.length() > 0) {
            sql += " AND t.title LIKE :keyword";
            txtSearch_flag = true;
        }

        if (orderBy.equalsIgnoreCase("Ascending")) {
            sql += " ORDER BY t.postedDate ASC";
        } else {
            sql += " ORDER BY t.postedDate DESC";
        }

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.PENDING_STATUS);

        if (txtSearch_flag) {
            query.setParameter("keyword", "%" + txtSearch + "%");
        }

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Tradepost> tradepostList = query.getResultList();

        return tradepostList;
    }

    @Override
    public int getCountSearchPendingTradepost(String txtSearch) {
        boolean txtSearch_flag = false;

        String sql = "SELECT count(t) FROM " + Tradepost.class.getName() + " AS t WHERE t.approvalStatus=:status";

        if (txtSearch.length() > 0) {
            sql += " AND t.title LIKE :keyword";
            txtSearch_flag = true;
        }

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("status", AppConstant.PENDING_STATUS);

        if (txtSearch_flag) {
            query.setParameter("keyword", "%" + txtSearch + "%");
        }

        return (int) (long) query.getSingleResult();
    }

    String getSortSql(int sortType) {
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
        return sortSql;
    }
}
