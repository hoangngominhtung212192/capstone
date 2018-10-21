package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.TradepostControllerWs;
import com.tks.gwa.dto.AddNewTradeData;
import com.tks.gwa.service.TradepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradepostControllerWsImpl implements TradepostControllerWs {
    @Autowired
    private TradepostService tradepostService;
    @Override
    public ResponseEntity<String> addTradepost(@RequestBody AddNewTradeData addNewTradeData) {
        if (addNewTradeData == null){
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
        addNewTradeData.printContent();
        boolean result = tradepostService.postNewTradePost(addNewTradeData);
        if (!result){
            return new ResponseEntity<>("Something went wrong when submit your trade post",HttpStatus.valueOf(400));
        }
        return ResponseEntity.ok("Your trade post has been posted successfully! Please wait admin approve it!");
    }
}
