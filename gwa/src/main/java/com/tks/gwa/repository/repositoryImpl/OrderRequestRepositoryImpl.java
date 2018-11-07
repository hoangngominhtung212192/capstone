package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.entity.Tradepost;
import com.tks.gwa.repository.OrderRequestRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRequestRepositoryImpl extends GenericRepositoryImpl<Orderrequest, Integer> implements OrderRequestRepository {
    public OrderRequestRepositoryImpl() {
        super(Orderrequest.class);
    }

    @Override
    public List<Orderrequest> getAllRequestByTradepostId(int TradepostId) {
        String sql = "SELECT o FROM " + Orderrequest.class.getName() + " AS o WHERE o.tradepost.id =:tradepostID";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", TradepostId);
        List<Orderrequest> result = null;
        try {
            result = (List<Orderrequest>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int countRequestWithStatusByTradepostId(int tradepostId, String status) {
        String sql = "SELECT COUNT(o) FROM " + Orderrequest.class.getName()
                + " AS o WHERE o.tradepost.id =:tradepostID AND o.status =:status";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("tradepostID", tradepostId);
        query.setParameter("status", status);
        long result = 0;
        try {
           result = (long) query.getSingleResult();
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
        }
        return (int) result;
    }

    @Override
    public List<Orderrequest> getTradepostRequestDataByStatusAndInPageNumberWithSorting(int tradepostId, String status, int pageNumber, int sortType) {
        String sortSql = getSortSql(sortType);
        String sql = "SELECT o FROM " + Orderrequest.class.getName() +
                " AS o WHERE o.tradepost.id =:tradepostID AND o.status =:status" + sortSql;
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("tradepostID", tradepostId);
        query.setParameter("status", status);
        query.setFirstResult((pageNumber - 1) * AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE);
        query.setMaxResults(AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE);
        List<Orderrequest> result = null;
        try {
            result = (List<Orderrequest>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean setOrderStatus(int orderId, String status, String dateSet, String cancelReason, String rejectReason) {
        Orderrequest orderrequest = this.read(orderId);
        if (orderrequest == null){
            return false;
        }
        orderrequest.setStatus(status);
        orderrequest.setStateSetDate(dateSet);
        orderrequest.setCancelReason(cancelReason);
        orderrequest.setRejectReason(rejectReason);
        this.update(orderrequest);
        return true;
    }

    @Override
    public Tradepost getTradepostByOrderId(int orderId) {
        Orderrequest orderrequest = this.read(orderId);
        return orderrequest.getTradepost();
    }

    @Override
    public int getOrderQuantityByOrderId(int orderId) {
        Orderrequest orderrequest = this.read(orderId);
        return orderrequest.getQuantity();
    }

    @Override
    public Orderrequest addNewOrderRequest(Orderrequest newOrder) {
        return this.create(newOrder);
    }

    @Override
    public List<Orderrequest> getAllOrderRequestByAccountId(int accountId, String status, int pageNumber, int sortType) {
        String sortSql = getSortSql(sortType);
        String statusSql = "o.status =:status";
        if (status.equals("others")){
            statusSql = "(o.status =:statusC OR o.status =:statusD)";
        }

        String sql = "SELECT o FROM " + Orderrequest.class.getName() +
                " AS o WHERE o.account.id =:accountID AND " + statusSql + sortSql;

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountId);
        if (status.equals("others")){
            query.setParameter("statusD","declined");
            query.setParameter("statusC","cancelled");
        }else {
            query.setParameter("status", status);
        }
        query.setFirstResult((pageNumber - 1) * AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE);
        query.setMaxResults(AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE);
        List<Orderrequest> result = null;
        try {
            result = (List<Orderrequest>) query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int countRequestWithStatusByAccountId(int accountId, String status) {
        String sql = "SELECT COUNT(o) FROM " + Orderrequest.class.getName()
                + " AS o WHERE o.account.id =:accountID AND o.status =:status";
        Query query = this.entityManager.createQuery(sql);
        //Set param
        query.setParameter("accountID", accountId);
        query.setParameter("status", status);
        long result = 0;
        try {
            result = (long) query.getSingleResult();
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
        }
        return (int) result;
    }

    @Override
    public Orderrequest getOrderrequestById(int orderId) {
        return this.read(orderId);
    }

    String getSortSql(int sortType) {
        String sortSql = "";
        if (AppConstant.TRADEPOST.SORT_DATE_DESC == sortType) {
            sortSql = " ORDER BY o.orderDate DESC";
        } else if (AppConstant.TRADEPOST.SORT_DATE_ASC == sortType) {
            sortSql = " ORDER BY o.orderDate ASC";
        }
        return sortSql;
    }
}
