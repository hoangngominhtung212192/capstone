package com.tks.gwa.service;

import com.tks.gwa.dto.*;
import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface TrademarketService {
    Tradepost getTradepostByID(int tradepostID);

    int postNewTradePost(TradepostRequestData tradePostData);
    TradepostRequestData getTradepostEditFormData(int tradepostId);
    int editTradePost(TradepostRequestData tradePostData);

    boolean updateTradepostImagesByTradepostID(int tradepostId, String[] images);

    boolean updateTradePostQuantity(int tradepostId, int quantity);
    boolean deleteTradePost(int tradepostId);


    ViewTradepostDTO getViewTradePostData(int tradepostId);
    List<Object> getAllRequestByTradepost(int tradepostId, String status, int pageNumber, int sortType);


    List<Object> getMyTradeData(int accountId, String status, int pageNumber, int sortType);
    List<Object> getSearchMyTradeData(int accountId, String status, int pageNumber, int sortType, String keyword);


    List<Object> getTradeListingData(String tradeType, int pageNumber, int sortType);
    List<Object> getSearchTradeListingData(String tradeType, int pageNumber, int sortType, String keyword);
    List<Object> getSearchTradeListingWithLocationData(String tradeType, int sortType, String keyword, String location, long range);


    boolean acceptOrder(int orderId);
    boolean declineOrder(int orderId, String reason);
    boolean confirmOrderSucceed(int orderId);
    boolean cancelOrder(int orderId, String reason);
    boolean sendOrder(NewOrderDTO orderDTO);
    boolean reportTrade(int tradepostId, String reason, String phone, String email);
    boolean ratingTrade(int orderId, int feedbackType, int value, String comment);


    List<Object> getMyOrderData(int accountId, String status, int pageNumber, int sortType);




}
