package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.dto.AddNewTradeData;
import com.tks.gwa.entity.Account;
import com.tks.gwa.entity.Profile;
import com.tks.gwa.entity.Tradepost;
import com.tks.gwa.entity.Tradepostimage;
import com.tks.gwa.repository.AccountRepository;
import com.tks.gwa.repository.ProfileRepository;
import com.tks.gwa.repository.TradepostRepository;
import com.tks.gwa.repository.TradepostimageRepository;
import com.tks.gwa.repository.repositoryImpl.ProfileRepositoryImpl;
import com.tks.gwa.service.TradepostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradepostServiceImpl implements TradepostService {

    @Autowired
    private TradepostRepository tradepostRepository;

    @Autowired
    private TradepostimageRepository tradepostimageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public boolean postNewTradePost(AddNewTradeData tradePostData) {
        //get Trade Post entity from AddNewData
        Tradepost newTradepost = tradePostData.getTradepostEntity();
        if (newTradepost == null){
            System.out.println("Error on getting Trade Post data from source");
            return false;
        }

        Profile traderProfile = profileRepository.findProfileByEmail(tradePostData.getTraderEmail());
        if(traderProfile == null){
            System.out.println("Error on getting profile by Email");
            return false;
        }

        //Set updated value to traderProfile
        traderProfile.setTel(tradePostData.getTraderPhone());
        traderProfile.setAddress(tradePostData.getTraderAddress());

        //Update traderProfile
        profileRepository.update(traderProfile);

        //Set Account to newTradepost
        newTradepost.setAccount(traderProfile.getAccount());
        Tradepost tradepostResult = tradepostRepository.addNewTradepost(newTradepost);
        if(tradepostResult == null){
            System.out.println("Error on Save Trade Post");
            return false;
        }

        //Save Tradepost Image
        String imgList[] = tradePostData.getImageUploadedList();
        for (int i = 0; i < imgList.length; i++) {
            Tradepostimage saveImage = new Tradepostimage();
            saveImage.setImageUrl(imgList[i]);
            saveImage.setTradepost(tradepostResult);
            tradepostimageRepository.addImage(saveImage);
        }


        return true;
    }
}
