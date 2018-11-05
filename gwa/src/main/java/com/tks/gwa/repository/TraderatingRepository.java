package com.tks.gwa.repository;

import com.tks.gwa.entity.Traderating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraderatingRepository extends GenericRepository<Traderating, Integer> {

    /**
     *
     * @param accountID
     * @return
     */
    public int getCountByToUserID(int accountID);

    /**
     *
     * @param pageNumber
     * @param accountID
     * @return
     */
    public List<Traderating> getListTradeRatingByToUserID(int pageNumber, int accountID);
}
