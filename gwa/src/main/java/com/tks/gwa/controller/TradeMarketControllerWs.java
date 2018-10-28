package com.tks.gwa.controller;

import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradeListingDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.dto.ViewTradepostDTO;
import com.tks.gwa.entity.Tradepost;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tradepost")
public interface TradeMarketControllerWs {
    @RequestMapping(value = "/add-trade-post", method = RequestMethod.POST)
    ResponseEntity<String> addTradepost(@RequestBody TradepostRequestData addRequestData);

    @RequestMapping(value = "/get-trade-post-edit-form-data", method = RequestMethod.GET)
    ResponseEntity<TradepostRequestData> getEditFormData(@RequestParam("tradepostID") String tradepostID);

    @RequestMapping(value = "/edit-trade-post", method = RequestMethod.POST)
    ResponseEntity<String> editTradepost(@RequestBody TradepostRequestData editRequestData);

    @RequestMapping(value = "/get-my-trade", method = RequestMethod.GET)
    ResponseEntity<List<MyTradeDTO>> getMyTradeList(@RequestParam("accountId") int accountId,
                                                    @RequestParam("status") String status,
                                                    @RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("sortType") int sortType);

    @RequestMapping(value = "/update-quantity", method = RequestMethod.POST)
    ResponseEntity<String> updateQuantity(@RequestParam("tradepostId") int tradepostId,
                                          @RequestParam("newQuantity") int quantity);
    @RequestMapping(value = "/delete-trade-post", method = RequestMethod.POST)
    ResponseEntity<String> deleteTradePost(@RequestParam("tradepostId") int tradepostId);

    @RequestMapping(value = "/view-trade-post", method = RequestMethod.GET)
    ResponseEntity<ViewTradepostDTO> viewTradePost(@RequestParam("tradepostId") int tradepostId);

    @RequestMapping(value = "/get-trade-listing", method = RequestMethod.GET)
    ResponseEntity<List<TradeListingDTO>> getTradeListing(@RequestParam("tradeType") String tradeType,
                                                          @RequestParam("pageNumber") int pageNumber,
                                                          @RequestParam("sortType") int sortType);


}
