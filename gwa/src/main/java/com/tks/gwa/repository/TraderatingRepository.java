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

    /**
     * Execute update to add new trade rating record
     * @param traderating
     * @return new traderating
     */
    Traderating addNewTraderating(Traderating traderating);

    /**
     * check rating existed by orderId and feedbackType
     * @param orderID
     * @param feedbackType
     * @return true if existed.
     */
    boolean checkTraderatingExistByOrderIdAndFeedbackType(int orderID, int feedbackType);

}
