package com.tks.gwa.repository;

import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradepostRepository extends GenericRepository<Tradepost, Integer>{
    List<Tradepost> getAllTradepost();
    List<Tradepost> getAllTradepostByAccountID(int accountID);
    Tradepost addNewTradepost(Tradepost tradepost);
    Tradepost editTradepost(Tradepost tradepost);
    boolean removeTradepost(Tradepost tradepost);
    Tradepost findTradepostById(int tradepostID);

}
