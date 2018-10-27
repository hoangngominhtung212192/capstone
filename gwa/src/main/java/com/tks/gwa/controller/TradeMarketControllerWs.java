package com.tks.gwa.controller;

import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.entity.Tradepost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tradepost")
public interface TradeMarketControllerWs {
    @RequestMapping(value = "/post-new-trade", method = RequestMethod.POST)
    ResponseEntity<String> addTradepost(@RequestBody TradepostRequestData addRequestData);

    @RequestMapping(value = "/get-trade-post-edit-form-data", method = RequestMethod.GET)
    ResponseEntity<TradepostRequestData> getEditFormData(@RequestParam("tradepostID") String tradepostID);

    @RequestMapping(value = "/edit-trade-post", method = RequestMethod.POST)
    ResponseEntity<String> editTradepost(@RequestBody TradepostRequestData editRequestData);

    @RequestMapping(value = "/get-my-trade", method = RequestMethod.GET)
    ResponseEntity<List<MyTradeDTO>> getMyTradeList(@RequestParam("accountId") int accountId);
}
