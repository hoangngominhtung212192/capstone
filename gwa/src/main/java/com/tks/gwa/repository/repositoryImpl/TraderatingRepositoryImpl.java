package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Traderating;
import com.tks.gwa.repository.TraderatingRepository;

import javax.persistence.Query;
import java.util.List;

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
}
