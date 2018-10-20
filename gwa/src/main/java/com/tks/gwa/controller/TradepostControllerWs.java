package com.tks.gwa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tradepost")
public interface TradepostControllerWs {
    @RequestMapping(value = "/post-new-trade", method = RequestMethod.POST)
    ResponseEntity<String> addTradepost();
}
