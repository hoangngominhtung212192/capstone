package com.tks.gwa.repository;

import com.tks.gwa.entity.Orderrequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRequestRepository {
    List<Orderrequest> getAllRequestByTradepostId(int TradepostId);
    int countRequestWithStatusByTradepostId(int tradepostId, String status);
}
