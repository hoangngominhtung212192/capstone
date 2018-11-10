package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.TradeMarketControllerWs;
import com.tks.gwa.dto.NewOrderDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.dto.ViewTradepostDTO;
import com.tks.gwa.entity.Orderrequest;
import com.tks.gwa.service.TrademarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TradeMarketControllerWsImpl implements TradeMarketControllerWs {
    @Autowired
    private TrademarketService trademarketService;

    @Override
    public ResponseEntity<Integer> addTradepost(@RequestBody TradepostRequestData addRequestData) {
        System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Accessing" );
        if (addRequestData == null) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Request Data is null" );
            return (ResponseEntity<Integer>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("---------------REQUEST ADD FORM DATA:");
        addRequestData.printContent();
        int result = -1;
        try {
            result = trademarketService.postNewTradePost(addRequestData);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]:  ERROR on EXECUTE database server" );
            ex.printStackTrace();
        }

        if (result == -1) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]:  ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<Integer>(-1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Successfully!" );
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<TradepostRequestData> getEditFormData(@RequestParam("tradepostID") String tradepostIDStr) {
        System.out.println("[TRADEPOST CONTROLLER][GET EDIT FORM REQUEST]: Accessing" );
        TradepostRequestData formData = null;
        int tradepostID = 0;
        try {
            tradepostID = Integer.parseInt(tradepostIDStr);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][GET EDIT FORM REQUEST]:  TradepostID is invalid!" );
            ex.printStackTrace();
            return new ResponseEntity<TradepostRequestData>(formData, HttpStatus.valueOf(400));
        }
        try {
            formData = trademarketService.getTradepostEditFormData(tradepostID);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][GET EDIT FORM REQUEST]:  ERROR on EXECUTE database server" );
            ex.printStackTrace();
            return new ResponseEntity<TradepostRequestData>(formData, HttpStatus.valueOf(500));
        }
        if (formData != null){
            System.out.println("--------------RESPONSE FORM DATA:");
            formData.printContent();
            System.out.println("[TRADEPOST CONTROLLER][GET EDIT FORM REQUEST]:   Successfully !" );
            return new ResponseEntity<TradepostRequestData>(formData, HttpStatus.OK);
        }
        System.out.println("[TRADEPOST CONTROLLER][GET EDIT FORM REQUEST]:  ERROR on EXECUTE database server - NULL RESULT" );
        return new ResponseEntity<TradepostRequestData>(formData, HttpStatus.valueOf(500));
    }

    @Override
    public ResponseEntity<Integer> editTradepost(@RequestBody TradepostRequestData editRequestData) {
        System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Accessing" );
        if (editRequestData == null) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Request Data is null" );
            return (ResponseEntity<Integer>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("---------------REQUEST EDIT FORM DATA:");
        editRequestData.printContent();
        int result = -1;
        try {
            result = trademarketService.editTradePost(editRequestData);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: ERROR on EXECUTE database server" );
            ex.printStackTrace();
            return new ResponseEntity<Integer>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result == -1) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<Integer>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Successfully !" );
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<String> updateImagesToDatabase(@RequestParam("tradepostId") int tradepostId,
                                                         @RequestParam("images[]") String[] images) {
        System.out.println(images);
        boolean result = trademarketService.updateTradepostImagesByTradepostID(tradepostId, images);
        if (!result){
            return new ResponseEntity<String>(
                    "Something went wrong when update images to database! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Add images link to database success");
    }

    @Override
    public ResponseEntity<String> updateQuantity(@RequestParam("tradepostId") int tradepostId,
                                                 @RequestParam("newQuantity") int quantity) {
        System.out.println("[TRADEPOST CONTROLLER][UPDATE QUANTITY]: Accessing" );
        if (tradepostId == 0 || quantity ==0) {
            System.out.println("[TRADEPOST CONTROLLER][UPDATE QUANTITY]: Request Data is null" );
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.updateTradePostQuantity(tradepostId,quantity);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][UPDATE QUANTITY]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when update quantity! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][UPDATE QUANTITY]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when update quantity! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][UPDATE QUANTITY]: Successfully !" );
        return ResponseEntity.ok("Updated quantity successfully!");
    }

    @Override
    public ResponseEntity<String> deleteTradePost(@RequestParam("tradepostId") int tradepostId) {
        System.out.println("[TRADEPOST CONTROLLER][DELETE TRADE POST]: Accessing" );
        if (tradepostId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][DELETE TRADE POST]: Request Data is null" );
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.deleteTradePost(tradepostId);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][DELETE TRADE POST]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when delete trade post! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][DELETE TRADE POST]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when delete trade post! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][DELETE TRADE POST]: Successfully !" );
        return ResponseEntity.ok("Delete trade post successfully!");
    }

    @Override
    public ResponseEntity<ViewTradepostDTO> viewTradePost(@RequestParam("tradepostId") int tradepostId) {
        System.out.println("[TRADEPOST CONTROLLER][VIEW TRADE POST]: Accessing" );
        ViewTradepostDTO dto = null;
        if (tradepostId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][VIEW TRADE POST]: Request Data is null" );
            return (ResponseEntity<ViewTradepostDTO>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        try {
            dto = trademarketService.getViewTradePostData(tradepostId);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][VIEW TRADE POST:  ERROR on EXECUTE database server" );
            ex.printStackTrace();
            return new ResponseEntity<ViewTradepostDTO>(dto, HttpStatus.valueOf(500));
        }
        if (dto != null){
            dto.getTradepost().getAccount().setPassword(null);
            System.out.println("[TRADEPOST CONTROLLER][VIEW TRADE POST]:   Successfully !" );
            return new ResponseEntity<ViewTradepostDTO>(dto, HttpStatus.OK);
        }
        System.out.println("[TRADEPOST CONTROLLER][GVIEW TRADE POST]:  ERROR on EXECUTE database server - NULL RESULT" );
        return new ResponseEntity<ViewTradepostDTO>(dto, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Object>> getMyTradeList(@RequestParam("accountId") int accountId,
                                                           @RequestParam("status") String status,
                                                           @RequestParam("pageNumber") int pageNumber,
                                                           @RequestParam("sortType") int sortType) {
        System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Accessing" );
        List<Object> result = new ArrayList<Object>();
        if (accountId <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Request Data is null" );
            return (ResponseEntity<List<Object>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Get all trade post from accountId: " + accountId );
        try {
            result = trademarketService.getMyTradeData(accountId, status, pageNumber, sortType);

        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET MY TRADE LIST]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][GET MY TRADE LIST]: Successfully!" );
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> searchMyTradeList(@RequestParam("accountId") int accountId,
                                                              @RequestParam("status") String status,
                                                              @RequestParam("pageNumber") int pageNumber,
                                                              @RequestParam("sortType") int sortType,
                                                              @RequestParam("keyword") String keyword) {
        System.out.println("[TRADEMARKET CONTROLLER][SEARCH MY TRADE LIST]: Accessing" );
        List<Object> result = new ArrayList<Object>();
        if (accountId <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][SEARCH MY TRADE LIST]: Request Data is null" );
            return (ResponseEntity<List<Object>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][SEARCH MY TRADE LIST]: Search all trade post from accountId: "
                + accountId + " - Keyword: " + keyword);
        try {
            result = trademarketService.getSearchMyTradeData(accountId,status,pageNumber,sortType,keyword);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][SEARCH MY TRADE LIST]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][SEARCH MY TRADE LIST]: Successfully!" );
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<Object>> getTradeListing(@RequestParam("tradeType") String tradeType,
                                                                 @RequestParam("pageNumber") int pageNumber,
                                                                 @RequestParam("sortType") int sortType) {


        System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Accessing" );
        List<Object> result = new ArrayList<Object>();
        if (pageNumber <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Request Data is null" );
            return (ResponseEntity<List<Object>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Trade Type: " + tradeType );
        try {
            result = trademarketService.getTradeListingData(tradeType,pageNumber,sortType);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET TRADE LISTING]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][GET TRADE LISTING]: Successfully!" );
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getSearchTradeListing(@RequestParam("tradeType") String tradeType,
                                                                       @RequestParam("pageNumber") int pageNumber,
                                                                       @RequestParam("sortType") int sortType,
                                                                       @RequestParam("keyword") String keyword) {

        System.out.println("[TRADEMARKET CONTROLLER][SEARCH TRADE LISTING]: Accessing" );
        List<Object> result = new ArrayList<Object>();
        if (pageNumber <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][SEARCH TRADE LISTING]: Request Data is null" );
            return (ResponseEntity<List<Object>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][SEARCH TRADE LISTING]: Keyword: " + keyword );
        try {
            result = trademarketService.getSearchTradeListingData(tradeType,pageNumber,sortType,keyword);

        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][SEARCH TRADE LISTING]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][SEARCH TRADE LISTING]: Successfully!" );
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getSearchTradeListingWithLocation(String tradeType, int sortType, String keyword, String location, long range) {
        System.out.println("[TRADEMARKET CONTROLLER][SEARCH TRADE LISTING WITH LOCATION]: Accessing" );
        List<Object> result = new ArrayList<Object>();
        try {
            result = trademarketService.getSearchTradeListingWithLocationData(tradeType,sortType,keyword,location,range);

        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][SEARCH TRADE LISTING WITH LOCATION]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][SEARCH TRADE LISTING WITH LOCATION]: Successfully!" );
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<Object>> getOrderByTradepost(@RequestParam("tradepostId") int tradepostId,
                                                            @RequestParam("status") String status,
                                                            @RequestParam("pageNumber") int pageNumber,
                                                            @RequestParam("sortType") int sortType) {
        List<Object> result = new ArrayList<Object>();
        try {
            result = trademarketService.getAllRequestByTradepost(tradepostId,status,pageNumber,sortType);

        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET ALL ORDER BY TRADEPOST]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> acceptOrder(int orderId) {
        if (orderId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][ACCEPT ORDER]: Request Data is null" );
            return new ResponseEntity<String>(
                    "OrderId is Null"
                    , HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.acceptOrder(orderId);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][ACCEPT ORDER]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when accept order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][ACCEPT ORDER]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when accept order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Order Accepted!");
    }

    @Override
    public ResponseEntity<String> declineOrder(int orderId, String reason) {
        if (orderId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][DECLINE ORDER]: Request Data is null" );
            return new ResponseEntity<String>(
                    "OrderId is Null"
                    , HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.declineOrder(orderId, reason);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][DECLINE ORDER]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when decline order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][DECLINE ORDER]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when decline order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Order Declined!");
    }

    @Override
    public ResponseEntity<String> confirmSucceedOrder(int orderId) {
        if (orderId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][CONFIRM SUCCEED ORDER]: Request Data is null" );
            return new ResponseEntity<String>(
                    "OrderId is Null"
                    , HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.confirmOrderSucceed(orderId);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][CONFIRM SUCCEED ORDER]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when confirm succeed order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][CONFIRM SUCCEED ORDER]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when confirm succeed order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Order confirm succeeded!");
    }

    @Override
    public ResponseEntity<String> cancelOrder(int orderId, String reason) {
        if (orderId == 0) {
            System.out.println("[TRADEPOST CONTROLLER][CANCEL ORDER]: Request Data is null" );
            return new ResponseEntity<String>(
                    "OrderId is Null"
                    , HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.cancelOrder(orderId, reason);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][CANCEL ORDER]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when cancel order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][CANCEL ORDER]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when cancel order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Order Cancelled!");
    }

    @Override
    public ResponseEntity<String> sendOrder(@RequestBody NewOrderDTO newOrderData) {
        if (newOrderData == null) {
            System.out.println("[TRADEPOST CONTROLLER][SEND ORDER]: Request Data is null" );
            return new ResponseEntity<String>(
                    "Order data is Null"
                    , HttpStatus.BAD_REQUEST);
        }
        boolean result = false;
        try{
            result = trademarketService.sendOrder(newOrderData);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][SEND ORDER]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when send order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][SEND ORDER]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when send order! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Your order has been sent to trade post owner!");
    }

    @Override
    public ResponseEntity<String> reportTrade(@RequestParam("tradepostId") int tradepostId,
                                              @RequestParam("reason") String reason,
                                              @RequestParam("phone") String phone,
                                              @RequestParam("email") String email) {
        boolean result = false;
        try{
            result = trademarketService.reportTrade(tradepostId, reason, phone, email);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][REPORT TRADE]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when report trade! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            return ResponseEntity.ok("You already reported this trade.");
        }
        return ResponseEntity.ok("Your report has been submitted");
    }

    @Override
    public ResponseEntity<List<Object>> getMyOrder(@RequestParam("accountId") int accountId,
                                                   @RequestParam("status") String status,
                                                   @RequestParam("pageNumber") int pageNumber,
                                                   @RequestParam("sortType") int sortType) {

        List<Object> result = new ArrayList<Object>();
        try {
            result = trademarketService.getMyOrderData(accountId,status,pageNumber,sortType);

        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET MY ORDER DATA]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<Object>>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Object>>(result,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> ratingTrade(@RequestParam("orderId") int orderId,
                                              @RequestParam("feedbackType") int feedbackType,
                                              @RequestParam("rating") int value,
                                              @RequestParam("comment") String comment) {
        boolean result = false;
        try{
            result = trademarketService.ratingTrade(orderId,feedbackType,value,comment);
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][RATING TRADE]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when report trade! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!result) {
            return ResponseEntity.ok("You already rating this trade.");
        }
        return ResponseEntity.ok("Your rating has been submitted");
    }


}
