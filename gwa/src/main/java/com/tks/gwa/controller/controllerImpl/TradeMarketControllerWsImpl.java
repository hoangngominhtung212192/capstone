package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.TradeMarketControllerWs;
import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradeListingDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.dto.ViewTradepostDTO;
import com.tks.gwa.service.TradepostService;
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
    private TradepostService tradepostService;

    @Override
    public ResponseEntity<String> addTradepost(@RequestBody TradepostRequestData addRequestData) {
        System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Accessing" );
        if (addRequestData == null) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Request Data is null" );
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("---------------REQUEST ADD FORM DATA:");
        addRequestData.printContent();
        boolean result = false;
        try {
            result = tradepostService.postNewTradePost(addRequestData);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]:  ERROR on EXECUTE database server" );
            ex.printStackTrace();
        }

        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]:  ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when submit your trade post! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][ADD TRADE POST REQUEST]: Successfully!" );
        return ResponseEntity.ok("Your trade post has been posted successfully! Please wait admin approve it!");
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
            formData = tradepostService.getTradepostEditFormData(tradepostID);
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
    public ResponseEntity<String> editTradepost(@RequestBody TradepostRequestData editRequestData) {
        System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Accessing" );
        if (editRequestData == null) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Request Data is null" );
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("---------------REQUEST EDIT FORM DATA:");
        editRequestData.printContent();
        boolean result = false;
        try {
            result = tradepostService.editTradePost(editRequestData);
        } catch (Exception ex) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: ERROR on EXECUTE database server" );
            ex.printStackTrace();
            return new ResponseEntity<String>(
                    "Something went wrong when submit your trade post! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!result) {
            System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: ERROR on EXECUTE database server - NULL RESULT" );
            return new ResponseEntity<String>(
                    "Something went wrong when submit your trade post! Please contact Administrator for more information"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][EDIT REQUEST]: Successfully !" );
        return ResponseEntity.ok("Your trade post has been edited successfully! Please wait admin approve it!");
    }

    @Override
    public ResponseEntity<List<MyTradeDTO>> getMyTradeList(@RequestParam("accountId") int accountId,
                                                           @RequestParam("status") String status,
                                                           @RequestParam("pageNumber") int pageNumber,
                                                           @RequestParam("sortType") int sortType) {
        System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Accessing" );
        List<MyTradeDTO> myTradeDTOList = new ArrayList<MyTradeDTO>();
        if (accountId <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Request Data is null" );
            return (ResponseEntity<List<MyTradeDTO>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][GET MY TRADE LIST]: Get all trade post from accountId: " + accountId );
        try {
            myTradeDTOList = tradepostService.getMyTradeData(accountId, status, pageNumber, sortType);
            for (int i = 0; i < myTradeDTOList.size(); i++) {
                myTradeDTOList.get(i).getMyTradePost().setAccount(null);
            }
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET MY TRADE LIST]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<MyTradeDTO>>(myTradeDTOList,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][GET MY TRADE LIST]: Successfully!" );
        return new ResponseEntity<List<MyTradeDTO>>(myTradeDTOList,HttpStatus.OK);
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
            result = tradepostService.updateTradePostQuantity(tradepostId,quantity);
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
            result = tradepostService.deleteTradePost(tradepostId);
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
            dto = tradepostService.getViewTradePostData(tradepostId);
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
        return new ResponseEntity<ViewTradepostDTO>(dto, HttpStatus.valueOf(500));
    }

    @Override
    public ResponseEntity<List<TradeListingDTO>> getTradeListing(@RequestParam("tradeType") String tradeType,
                                                                 @RequestParam("pageNumber") int pageNumber,
                                                                 @RequestParam("sortType") int sortType) {


        System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Accessing" );
        List<TradeListingDTO> listingDTOList = new ArrayList<TradeListingDTO>();
        if (pageNumber <= 0){
            System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Request Data is null" );
            return (ResponseEntity<List<TradeListingDTO>>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        System.out.println("[TRADEMARKET CONTROLLER][GET TRADE LISTING]: Trade Type: " + tradeType );
        try {
            listingDTOList = tradepostService.getTradeListingData(tradeType,pageNumber,sortType);
            for (int i = 0; i < listingDTOList.size(); i++) {
                listingDTOList.get(i).getTradepost().getAccount().setPassword(null);
                listingDTOList.get(i).getTradepost().getAccount().setAvatar(null);
                listingDTOList.get(i).getTradepost().getAccount().setRole(null);
                listingDTOList.get(i).getTradepost().getAccount().setMessage(null);
            }
        }catch (Exception e){
            System.out.println("[TRADEPOST CONTROLLER][GET TRADE LISTING]: ERROR on EXECUTE database server" );
            e.printStackTrace();
            return new ResponseEntity<List<TradeListingDTO>>(listingDTOList,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("[TRADEPOST CONTROLLER][GET TRADE LISTING]: Successfully!" );
        return new ResponseEntity<List<TradeListingDTO>>(listingDTOList,HttpStatus.OK);
    }

}
