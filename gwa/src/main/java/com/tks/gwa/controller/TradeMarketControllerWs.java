package com.tks.gwa.controller;

import com.tks.gwa.dto.*;
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

    @RequestMapping(value = "/update-quantity", method = RequestMethod.POST)
    ResponseEntity<String> updateQuantity(@RequestParam("tradepostId") int tradepostId,
                                          @RequestParam("newQuantity") int quantity);

    @RequestMapping(value = "/delete-trade-post", method = RequestMethod.POST)
    ResponseEntity<String> deleteTradePost(@RequestParam("tradepostId") int tradepostId);

    @RequestMapping(value = "/view-trade-post", method = RequestMethod.GET)
    ResponseEntity<ViewTradepostDTO> viewTradePost(@RequestParam("tradepostId") int tradepostId);

    @RequestMapping(value = "/get-my-trade", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getMyTradeList(@RequestParam("accountId") int accountId,
                                                    @RequestParam("status") String status,
                                                    @RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("sortType") int sortType);

    @RequestMapping(value = "/search-my-trade", method = RequestMethod.GET)
    ResponseEntity<List<Object>> searchMyTradeList(@RequestParam("accountId") int accountId,
                                                       @RequestParam("status") String status,
                                                       @RequestParam("pageNumber") int pageNumber,
                                                       @RequestParam("sortType") int sortType,
                                                       @RequestParam("keyword") String keyword);


    @RequestMapping(value = "/get-trade-listing", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getTradeListing(@RequestParam("tradeType") String tradeType,
                                                          @RequestParam("pageNumber") int pageNumber,
                                                          @RequestParam("sortType") int sortType);

    @RequestMapping(value = "/search-trade-listing", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getSearchTradeListing(@RequestParam("tradeType") String tradeType,
                                                                @RequestParam("pageNumber") int pageNumber,
                                                                @RequestParam("sortType") int sortType,
                                                                @RequestParam("keyword") String keyword);
    @RequestMapping(value = "/search-location-trade-listing", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getSearchTradeListingWithLocation(@RequestParam("tradeType") String tradeType,
                                                                   @RequestParam("sortType") int sortType,
                                                                   @RequestParam("keyword") String keyword,
                                                                   @RequestParam("location") String location,
                                                                   @RequestParam("range") long range);

    @RequestMapping(value = "/get-order-by-trade-post", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getOrderByTradepost(@RequestParam("tradepostId") int tradepostId,
                                                     @RequestParam("status") String status,
                                                     @RequestParam("pageNumber") int pageNumber,
                                                     @RequestParam("sortType") int sortType);

    @RequestMapping(value = "/accept-order", method = RequestMethod.POST)
    ResponseEntity<String> acceptOrder(@RequestParam("orderId") int orderId);

    @RequestMapping(value = "/decline-order", method = RequestMethod.POST)
    ResponseEntity<String> declineOrder(@RequestParam("orderId") int orderId, @RequestParam("reason") String reason);

    @RequestMapping(value = "/confirm-succeed-order", method = RequestMethod.POST)
    ResponseEntity<String> confirmSucceedOrder(@RequestParam("orderId") int orderId);

    @RequestMapping(value = "/cancel-order", method = RequestMethod.POST)
    ResponseEntity<String> cancelOrder(@RequestParam("orderId") int orderId, @RequestParam("reason") String reason);

    @RequestMapping(value = "/send-order", method = RequestMethod.POST)
    ResponseEntity<String> sendOrder(@RequestBody NewOrderDTO newOrderData);

    @RequestMapping(value = "/report-trade", method = RequestMethod.POST)
    ResponseEntity<String> reportTrade(@RequestParam("tradepostId") int tradepostId,
                                       @RequestParam("reason") String reason,
                                       @RequestParam("phone") String phone,
                                       @RequestParam("email") String email);

    @RequestMapping(value = "/get-my-order", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getMyOrder(@RequestParam("accountId") int accountId,
                                            @RequestParam("status") String status,
                                            @RequestParam("pageNumber") int pageNumber,
                                            @RequestParam("sortType") int sortType);


    @RequestMapping(value = "/rating-trade", method = RequestMethod.POST)
    ResponseEntity<String> ratingTrade(@RequestParam("orderId") int orderId,
                                       @RequestParam("feedbackType") int feedbackType,
                                       @RequestParam("rating") int value,
                                       @RequestParam("comment") String comment);



}
