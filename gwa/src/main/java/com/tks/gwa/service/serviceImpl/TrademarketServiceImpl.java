package com.tks.gwa.service.serviceImpl;

import com.google.maps.model.LatLng;
import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.*;
import com.tks.gwa.entity.*;
import com.tks.gwa.firebase.PushNotification;
import com.tks.gwa.repository.*;
import com.tks.gwa.service.TrademarketService;
import com.tks.gwa.utils.DatetimeHelper;
import com.tks.gwa.utils.GoogleMapHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class TrademarketServiceImpl implements TrademarketService {

    @Autowired
    private TradepostRepository tradepostRepository;

    @Autowired
    private TradepostimageRepository tradepostimageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private TradereportRepository tradereportRepository;

    @Autowired
    private  TraderatingRepository traderatingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PushNotification pushNotification;

    @Autowired
    private TokenRepository tokenRepository;

    //get Trade Post using ID
    @Override
    public Tradepost getTradepostByID(int tradepostID) {
        return tradepostRepository.findTradepostById(tradepostID);
    }



    /******************************* Method CRUD on TRADEPOST ************************************************/
    //ADD NEW TRADEPOST
    @Override
    public int postNewTradePost(TradepostRequestData tradePostData) {
        //get Trade Post entity from AddNewData
        Tradepost newTradepost = this.getTradepostEntity(tradePostData);
        if (newTradepost == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Tradepost data is null");
            return -1;
        }

        Profile traderProfile = null;
        try {
            traderProfile = profileRepository.findProfileByEmail(tradePostData.getTraderEmail());
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Get Profile by email - Execute query error");
            e.printStackTrace();
            return -1;
        }

        if (traderProfile == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Get Profile by email - Null result");
            return -1;
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
            return -1;
        }


        //Set Account to newTradepost
        newTradepost.setAccount(traderProfile.getAccount());
        newTradepost.setPostedDate(DatetimeHelper.dateConvertToString(new Date()));
        Tradepost tradepostResult = null;

        try {
            tradepostResult = tradepostRepository.addNewTradepost(newTradepost);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Add new trade post - Execute query error");
            e.printStackTrace();
            return -1;
        }
        if (tradepostResult == null) {
            System.out.println("--[TRADEPOST SERVICE][POST TRADE]: Add new trade post - Null result");
            return -1;
        }

        return tradepostResult.getId();
    }



    //GET TRADEPOST EDIT DATA
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
        result.setTraderLatlng(tradepost.getLatlng());
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

    //UPDATE TRADEPOST DATA
    @Override
    public int editTradePost(TradepostRequestData tradePostData) {
        Tradepost editTradepost = this.getTradepostEntity(tradePostData);
        if (editTradepost == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Tradepost data is null");
            return -1;
        }

        Profile traderProfile = null;
        try {
            traderProfile = profileRepository.findProfileByEmail(tradePostData.getTraderEmail());
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Get Profile by email - Execute query error");
            e.printStackTrace();
            return -1;
        }

        if (traderProfile == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Get Profile by email - Null result");
            return -1;
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
            return -1;
        }

        editTradepost.setLastModified(DatetimeHelper.dateConvertToString(new Date()));
        Tradepost tradepostResult = null;

        try {
            tradepostResult = tradepostRepository.updateTradepost(editTradepost);
        } catch (Exception e) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Add new trade post - Execute query error");
            e.printStackTrace();
            return -1;
        }
        if (tradepostResult == null) {
            System.out.println("--[TRADEPOST SERVICE][EDIT TRADE]: Add new trade post - Null result");
            return -1;
        }


        return tradepostResult.getId();
    }


    //UPDATE IMAGE LIST FOR TRADE POST
    @Override
    public boolean updateTradepostImagesByTradepostID(int tradepostId, String[] images) {
        //delete all image
        tradepostimageRepository.deleteAllImageByTradepostId(tradepostId);
        //get tradepost
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);

        //save image
        for (int i = 0; i < images.length; i++) {
            Tradepostimage tradepostimage = new Tradepostimage();
            tradepostimage.setImageUrl(images[i]);
            tradepostimage.setTradepost(tradepost);
            tradepostimageRepository.addImage(tradepostimage);
        }
        //get list delete on server


        return true;
    }

    //UPDATE QUANTITY TRADEPOST WITH ID
    @Override
    public boolean updateTradePostQuantity(int tradepostId, int quantity) {
        return tradepostRepository.updateQuantityById(tradepostId, quantity);
    }

    //DELETE A TRADEPOST WITH ID
    @Override
    public boolean deleteTradePost(int tradepostId) {
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        if (tradepost == null) return false;
        tradepostimageRepository.deleteAllImageByTradepostId(tradepostId);
        return tradepostRepository.removeTradepost(tradepost);
    }



    /******************************* Method on VIEW DETAILS Page ************************************************/

    //GET DATA IN PAGE VIEW DETAILS
    @Override
    public ViewTradepostDTO getViewTradePostData(int tradepostId) {
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        if (tradepost != null) {
            try {
                String[] images = this.getImageArrayByTradepostId(tradepostId);
                int totalOrder = orderRequestRepository.getAllRequestByTradepostId(tradepostId).size();
                ViewTradepostDTO dto = new ViewTradepostDTO(tradepost,totalOrder,images);
                return dto;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //LOAD ALL REQUEST BY TRADE POST ID
    @Override
    public OrderRequestDataList getAllRequestByTradepost(int tradepostId, String status, int pageNumber, int sortType) {
        OrderRequestDataList result = new OrderRequestDataList();

        int totalRecord = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId, status);
        int totalPage = totalRecord/ AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE;
        if (totalRecord % AppConstant.TRADEPOST.MAX_POST_PER_PAGE > 0) {
            totalPage = totalPage + 1;
        }

        result.setTotalPage(totalPage);

        List<Orderrequest> orderrequestList =
                orderRequestRepository.getTradepostRequestDataByStatusAndInPageNumberWithSorting(
                        tradepostId,status,pageNumber,sortType);
        for (int i = 0; i < orderrequestList.size(); i++) {
            //Check rated by owner
            orderrequestList.get(i).setRated(
                    traderatingRepository.checkTraderatingExistByOrderIdAndFeedbackType(orderrequestList.get(i).getId(),
                            AppConstant.TRADEPOST.FEEDBACK_TYPE_OWNER_TO_TRADER));

            Profile profile = profileRepository.findProfileByAccountID(orderrequestList.get(i).getAccount().getId());

            Account emptyAcc = new Account();
            emptyAcc.setUsername(orderrequestList.get(i).getAccount().getUsername());
            emptyAcc.setId(orderrequestList.get(i).getAccount().getId());
            emptyAcc.setAvatar(profile.getAvatar());
            orderrequestList.get(i).setAccount(emptyAcc);
            Tradepost emptyTradepost = new Tradepost();
            emptyTradepost.setId(orderrequestList.get(i).getTradepost().getId());
            orderrequestList.get(i).setTradepost(emptyTradepost);
        }
        result.setData(orderrequestList);
        return result;
    }


    /******************************* Method on MY TRADE Page ************************************************/

    //Get DATA IN PAGE MY TRADE
    @Override
    public MyTradeDataList getMyTradeData(int accountId, String status, int pageNumber, int sortType) {
        MyTradeDataList result = new MyTradeDataList();

        int totalPage = tradepostRepository.countTotalPageByStatusAndAccount(status, accountId);
        result.setTotalPage(totalPage);

        List<MyTradeDTO> dtoList = new ArrayList<MyTradeDTO>();
        List<Tradepost> tradepostList = tradepostRepository.getTradepostByAccountStatusAndSortTypeInPageNumber(accountId, status, pageNumber, sortType);

        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            int tradepostId = tradepost.getId();

            String thumbnailImgUrl = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepostId);
            int numOfSucceedRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.SUCCEED_STATUS);
            int numOfPendingRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.PENDING_STATUS);
            int numOfOnPaymentRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.APPROVED_STATUS);

            Account emptyAcc = new Account();
            emptyAcc.setUsername(tradepost.getAccount().getUsername());
            emptyAcc.setId(tradepost.getAccount().getId());
            tradepost.setAccount(emptyAcc);

            MyTradeDTO myTradeDTO = new MyTradeDTO(tradepost, numOfSucceedRequest, numOfPendingRequest,
                    numOfOnPaymentRequest, thumbnailImgUrl);
            dtoList.add(myTradeDTO);
        }

        result.setData(dtoList);

        return result;
    }

    //GET SEARCH DATA IN PAGE MY TRADE
    @Override
    public MyTradeDataList getSearchMyTradeData(int accountId, String status, int pageNumber, int sortType, String keyword) {
        MyTradeDataList result = new MyTradeDataList();
        int totalPage = tradepostRepository.countTotalPageByAccountStatusWithKeyword(accountId, status, keyword);
        result.setTotalPage(totalPage);

        List<MyTradeDTO> dtoList = new ArrayList<MyTradeDTO>();
        List<Tradepost> tradepostList =
                tradepostRepository.searchTradepostByAccountStatusAndSortTypeWithKeywordInPageNumber(accountId, status,
                        pageNumber, sortType, keyword);

        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            int tradepostId = tradepost.getId();

            String thumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepostId);
            int numOfSucceedRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.SUCCEED_STATUS);
            int numOfPendingRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.PENDING_STATUS);
            int numOfOnPaymentRequest = orderRequestRepository.countRequestWithStatusByTradepostId(tradepostId,
                    AppConstant.APPROVED_STATUS);

            Account emptyAcc = new Account();
            emptyAcc.setUsername(tradepost.getAccount().getUsername());
            emptyAcc.setId(tradepost.getAccount().getId());
            tradepost.setAccount(emptyAcc);

            MyTradeDTO myTradeDTO = new MyTradeDTO(tradepost, numOfSucceedRequest, numOfPendingRequest,
                    numOfOnPaymentRequest, thumbnail);

            dtoList.add(myTradeDTO);
        }

        result.setData(dtoList);

        return result;
    }



    /******************************* Method on TRADING Page ************************************************/

    //GET DATA IN PAGE TRADING
    @Override
    public TradingDataList getTradeListingData(String tradeType, int pageNumber, int sortType) {
        TradingDataList result = new TradingDataList();

        int totalPage = tradepostRepository.countTotalPageByTradeType(tradeType);

        result.setTotalPage(totalPage);

        List<TradeListingDTO> listingDTOList = new ArrayList<TradeListingDTO>();
        List<Tradepost> tradepostList = tradepostRepository.getTradepostByTradeTypeAndSortTypeInPageNumber(tradeType, pageNumber, sortType);

        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            int tradepostId = tradepost.getId();

            String thumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepostId);

            Account emptyAcc = new Account();
            emptyAcc.setUsername(tradepost.getAccount().getUsername());
            emptyAcc.setId(tradepost.getAccount().getId());
            tradepost.setAccount(emptyAcc);

            TradeListingDTO dto = new TradeListingDTO(tradepost, thumbnail);
            listingDTOList.add(dto);
        }
        result.setData(listingDTOList);
        return result;
    }


    //GET SEARCH DATA IN PAGE TRADING
    @Override
    public TradingDataList getSearchTradeListingData(String tradeType, int pageNumber, int sortType, String keyword) {
        TradingDataList result = new TradingDataList();

        int totalPage = tradepostRepository.countTotalPageByTradeTypeWithKeyword(tradeType, keyword);
        result.setTotalPage(totalPage);

        List<TradeListingDTO> dtoList = new ArrayList<TradeListingDTO>();
        List<Tradepost> tradepostList =
                tradepostRepository.searchTradepostByTradeTypeAndSortTypeWithKeywordInPageNumber(tradeType, pageNumber,
                        sortType, keyword);

        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            int tradepostId = tradepost.getId();

            String thumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepostId);

            Account emptyAcc = new Account();
            emptyAcc.setUsername(tradepost.getAccount().getUsername());
            emptyAcc.setId(tradepost.getAccount().getId());
            tradepost.setAccount(emptyAcc);

            TradeListingDTO dto = new TradeListingDTO(tradepost, thumbnail);
            dtoList.add(dto);
        }

        result.setData(dtoList);
        return result;
    }

    //SEARCH WITH LOCATION
    @Override
    public TradingDataList getSearchTradeListingWithLocationData(String tradeType, int sortType, String keyword,
                                                              String location, long range) {
        TradingDataList result = new TradingDataList();
        result.setTotalPage(1);
        List<TradeListingDTO> dtoList = new ArrayList<TradeListingDTO>();
        List<Tradepost> tradepostList =
                tradepostRepository.searchAllTradepostByAccountStatusAndSortTypeWithKeyword(tradeType,sortType,keyword);
        LatLng from = new LatLng();
        from.lat = Double.parseDouble(location.split(",")[0]);
        from.lng = Double.parseDouble(location.split(",")[1]);
        for (int i = 0; i < tradepostList.size(); i++) {
            Tradepost tradepost = tradepostList.get(i);
            int tradepostId = tradepost.getId();
            LatLng to = new LatLng();
            to.lat = Double.parseDouble(tradepost.getLatlng().split(",")[0]);
            to.lng = Double.parseDouble(tradepost.getLatlng().split(",")[1]);
            long distance = GoogleMapHelper.calculateDistanceBetweenTwoPoint(from,to);
            System.out.println("FROM: " + from + " - TO: " + to + " = " + distance);
            if (distance <= range){
                String thumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(tradepostId);

                Account emptyAcc = new Account();
                emptyAcc.setUsername(tradepost.getAccount().getUsername());
                emptyAcc.setId(tradepost.getAccount().getId());
                tradepost.setAccount(emptyAcc);

                TradeListingDTO dto = new TradeListingDTO(tradepost, thumbnail);
                dtoList.add(dto);
            }

        }

        result.setData(dtoList);
        return result;
    }



    /******************************* Method For Order Request ************************************************/

    @Override
    public boolean acceptOrder(int orderId) {
        String status = AppConstant.APPROVED_STATUS;
        String dateSet = DatetimeHelper.dateConvertToString(new Date());
        return orderRequestRepository.setOrderStatus(orderId,status,dateSet, null, null);
    }

    @Override
    public boolean declineOrder(int orderId, String reason) {
        String status = AppConstant.DECLINED_STATUS;
        String dateSet = DatetimeHelper.dateConvertToString(new Date());
        Tradepost updateQuantityTradepost = orderRequestRepository.getTradepostByOrderId(orderId);
        int quantityToAdd = orderRequestRepository.getOrderQuantityByOrderId(orderId);
        int currentQuantity = updateQuantityTradepost.getQuantity();
        tradepostRepository.updateQuantityById(updateQuantityTradepost.getId(), currentQuantity + quantityToAdd);
        return orderRequestRepository.setOrderStatus(orderId,status,dateSet,null,reason);
    }

    @Override
    public boolean confirmOrderSucceed(int orderId) {
        String status = AppConstant.SUCCEED_STATUS;
        String dateSet = DatetimeHelper.dateConvertToString(new Date());
        return orderRequestRepository.setOrderStatus(orderId,status,dateSet,null,null);
    }

    @Override
    public boolean cancelOrder(int orderId, String reason) {
        String status = AppConstant.CANCELLED_STATUS;
        String dateSet = DatetimeHelper.dateConvertToString(new Date());
        Tradepost updateQuantityTradepost = orderRequestRepository.getTradepostByOrderId(orderId);
        int quantityToAdd = orderRequestRepository.getOrderQuantityByOrderId(orderId);
        int currentQuantity = updateQuantityTradepost.getQuantity();
        tradepostRepository.updateQuantityById(updateQuantityTradepost.getId(), currentQuantity + quantityToAdd);
        return orderRequestRepository.setOrderStatus(orderId,status,dateSet,reason,null);
    }

    @Override
    public boolean sendOrder(NewOrderDTO orderDTO) {

        Profile updateProfile = profileRepository.findProfileByEmail(orderDTO.getTraderEmail());
        updateProfile.setTel(orderDTO.getTraderPhone());
        updateProfile.setAddress(orderDTO.getAddress());
        profileRepository.updateProfile(updateProfile);

        Orderrequest newOrder = new Orderrequest();
        newOrder.setRejectReason(null);
        newOrder.setCancelReason(null);
        newOrder.setStateSetDate(null);
        newOrder.setStatus(AppConstant.PENDING_STATUS);
        Tradepost tradepost = tradepostRepository.findTradepostById(orderDTO.getTradepostId());
        newOrder.setTradepost(tradepost);
        newOrder.setAccount(updateProfile.getAccount());
        newOrder.setBillingAddress(orderDTO.getAddress());
        newOrder.setOrderDate(DatetimeHelper.dateConvertToString(new Date()));
        newOrder.setQuantity(orderDTO.getQuantity());
        orderRequestRepository.addNewOrderRequest(newOrder);

        tradepostRepository.updateQuantityById(tradepost.getId(), tradepost.getQuantity() - newOrder.getQuantity());

        return true;
    }

    @Override
    public boolean reportTrade(int tradepostId, String reason, String phone, String email) {

        Tradereport newReport = tradereportRepository.findReportByTradepostIdAndEmail(tradepostId, email);
        if (newReport != null){
            return false;
        }
        newReport = new Tradereport();
        newReport.setReason(reason);
        newReport.setTel(phone);
        newReport.setEmail(email);
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        newReport.setTradepost(tradepost);
        tradereportRepository.addNewReport(newReport);
        return true;
    }

    @Override
    public boolean ratingTrade(int orderId, int feedbackType, int value, String comment) {
        boolean result = traderatingRepository.checkTraderatingExistByOrderIdAndFeedbackType(orderId, feedbackType);
        if (!result){
            Traderating traderating = new Traderating();
            Orderrequest orderrequest = orderRequestRepository.getOrderrequestById(orderId);
            traderating.setOrderrequest(orderrequest);
            traderating.setFeedbackType(feedbackType);
            traderating.setComment(comment);
            traderating.setRating(value);
            traderating.setRatingDate(DatetimeHelper.dateConvertToString(new Date()));
            if(feedbackType == AppConstant.TRADEPOST.FEEDBACK_TYPE_OWNER_TO_TRADER){
                traderating.setFromUser(orderrequest.getTradepost().getAccount());
                traderating.setToUser(orderrequest.getAccount());

                //Update Profile Rating
                Profile updateProfileRating = profileRepository.findProfileByAccountID(orderrequest.getAccount().getId());
                updateProfileRating.setNumberOfRaters(updateProfileRating.getNumberOfRaters() + 1);
                updateProfileRating.setNumberOfStars(updateProfileRating.getNumberOfStars() + value);

                profileRepository.updateProfile(updateProfileRating);

            }else if(feedbackType == AppConstant.TRADEPOST.FEEDBACK_TYPE_TRADER_TO_OWNER) {
                traderating.setFromUser(orderrequest.getAccount());
                traderating.setToUser(orderrequest.getTradepost().getAccount());

                //Update Profile Rating
                Profile updateProfileRating = profileRepository.findProfileByAccountID(orderrequest.getTradepost().getAccount().getId());
                updateProfileRating.setNumberOfRaters(updateProfileRating.getNumberOfRaters() + 1);
                updateProfileRating.setNumberOfStars(updateProfileRating.getNumberOfStars() + value);

                profileRepository.updateProfile(updateProfileRating);

                //Update Tradepost Rating
                Tradepost updateTradepostRating = tradepostRepository.findTradepostById(orderrequest.getTradepost().getId());
                updateTradepostRating.setNumberOfRater(updateTradepostRating.getNumberOfRater() + 1);
                updateTradepostRating.setNumberOfStar(updateTradepostRating.getNumberOfStar() + value);

                tradepostRepository.update(updateTradepostRating);

            }

            if (traderatingRepository.addNewTraderating(traderating)!= null){
                result = true;
            }else {
                result = false;
            }
        }else {
            result = false;
        }

        return result;
    }


    /******************************* Method on MY ORDER Page ************************************************/

    @Override
    public MyOrderDataList getMyOrderData(int accountId, String status, int pageNumber, int sortType) {
        MyOrderDataList result = new MyOrderDataList();
        if (status.equals("others")){

        }else {

        }
        int totalRecord = 0;
        if (status.equals("others")){
            totalRecord = orderRequestRepository.countRequestWithStatusByAccountId(accountId, AppConstant.DECLINED_STATUS)
                    + orderRequestRepository.countRequestWithStatusByAccountId(accountId, AppConstant.CANCELLED_STATUS);
        }else {
            totalRecord = orderRequestRepository.countRequestWithStatusByAccountId(accountId,status);
        }
        int totalPage = totalRecord/ AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE;
        if (totalRecord % AppConstant.TRADEPOST.MAX_REQUEST_PER_PAGE > 0) {
            totalPage = totalPage + 1;
        }

        result.setTotalPage(totalPage);

        List<Orderrequest> orderrequestList = new ArrayList<Orderrequest>();

        orderrequestList = orderRequestRepository.getAllOrderRequestByAccountId(accountId,status,pageNumber,sortType);

        List<MyOrderDTO> orderDTOList = new ArrayList<MyOrderDTO>();

        for (int i = 0; i < orderrequestList.size(); i++) {
            MyOrderDTO orderDTO = new MyOrderDTO();
            orderDTO.setOrderedDate(orderrequestList.get(i).getOrderDate());
            orderDTO.setOrderStatus(orderrequestList.get(i).getStatus());
            orderDTO.setOrderQuantity(orderrequestList.get(i).getQuantity());

            if (orderDTO.getOrderStatus().equals(AppConstant.CANCELLED_STATUS)){
                orderDTO.setOrderReason(orderrequestList.get(i).getCancelReason());
            }else if (orderDTO.getOrderStatus().equals(AppConstant.DECLINED_STATUS)){
                orderDTO.setOrderReason(orderrequestList.get(i).getRejectReason());
            }else {
                orderDTO.setOrderReason(null);
            }
            orderDTO.setOrderSetDate(orderrequestList.get(i).getStateSetDate());
            orderDTO.setOrderPay(orderDTO.getOrderQuantity() * orderrequestList.get(i).getTradepost().getPrice());
            orderDTO.setTradepostId(orderrequestList.get(i).getTradepost().getId());
            String postThumbnail = tradepostimageRepository.getThumbnailImgUrlByTradepostId(orderDTO.getTradepostId());
            orderDTO.setTradepostThumbnail(postThumbnail);
            orderDTO.setTradepostTitle(orderrequestList.get(i).getTradepost().getTitle());

            Profile ownerProfile = profileRepository.findProfileByAccountID(orderrequestList.get(i).getTradepost().getAccount().getId());

            orderDTO.setOwnerAddress(orderrequestList.get(i).getTradepost().getLocation());
            if (orderDTO.getOrderStatus().equals(AppConstant.APPROVED_STATUS) ||
                    orderDTO.getOrderStatus().equals(AppConstant.SUCCEED_STATUS)){
                orderDTO.setOwnerEmail(ownerProfile.getEmail());
                orderDTO.setOwnerPhone(ownerProfile.getTel());
            }else {
                orderDTO.setOwnerEmail("N/A");
                orderDTO.setOwnerPhone("N/A");
            }


            String fullname = "";
            if (ownerProfile.getFirstName() != null){
                fullname = fullname + ownerProfile.getFirstName();
            }
            if (ownerProfile.getMiddleName() != null){
                fullname = fullname + " " + ownerProfile.getMiddleName();
            }
            if (ownerProfile.getLastName() != null){
                fullname = fullname + " " + ownerProfile.getLastName();
            }
            orderDTO.setOwnerId(ownerProfile.getAccount().getId());
            orderDTO.setOwnerName(fullname);
            orderDTO.setOrderId(orderrequestList.get(i).getId());
            //Check rated Trader -> owner
            orderDTO.setRated(traderatingRepository.checkTraderatingExistByOrderIdAndFeedbackType(
                    orderrequestList.get(i).getId(),AppConstant.TRADEPOST.FEEDBACK_TYPE_TRADER_TO_OWNER));
            orderDTOList.add(orderDTO);

        }
        result.setData(orderDTOList);
        return result;
    }


    /****************************** Method on AMIN PAGE ---***********************************************/

    @Override
    public List<Object> searchPendingTradepost(int pageNumber, String type, String txtSearch, String orderBy) {
        List<Object> result = new ArrayList<>();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCountResultAndLastPagePending(AppConstant.PAGE_SIZE, txtSearch);
        countTotal = (int) resultList[0];
        lastPage = (int) resultList[1];

        if (type.equals("First")) {
            currentPage = 1;
        } else if (type.equals("Prev")) {
            currentPage = pageNumber - 1;
        } else if (type.equals("Next")) {
            currentPage = pageNumber + 1;
        } else if (type.equals("Last")) {
            currentPage = lastPage;
        } else {
            currentPage = pageNumber;
        }

        if (currentPage <= 5) {
            beginPage = 1;
        } else if (currentPage % 5 != 0) {
            beginPage = ((int) (currentPage / 5)) * 5 + 1;
        } else {
            beginPage = ((currentPage / 5) - 1) * 5 + 1;
        }

        Pagination pagination = new Pagination(countTotal, currentPage, lastPage, beginPage);
        result.add(pagination);

        List<Tradepost> tradepostList = tradepostRepository.searchPendingTradePost(currentPage, AppConstant.PAGE_SIZE, txtSearch, orderBy);

        if (tradepostList == null) {
            tradepostList = new ArrayList<Tradepost>();
        }
        result.add(tradepostList);

        return result;
    }

    @Override
    public Tradepost approveTradepost(int tradepostId) {
        boolean result = false;
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        tradepost.setApprovalStatus(AppConstant.APPROVED_STATUS);
        Tradepost updatedTradepost = tradepostRepository.updateTradepost(tradepost);
        return updatedTradepost;
    }

    @Override
    public Tradepost declineTradepost(int tradepostId, String reason) {
        boolean result = false;
        Tradepost tradepost = tradepostRepository.findTradepostById(tradepostId);
        tradepost.setApprovalStatus(AppConstant.DECLINED_STATUS);
        tradepost.setRejectReason(reason);
        Tradepost updatedTradepost = tradepostRepository.updateTradepost(tradepost);
        return updatedTradepost;
    }

    /******************************* SCHEDULER METHOD ************************************************/
    @Override
    public void autoRejectAllOrderByScheduler() {
        String rejectReason = "Auto decline by scheduler: This order is 2 days old";
        Date now = new Date();

        List<Orderrequest> orderrequestList = orderRequestRepository.getAllPendingOrderrequest();
        for (int i = 0; i < orderrequestList.size(); i++) {
            Orderrequest orderrequest = orderrequestList.get(i);
            Date orderDate = DatetimeHelper.stringConvertToDate(orderrequest.getOrderDate());
            long diff = DatetimeHelper.diffInMilliseconds(orderDate,now);
            if (diff >= AppConstant.TRADEPOST.AUTO_DECLINE_ORDER_TIME){
                this.declineOrder(orderrequest.getId(),rejectReason);

                String noticationText = "You have order with id="+ orderrequest.getId() +" has rejected by scheduler automatic.";

                Notification notification = new Notification();
                notification.setSeen(AppConstant.NOTIFICATION_NOT_SEEN);
                notification.setDate(DatetimeHelper.dateConvertToString(now));
                notification.setAccount(orderrequest.getAccount());
                notification.setDescription(noticationText);
                Notificationtype notificationtype = new Notificationtype();
                notificationtype.setId(4);
                notificationtype.setName("OrderSent");
                notification.setNotificationtype(notificationtype);
                notification.setObjectID(orderrequest.getId());

                Notification result = notificationRepository.create(notification);

                // firebase send notification
                if (result != null) {
                    List<Token> tokens = tokenRepository.findTokenByAccountID(result.getAccount().getId());

                    System.out.println("User " + result.getAccount().getId() + " have " + tokens.size() + " tokens!!!");

                    for (Token token : tokens) {
                        send(token.getToken(), "OrderSent", noticationText);
                    }
                }
            }
        }
    }


    /******************************* Additional Method for service ************************************************/

    //Get All Images Url Array by Trade post
    String[] getImageArrayByTradepostId(int tradepostId) throws Exception {
        List<Tradepostimage> imgList = tradepostimageRepository.getImageByTradepostId(tradepostId);
        System.out.println(imgList);
        String[] imgArr = new String[imgList.size()];
        for (int i = 0; i < imgList.size(); i++) {
            imgArr[i] = imgList.get(i).getImageUrl();
        }
        return imgArr;
    }


    //Transform TradepostRequestData to Tradepost Entity
    Tradepost getTradepostEntity(TradepostRequestData tradepostRequestData) {
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
        result.setLatlng(tradepostRequestData.getTraderLatlng());
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


    void deleteImageOnServer(String[] imageDeleteList) {

        for (int i = 0; i < imageDeleteList.length; i++) {
            // delete file image at server
            if (imageDeleteList[i].contains("downloadFile/")) {
                String[] imageUrlSplit = imageDeleteList[i].split("/");
                String imageName = imageUrlSplit[imageUrlSplit.length - 1];

                File file = new File("./uploads/" + imageName);
                if (file.delete()) {
                    System.out.println("Deleted file " + imageName);
                } else {
                    System.out.println("File " + imageName + " does not exist!");
                }
            }

        }

    }

    int[] getCountResultAndLastPagePending(int pageSize, String txtSearch) {

        int[] listResult = new int[2];
        int countResult = tradepostRepository.getCountSearchPendingTradepost(txtSearch);
        listResult[0] = countResult;

        int lastPage = 1;

        if (countResult % pageSize == 0) {
            lastPage = countResult / pageSize;
        } else {
            lastPage = ((countResult / pageSize) + 1);
        }
        listResult[1] = lastPage;

        return listResult;
    }

    public void send(String token, String notificationType, String content) throws JSONException {

        JSONObject body = new JSONObject();
        body.put("to", token.trim());

        JSONObject notification = new JSONObject();
        notification.put("title", notificationType);
        notification.put("body", content);
        body.put("notification", notification);

        // print
        System.out.println(body.toString());

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotifications = pushNotification.send(request);
        CompletableFuture.allOf(pushNotifications).join();

        try {
            String firebaseResponse = pushNotifications.get();

            System.out.println(firebaseResponse);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
