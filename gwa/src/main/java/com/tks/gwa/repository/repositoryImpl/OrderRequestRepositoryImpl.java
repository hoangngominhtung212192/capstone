package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Orderrequest;
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
}
