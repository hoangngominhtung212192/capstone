package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradepostRepository extends GenericRepository<Tradepost, Integer>{
    List<Tradepost> getAllTradepost();
    List<Tradepost> getAllTradepostByAccountID(int accountID);
    List<Tradepost> getTradepostPerPageWithSortingAndStatusByAccount(int accountId, String status, int pageNumber, int sortType);
    Tradepost addNewTradepost(Tradepost tradepost);
    Tradepost editTradepost(Tradepost tradepost);
    boolean removeTradepost(Tradepost tradepost);
    Tradepost findTradepostById(int tradepostID);
    boolean updateQuantityById(int tradepostId, int quantity);
    List<Tradepost> getTradePostPerPageWithSortingByTradeType(String tradeType, int pageNumber, int sortType);
    int countNumberOfTradepostByTradeType(String tradeType);
    int countNumberOfTradepostByStatusAndAccount(String status, int accountId);

    int getCountAll();
    List<Tradepost> get50TradePost(int pageNumber, int pageSize);
}
