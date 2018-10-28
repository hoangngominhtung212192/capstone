package com.tks.gwa.service;

import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradeListingDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.dto.ViewTradepostDTO;
import com.tks.gwa.entity.Tradepost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface TradepostService {
    boolean postNewTradePost(TradepostRequestData tradePostData);
    Tradepost getTradepostByID(int tradepostID);
    boolean editTradePost(TradepostRequestData tradePostData);
    TradepostRequestData getTradepostEditFormData(int tradepostId);
    List<MyTradeDTO> getMyTradeData(int accountId, String status, int pageNumber, int sortType);
    boolean updateTradePostQuantity(int tradepostId, int quantity);
    boolean deleteTradePost(int tradepostId);
    ViewTradepostDTO getViewTradePostData(int tradepostId);
    List<TradeListingDTO> getTradeListingData(String tradeType, int pageNumber, int sortType);
}
