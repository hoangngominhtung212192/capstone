package com.tks.gwa.repository;

import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRequestRepository {
    List<Orderrequest> getAllRequestByTradepostId(int TradepostId);
    int countRequestWithStatusByTradepostId(int tradepostId, String status);
    List<Orderrequest> getTradepostRequestDataByStatusAndInPageNumberWithSorting(int tradepostId, String status, int pageNumber, int sortType);
    boolean setOrderStatus(int orderId, String status, String dateSet, String cancelReason, String rejectReason);
    Tradepost getTradepostByOrderId(int orderId);
    int getOrderQuantityByOrderId(int orderId);
    Orderrequest addNewOrderRequest(Orderrequest newOrder);

    List<Orderrequest> getAllOrderRequestByAccountId(int accountId, String status, int pageNumber, int sortType);
    int countRequestWithStatusByAccountId(int accountId, String status);

    Orderrequest getOrderrequestById(int orderId);
}
