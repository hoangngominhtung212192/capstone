package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.MyTradeDTO;
import com.tks.gwa.dto.TradeListingDTO;
import com.tks.gwa.dto.TradepostRequestData;
import com.tks.gwa.dto.ViewTradepostDTO;
import com.tks.gwa.entity.*;
import com.tks.gwa.repository.OrderRequestRepository;
import com.tks.gwa.repository.ProfileRepository;
import com.tks.gwa.repository.TradepostRepository;
import com.tks.gwa.repository.TradepostimageRepository;
import com.tks.gwa.service.TradepostService;
import com.tks.gwa.utils.DateStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TradepostServiceImpl implements TradepostService {

    @Autowired
    private TradepostRepository tradepostRepository;

    @Autowired
    private TradepostimageRepository tradepostimageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    //Save New Trade Post / Update Profile / Save Image List
    @Override
    public boolean postNewTradePost(TradepostRequestData tradePostData) {
        //get Trade Post entity from AddNewData
        Tradepost newTradepost = this.getTradepostEntity(tradePostData);
        if (newTradepost == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Tradepost data is null");
            return false;
        }

        Profile traderProfile = null;
        try {
            traderProfile = profileRepository.findProfileByEmail(tradePostData.getTraderEmail());
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Get Profile by email - Execute query error");
            e.printStackTrace();
            return false;
        }

        if (traderProfile == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Get Profile by email - Null result");
            return false;
        }

        //Set updated value to traderProfile
        traderProfile.setTel(tradePostData.getTraderPhone());
        traderProfile.setAddress(tradePostData.getTraderAddress());

        //Update traderProfile
        try {
            profileRepository.updateProfile(traderProfile);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Update profile - Execute query error");
            e.printStackTrace();
            return false;
        }


        //Set Account to newTradepost
        newTradepost.setAccount(traderProfile.getAccount());
        newTradepost.setPostedDate(DateStringConverter.dateConvertToString(new Date()));
        Tradepost tradepostResult = null;

        try {
            tradepostResult = tradepostRepository.addNewTradepost(newTradepost);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Add new trade post - Execute query error");
            e.printStackTrace();
            return false;
        }
        if (tradepostResult == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Add new trade post - Null result");
            return false;
        }

        //Save Tradepost Image
        String imgList[] = tradePostData.getImageUploadedList();
        try {
            saveImageByTradepost(imgList, tradepostResult);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Save Image - Execute query error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //get Trade Post using ID
    @Override
    public Tradepost getTradepostByID(int tradepostID) {
        return tradepostRepository.findTradepostById(tradepostID);
    }

    //Save Edit Trade Post / Update Profile / Delete Old Image / Save new Image List
    @Override
    public boolean editTradePost(TradepostRequestData tradePostData) {
        Tradepost editTradepost = this.getTradepostEntity(tradePostData);
        if (editTradepost == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Tradepost data is null");
            return false;
        }

        Profile traderProfile = null;
        try {
            traderProfile = profileRepository.findProfileByEmail(tradePostData.getTraderEmail());
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Get Profile by email - Execute query error");
            e.printStackTrace();
            return false;
        }

        if (traderProfile == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Get Profile by email - Null result");
            return false;
        }
        //Set updated value to traderProfile
        traderProfile.setTel(tradePostData.getTraderPhone());
        traderProfile.setAddress(tradePostData.getTraderAddress());

        //Update traderProfile
        try {
            profileRepository.updateProfile(traderProfile);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Update profile - Execute query error");
            e.printStackTrace();
            return false;
        }

        editTradepost.setLastModified(DateStringConverter.dateConvertToString(new Date()));
        Tradepost tradepostResult = null;

        try {
            tradepostResult = tradepostRepository.editTradepost(editTradepost);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Add new trade post - Execute query error");
            e.printStackTrace();
            return false;
        }
        if (tradepostResult == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Add new trade post - Null result");
            return false;
        }

        //Delete old image
        try {
            boolean delRe = tradepostimageRepository.deleteAllImageByTradepostId(tradepostResult.getId());
            if (!delRe) {
                System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Delete old Image - No Image has been deleted");
                return false;
            }

        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Delete old Image - Execute query error");
            e.printStackTrace();
            return false;

        }

        //Add new image
        String imgList[] = tradePostData.getImageUploadedList();
        try {
            saveImageByTradepost(imgList, tradepostResult);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Save Image - Execute query error");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //Get Trade Post List by Account / Get All Request Per Trade post / Get Thumbnail Image per Trade post/ On pageNumber/Sorting by
    @Override
    public List<MyTradeDTO> getMyTradeData(int accountId, String status, int pageNumber, int sortType) {
        List<MyTradeDTO> result = new ArrayList<MyTradeDTO>();
        List<Tradepost> tradepostList = tradepostRepository.getTradepostPerPageWithSortingAndStatusByAccount(accountId, status, pageNumber, sortType);
        if (tradepostList != null) {
            for (int i = 0; i < tradepostList.size(); i++) {
                Tradepost tradepost = tradepostList.get(i);
                String thumbnailImgUrl = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepost.getId());
                int numOfSucceedRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepost.getId(), AppConstant.SUCCEED_STATUS);
                int numOfPendingRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepost.getId(), AppConstant.PENDING_STATUS);
                int numOfOnPaymentRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepost.getId(), AppConstant.APPROVED_STATUS);
                int totalPage = tradepostRepository.countNumberOfTradepostByStatusAndAccount(status, accountId);
                MyTradeDTO myTradeDTO = new MyTradeDTO(tradepost, numOfSucceedRequest, numOfPendingRequest, numOfOnPaymentRequest, thumbnailImgUrl, totalPage);
                result.add(myTradeDTO);
            }

        }

        return result;
    }

    @Override
    public boolean updateTradePostQuantity(int tradepostId, int quantity) {
        return tradepostRepository.updateQuantityById(tradepostId, quantity);
    }

    @Override
    public boolean deleteTradePost(int tradepostId) {
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        if (tradepost == null) return false;
        tradepostimageRepository.deleteAllImageByTradepostId(tradepostId);
        return tradepostRepository.removeTradepost(tradepost);
    }

    @Override
    public ViewTradepostDTO getViewTradePostData(int tradepostId) {
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        if (tradepost != null){
            try {
                String[] images = this.getImageArrayByTradepostId(tradepostId);
                ViewTradepostDTO dto = new ViewTradepostDTO(tradepost,images);
                return dto;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<TradeListingDTO> getTradeListingData(String tradeType, int pageNumber, int sortType) {
        List<TradeListingDTO> listingDTOList = new ArrayList<TradeListingDTO>();
        List<Tradepost> tradepostList = tradepostRepository.getTradePostPerPageWithSortingByTradeType(tradeType,pageNumber,sortType);
        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            String thumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepost.getId());
            int totalPage = tradepostRepository.countNumberOfTradepostByTradeType(tradeType);
            TradeListingDTO dto = new TradeListingDTO(tradepost,thumbnail,totalPage);
            listingDTOList.add(dto);
        }
        return listingDTOList;
    }

    //Get Trade post data for edit form
    @Override
    public TradepostRequestData getTradepostEditFormData(int tradepostId) {
        Tradepost tradepost = null;
        try {
            tradepost = tradepostRepository.findTradepostById(tradepostId);
            if (tradepost == null) {
                System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trade post data - Null Result");
                return null;
            }
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trade post data - Execute query error");
            e.printStackTrace();
            return null;
        }

        TradepostRequestData result = new TradepostRequestData();
        try {
            String imgList[] = getImageArrayByTradepostId(tradepostId);
            if (imgList == null) {
                System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trade post images - Null Result");
                return null;
            }
            result.setImageUploadedList(imgList);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trade post images - Execute query error");
            e.printStackTrace();
            return null;
        }

        result.setTradeBrand(tradepost.getBrand());
        result.setTradeId(tradepost.getId());
        result.setTradeCondition((tradepost.getCondition() == AppConstant.TRADEPOST.CONDITION_NEW_INT)
                ? AppConstant.TRADEPOST.CONDITION_NEW : AppConstant.TRADEPOST.CONDITION_USED);
        result.setTradeDesc(tradepost.getDescription());
        result.setTradeModel(tradepost.getModel());
        result.setTradeNegotiable((tradepost.getPriceNegotiable() == AppConstant.TRADEPOST.NEGOTIABLE_ON_INT)
                ? AppConstant.TRADEPOST.NEGOTIABLE_ON : AppConstant.TRADEPOST.NEGOTIABLE_OFF);
        result.setTradePrice(tradepost.getPrice());
        result.setTradeQuantity(tradepost.getQuantity());
        result.setTraderAddress(tradepost.getLocation());
        Profile profile = null;
        try {
            profile = profileRepository.findProfileByAccountID(tradepost.getAccount().getId());
            if (profile == null) {
                System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trader profile - Null result");
                return null;
            }
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][GET FORM DATA]: Get Trader profile - Execute query error");
            e.printStackTrace();
            return null;
        }
        result.setTraderEmail(profile.getEmail());
        result.setTraderName(profile.getFirstName() + " " + profile.getMiddleName() + " " + profile.getLastName());
        result.setTraderPhone(profile.getTel());
        result.setTradeTitle(tradepost.getTitle());
        result.setTradeType((tradepost.getTradeType() == AppConstant.TRADEPOST.TYPE_SELL_INT)
                ? AppConstant.TRADEPOST.TYPE_SELL : AppConstant.TRADEPOST.TYPE_BUY);
        result.setTraderId(tradepost.getAccount().getId());

        return result;
    }

    //Save image list with Trade post
    public void saveImageByTradepost(String img[], Tradepost tradepost) throws Exception {
        for (int i = 0; i < img.length; i++) {
            Tradepostimage saveImage = new Tradepostimage();
            saveImage.setImageUrl(img[i]);
            saveImage.setTradepost(tradepost);
            tradepostimageRepository.addImage(saveImage);
        }
    }

    //Get All Images Url Array by Trade post
    public String[] getImageArrayByTradepostId(int tradepostId) throws Exception {
        List<Tradepostimage> imgList = tradepostimageRepository.getImageByTradepostId(tradepostId);
        System.out.println(imgList);
        String[] imgArr = new String[imgList.size()];
        for (int i = 0; i < imgList.size(); i++) {
            imgArr[i] = imgList.get(i).getImageUrl();
        }
        return imgArr;
    }


    //Transform TradepostRequestData to Tradepost Entity
    public Tradepost getTradepostEntity(TradepostRequestData tradepostRequestData) {
        Tradepost result = new Tradepost();
        int tradeID = tradepostRequestData.getTradeId();

        if (tradeID > 0) {
            result = tradepostRepository.findTradepostById(tradeID);

        }
        result.setApprovalStatus(AppConstant.PENDING_STATUS);
        result.setBrand(tradepostRequestData.getTradeBrand());
        int tradeConditionInt = AppConstant.TRADEPOST.CONDITION_USED_INT;
        if (tradepostRequestData.getTradeCondition().equals(AppConstant.TRADEPOST.CONDITION_NEW)) {
            tradeConditionInt = AppConstant.TRADEPOST.CONDITION_NEW_INT;
        }
        result.setCondition(tradeConditionInt);
        result.setDescription(tradepostRequestData.getTradeDesc());
        result.setLocation(tradepostRequestData.getTraderAddress());
        result.setModel(tradepostRequestData.getTradeModel());
        result.setPrice(tradepostRequestData.getTradePrice());
        int isNegotiable = AppConstant.TRADEPOST.NEGOTIABLE_ON_INT;
        if (tradepostRequestData.getTradeNegotiable() == null) {
            isNegotiable = AppConstant.TRADEPOST.NEGOTIABLE_OFF_INT;
        }
        result.setPriceNegotiable(isNegotiable);
        result.setQuantity(tradepostRequestData.getTradeQuantity());
        result.setTitle(tradepostRequestData.getTradeTitle());
        int tradeTypeInt = AppConstant.TRADEPOST.TYPE_SELL_INT;
        if (tradepostRequestData.getTradeType().equals(AppConstant.TRADEPOST.TYPE_BUY)) {
            tradeTypeInt = AppConstant.TRADEPOST.TYPE_BUY_INT;
        }
        result.setTradeType(tradeTypeInt);
        return result;
    }
}
