package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.repository.OrderRequestRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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
        try{
            result = (List<Orderrequest>) query.getResultList();
        } catch (NoResultException e){
            e.printStackTrace();
        }
        return result;
    }
}
