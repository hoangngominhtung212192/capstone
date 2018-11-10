package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Traderating;
import com.tks.gwa.repository.TraderatingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TraderatingRepositoryImpl extends GenericRepositoryImpl<Traderating, Integer> implements TraderatingRepository {

    @Override
    public int getCountByToUserID(int accountID) {

        String sql = "SELECT count(t.id) FROM " + Traderating.class.getName() + " AS t WHERE t.toUser.id =:accountID";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public List<Traderating> getListTradeRatingByToUserID(int pageNumber, int accountID) {

        String sql = "SELECT t FROM " + Traderating.class.getName() + " AS t WHERE t.toUser.id =:accountID ORDER BY t.ratingDate DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("accountID", accountID);
        query.setFirstResult((pageNumber-1)*10);
        query.setMaxResults(10);

        List<Traderating> traderatingList = query.getResultList();

        return traderatingList;
    }

    @Override
    public Traderating addNewTraderating(Traderating traderating) {
        return this.create(traderating);
    }

    @Override
    public boolean checkTraderatingExistByOrderIdAndFeedbackType(int orderID, int feedbackType) {
        String sql = "SELECT tr FROM " + Traderating.class.getName() + " AS tr WHERE tr.orderrequest.id =:orderID AND tr.feedbackType =:feedbackType";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("orderID", orderID);
        query.setParameter("feedbackType", feedbackType);
        try {
            query.getSingleResult();
            return true;
        }catch (NoResultException e){
            return false;
        }
    }
}
