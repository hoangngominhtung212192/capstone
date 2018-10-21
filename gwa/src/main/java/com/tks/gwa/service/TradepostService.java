package com.tks.gwa.service;

import com.tks.gwa.dto.AddNewTradeData;
import org.springframework.stereotype.Service;

@Service
public interface TradepostService {
    public boolean postNewTradePost(AddNewTradeData tradePostData);
}
