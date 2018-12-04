package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradepostRepository extends GenericRepository<Tradepost, Integer>{
    List<Tradepost> getAllTradepost();
    List<Tradepost> getAllTradepostByAccountID(int accountID);
    List<Tradepost> getTradepostByAccountStatusAndSortTypeInPageNumber(int accountId, String status, int pageNumber,
                                                                       int sortType);
    Tradepost addNewTradepost(Tradepost tradepost);
    Tradepost updateTradepost(Tradepost tradepost);
    boolean removeTradepost(Tradepost tradepost);
    Tradepost findTradepostById(int tradepostID);
    boolean updateQuantityById(int tradepostId, int quantity);

    List<Tradepost> getTradepostByTradeTypeAndSortTypeInPageNumber(String tradeType, int pageNumber, int sortType);
    int countTotalPageByTradeType(String tradeType);
    int countTotalPageByStatusAndAccount(String status, int accountId);
    List<Tradepost> searchTradepostByTradeTypeAndSortTypeWithKeywordInPageNumber(String tradeType, int pageNumber,
                                                                                 int sortType, String keyword);
    int countTotalPageByTradeTypeWithKeyword(String tradeType, String keyword);
    List<Tradepost> searchTradepostByAccountStatusAndSortTypeWithKeywordInPageNumber(int accountId, String status,
                                                                                     int pageNumber, int sortType,
                                                                                     String keyword);
    int countTotalPageByAccountStatusWithKeyword(int accountId, String status, String keyword);

    List<Tradepost> searchAllTradepostByAccountStatusAndSortTypeWithKeyword(String tradeType, int sortType, String keyword);



    List<Tradepost> searchPendingTradePost(int pageNumber, int pageSize, String txtSearch, String orderBy);
    int getCountSearchPendingTradepost(String txtSearch);

    // by TungHNM
    int getCountAll();
    List<Tradepost> get50TradePost(int pageNumber, int pageSize);
    int getCountTradepostByAccountIDAndTradetype(int accountID, int tradeType);
    // end by TungHNM
}
