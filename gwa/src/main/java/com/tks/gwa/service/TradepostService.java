package com.tks.gwa.service;

import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradepostRequestData;
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
    List<MyTradeDTO> getMyTradePageDataByAccountId(int accountId);
}
