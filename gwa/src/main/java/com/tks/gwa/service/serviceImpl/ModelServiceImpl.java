package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.ModelDTO;
import com.tks.gwa.dto.ModelSDTO;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.entity.*;
import com.tks.gwa.repository.*;
import com.tks.gwa.service.ModelService;
import com.tks.gwa.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ProductseriesRepository productseriesRepository;

    @Autowired
    private SeriestitleRepository seriestitleRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ImagetypeRepository imagetypeRepository;

    @Autowired
    private ModelimageRepository modelimageRepository;

    @Autowired
    private ModelratingRepository modelratingRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TradepostRepository tradepostRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Model createNewModel(Model model, String status) {

        if (model.getProductseries() != null && model.getCode() != null && model.getSeriestitle() != null) {
            // check existed code
            if (!checkExistedCode(model.getCode())) {
                // check existed model
                String hashMD5 = hashMD5(model.getName(), model.getManufacturer().getName(), model.getProductseries().getName(),
                        model.getSeriestitle().getName());
                List<String> md5List = modelRepository.getAllMd5HashValues();

                if (!checkExistedModel(hashMD5, md5List)) {
                    // set md5 hash value
                    model.setMd5Hash(hashMD5);

                    // check exist manu or create new
                    String manufacturer_name = model.getManufacturer().getName();
                    Manufacturer manufacturer = processManufacturer(manufacturer_name);
                    model.setManufacturer(manufacturer);

                    // check exist product series or create new
                    String productseries_name = model.getProductseries().getName();
                    Productseries productseries = processProductseries(productseries_name);
                    model.setProductseries(productseries);

                    // check exist series title or create new
                    String seriestitle_name = model.getSeriestitle().getName();
                    Seriestitle seriestitle = processSeriestitle(seriestitle_name);
                    model.setSeriestitle(seriestitle);

                    // set current date time
                    String currentDateTime = getCurrentTimeStamp();
                    model.setCreatedDate(currentDateTime);

                    // set default status for admin to approve
                    if (status.equalsIgnoreCase("crawl")) {
                        model.setStatus(AppConstant.CRAWL_PENDING);
                    }

                    // set default rating
                    model.setNumberOfRating(0);
                    model.setNumberOfRater(0);

                    // create new entity model
                    Model newModel = modelRepository.addNewModel(model);

                    if (newModel != null) {
                        // to save all new model images which are saved to database
                        List<Modelimage> newModelImageList = new ArrayList<Modelimage>();

                        System.out.println("Save model code: " + newModel.getCode() + " successfully!");
                        System.out.println("New model is saved with MD5 hash value: " + newModel.getMd5Hash());

                        // get current list model images
                        List<Modelimage> modelimageList = model.getModelimages();
                        if (modelimageList != null) {
                            for (Modelimage modelimage : modelimageList) {
                                // get image type by name
                                Imagetype imagetype = processImageType(modelimage.getImagetype().getName());

                                if (imagetype != null) {
                                    // set image type to model image
                                    modelimage.setImagetype(imagetype);

                                    // set modelID to modelimage
                                    Model model_for_image = new Model();
                                    model_for_image.setId(newModel.getId());
                                    modelimage.setModel(model_for_image);

                                    // create new
                                    Modelimage newModelImage = modelimageRepository.createNew(modelimage);
                                    if (newModelImage != null) {
                                        System.out.println("Save image: " + newModelImage.getImageUrl() + " successfully!");
                                        // add to list new model images
                                        newModelImageList.add(newModelImage);
                                    }
                                } else {
                                    // missing image type
                                    System.out.println("Database doesn't have image type: " + modelimage.getImagetype().getName());
                                }
                            }
                        } // end for model image

                        // set list new model images
                        newModel.setModelimages(newModelImageList);

                        // return
                        return newModel;
                    }
                } else {
                    Model responseModel = new Model();
                    responseModel.setMessage("This model has been existed");
                    System.out.println("This model has been existed");

                    return responseModel;
                }
            } else {
                Model responseModel = new Model();
                responseModel.setMessage("Model's code " + model.getCode() + " has been existed");
                System.out.println("Model's code " + model.getCode() + " has been existed");

                return responseModel;
            }
        }

        return null;
    }

    @Override
    public List<Productseries> getAllProductseries() {

        List<Productseries> productseriesList = productseriesRepository.getAllProductseries();

        if (productseriesList == null) {
            productseriesList = new ArrayList<Productseries>();
        }

        return productseriesList;
    }

    @Override
    public List<Seriestitle> getAllSeriestitle() {
        List<Seriestitle> seriestitleList = seriestitleRepository.getAllSeriestitle();

        if (seriestitleList == null) {
            seriestitleList = new ArrayList<Seriestitle>();
        }

        return seriestitleList;
    }

    @Override
    public List<Manufacturer> getAllManufacturer() {
        List<Manufacturer> manufacturerList = manufacturerRepository.getAllManufacturer();

        if (manufacturerList == null) {
            manufacturerList = new ArrayList<Manufacturer>();
        }

        return manufacturerList;
    }

    public boolean checkExistedCode(String code) {
        // if existed
        if (modelRepository.findModelByCode(code) != null) {
            return true;
        }

        // not exist
        return false;
    }

    public boolean checkExistedModel(String md5Hash, List<String> md5List) {
        // if existed
        if (md5Hash != null && md5List != null) {

            for (int i = 0; i < md5List.size(); i++) {
                if (md5List.get(i).equals(md5Hash)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String hashMD5(String name, String manufacturer, String productseries, String seriestitle) {

        if (manufacturer == null) {
            manufacturer = "";
        }

        String input = name + manufacturer + productseries + seriestitle;

        String md5 = StringHelper.hashMd5(input);

        return md5;
    }

    // check exist or create new
    public Productseries processProductseries(String name) {
        Productseries productseries = productseriesRepository.findByName(name);

        // if exist
        if (productseries != null) {
            return productseries;
        }

        // create new
        productseries = new Productseries();
        productseries.setName(name);

        Productseries newProductseries = productseriesRepository.createNew(productseries);
        return newProductseries;
    }

    // check exist or create new
    public Manufacturer processManufacturer(String name) {

        if (name.length() > 0) {
            Manufacturer manufacturer = manufacturerRepository.findByName(name);

            // if exist
            if (manufacturer != null) {
                return manufacturer;
            }

            // create new
            manufacturer = new Manufacturer();
            manufacturer.setName(name);

            Manufacturer newManufacturer = manufacturerRepository.createNew(manufacturer);
            return newManufacturer;
        }

        Manufacturer newManufacturer = new Manufacturer();
        return newManufacturer;
    }

    // check exist or create new
    public Seriestitle processSeriestitle(String name) {

        Seriestitle seriestitle = seriestitleRepository.findByName(name);

        // if exist
        if (seriestitle != null) {
            return seriestitle;
        }

        // create new
        seriestitle = new Seriestitle();
        seriestitle.setName(name);

        Seriestitle newSeriestitle = seriestitleRepository.createNew(seriestitle);
        return newSeriestitle;
    }

    public Imagetype processImageType(String name) {

        Imagetype imagetype = imagetypeRepository.findByName(name);

        return imagetype;
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdf.format(now);
        return strDate;
    }

    public List<LogCrawl> getLogCrawlFromFile() {
        List<LogCrawl> listLogs = new ArrayList<LogCrawl>();
        File f = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            f = new File(AppConstant.LOG_FILE_MODEL_CRAWL);
            if (!f.exists()) {
                return listLogs;
            }

            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String details;

            while ((details = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(details, ";");
                int id = Integer.parseInt(stk.nextToken());
                String logDateTime = stk.nextToken();
                String numberOfRecords = stk.nextToken();
                String numberOfNewRecords = stk.nextToken();
                String status = stk.nextToken();

                LogCrawl logCrawl = new LogCrawl(id, logDateTime, numberOfRecords, numberOfNewRecords, status);
                listLogs.add(logCrawl);
            }
            System.out.println("Finish loading from file with result: " + listLogs + " and size: " + listLogs.size());

            return listLogs;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listLogs;
    }

    @Override
    public List<Object> searchPendingModel(int pageNumber, String type, String txtSearch, String orderBy) {

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

        List<Model> modelList = modelRepository.searchPending(currentPage, AppConstant.PAGE_SIZE, txtSearch, orderBy);

        if (modelList == null) {
            modelList = new ArrayList<Model>();
        }
        result.add(modelList);

        return result;
    }

    @Override
    public Model approveModel(int id) {

        Model model = modelRepository.findModelByID(id);

        model.setStatus("Available");

        Model newModel = modelRepository.update(model);

        return newModel;
    }

    @Override
    public Model uploadModelImages(List<String> images, List<String> types, int modelID) {

        Model model = new Model();
        model.setId(modelID);

        for (int i = 0; i < images.size(); i++) {
            String typeName = types.get(i);

            Imagetype imagetype = processImageType(typeName);

            if (imagetype != null) {
                Modelimage modelimage = new Modelimage();

                modelimage.setImageUrl(images.get(i));
                modelimage.setImagetype(imagetype);
                modelimage.setModel(model);

                Modelimage newModelImage = modelimageRepository.createNew(modelimage);
                System.out.println("Create new model image: " + newModelImage.toString());
            }
        }

        return model;
    }

    @Override
    public ModelDTO findModelByID(int id) {
        Model model = modelRepository.findModelByID(id);

        if (model != null) {
            List<Modelimage> modelimageList = modelimageRepository.findImagesByModelID(id);

            ModelDTO modelDTO = new ModelDTO();
            modelDTO.setModel(model);
            modelDTO.setModelimageList(modelimageList);

            return modelDTO;
        }

        return null;
    }

    @Override
    public ModelSDTO searchModel(ModelSDTO modelSDTO) {

        List<ModelDTO> modelDTOList = new ArrayList<ModelDTO>();

        Pagination currentPagination = modelSDTO.getPagination();
        String type = currentPagination.getType();
        int pageNumber = currentPagination.getCurrentPage();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCounResultAndLastPageSearch(AppConstant.PAGE_SIZE, modelSDTO);
        countTotal = (int) resultList[0];
        lastPage = (int) resultList[1];

        // calculate current page if have type
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

        // calculate beginPage
        if (currentPage <= 5) {
            beginPage = 1;
        } else if (currentPage % 5 != 0) {
            beginPage = ((int) (currentPage / 5)) * 5 + 1;
        } else {
            beginPage = ((currentPage / 5) - 1) * 5 + 1;
        }

        // re-caculate pagination
        Pagination new_pagination = new Pagination(countTotal, currentPage, lastPage, beginPage);
        System.out.println("Total: " + countTotal);
        System.out.println("Current Page: " + currentPage);
        System.out.println("Last page: " + lastPage);
        System.out.println("Begin page: " + beginPage);

        // set new calculated pagination
        modelSDTO.setPagination(new_pagination);

        // this is magic
        List<Model> result = modelRepository.searchModel(modelSDTO);

        // get thumbImage
        for (int i = 0; i < result.size(); i++) {
            ModelDTO dto = new ModelDTO();

            Model model = result.get(i);
            List<Modelimage> modelimageList = modelimageRepository.findImagesByModelID(model.getId());
            // set the first image in galery to be thumbImage
            if (modelimageList.size() > 0) {
                String thumbImage = getThumbImage(modelimageList, "Package");

                if (thumbImage == null) {
                    thumbImage = getThumbImage(modelimageList, "Item picture");

                    if (thumbImage == null) {
                        thumbImage = getThumbImage(modelimageList, "Other picture");

                        if (thumbImage == null) {
                            thumbImage = getThumbImage(modelimageList, "Contents");

                            if (thumbImage == null) {
                                thumbImage = getThumbImage(modelimageList, "About item");

                                if (thumbImage == null) {
                                    thumbImage = getThumbImage(modelimageList, "Color");

                                    if (thumbImage == null) {
                                        thumbImage = getThumbImage(modelimageList, "Assembly guide");
                                    }
                                }
                            }
                        }
                    }
                }

                model.setThumbImage(thumbImage);
            } else {
                // set default image
                model.setThumbImage("https://www.1999.co.jp/itbig00/10009254.jpg");
            }

            // set model
            dto.setModel(model);
            // add to list dtos
            modelDTOList.add(dto);
        }

        modelSDTO.setModelDTOList(modelDTOList);

        return modelSDTO;
    }

    @Override
    public Model updateModel(Model model) {

        boolean checkExistMd5 = false;

        // check existed model
        String hashMD5 = hashMD5(model.getName(), model.getManufacturer().getName(), model.getProductseries().getName(),
                model.getSeriestitle().getName());
        Model checkExist_Model = modelRepository.getModelByMD5(hashMD5);

        // check exist
        if (checkExist_Model != null) {
            if (checkExist_Model.getId() != model.getId()) {
                checkExistMd5 = true;
            }
        }

        // if not exist
        if (!checkExistMd5) {

            // check exist manu or create new
            String manufacturer_name = model.getManufacturer().getName();
            Manufacturer manufacturer = processManufacturer(manufacturer_name);
            model.setManufacturer(manufacturer);

            // check exist product series or create new
            String productseries_name = model.getProductseries().getName();
            Productseries productseries = processProductseries(productseries_name);
            model.setProductseries(productseries);

            // check exist series title or create new
            String seriestitle_name = model.getSeriestitle().getName();
            Seriestitle seriestitle = processSeriestitle(seriestitle_name);
            model.setSeriestitle(seriestitle);

            // create new entity model
            Model updatedModel = modelRepository.editModel(model);

            if (updatedModel != null) {// to save all new model images which are saved to database
                return updatedModel;
            }
        } else {
            Model responseModel = new Model();
            responseModel.setMessage("This model has been existed");
            System.out.println("This model has been existed");

            return responseModel;
        }

        return null;
    }

    @Override
    public void updatedImage(List<Modelimage> modelimageList) {

        for (int i = 0; i < modelimageList.size(); i++) {
            Modelimage modelimage = modelimageRepository.findImagesByUrl(modelimageList.get(i).getImageUrl());

            if (modelimage != null) {
                Imagetype imagetype = imagetypeRepository.findByName(modelimageList.get(i).getImagetype().getName());
                modelimage.setImagetype(imagetype);

                modelimageRepository.update(modelimage);
            }
        }

    }

    @Override
    public void deleteImage(List<Modelimage> modelimageList) {

        for (int i = 0; i < modelimageList.size(); i++) {
            // delete file image at server
            if (modelimageList.get(i).getImageUrl().contains("downloadFile/")) {
                String[] imageUrlSplit = modelimageList.get(i).getImageUrl().split("/");
                String imageName = imageUrlSplit[imageUrlSplit.length - 1];

                File file = new File("./uploads/" + imageName);
                if (file.delete()) {
                    System.out.println("Deleted file " + imageName);
                } else {
                    System.out.println("File " + imageName + " does not exist!");
                }
            }

            // delete images in database
            Modelimage modelimage = modelimageRepository.findImagesByUrl(modelimageList.get(i).getImageUrl());
            if (modelimage != null) {
                modelimageRepository.delete(modelimage);
                System.out.println("Delete model image in database: " + modelimage);
            }
        }

    }

    @Override
    public Modelrating checkExistRating(int modelID, int accountID) {

        Model model = new Model();
        model.setId(modelID);

        Account account = new Account();
        account.setId(accountID);

        Modelrating responseModelrating = modelratingRepository.checkExistRating(model, account);

        return responseModelrating;
    }

    @Override
    public void createModelRating(Modelrating modelrating) {

        if (modelrating != null) {
            String date = getCurrentTimeStamp();
            modelrating.setDate(date);

            modelratingRepository.create(modelrating);

            // update numberOfRater, numberOfRating
            Model model = modelRepository.findModelByID(modelrating.getModel().getId());

            // plus one rater
            int numberOfRater = model.getNumberOfRater() + 1;
            model.setNumberOfRater(numberOfRater);

            // plus number of ratings
            int numberOfRating = model.getNumberOfRating() + modelrating.getRating();
            model.setNumberOfRating(numberOfRating);
            modelRepository.update(model);
        }

    }

    @Override
    public List<Object> getAllModelRatingByModelID(int pageNumber, int modelID) {

        List<Object> result = new ArrayList<>();

        Model model = new Model();
        model.setId(modelID);

        int total = modelratingRepository.getCountModelRatingByModelID(model);

        if (total > 0) {
            int lastPage = 0;

            if (total % 10 == 0) {
                lastPage = total / 10;
            } else {
                lastPage = ((total / 10) + 1);
            }

            result.add(lastPage);

            List<Modelrating> modelratingList = modelratingRepository.getListModelRating(pageNumber, model);
            if (modelratingList != null) {
                for (int i = 0; i < modelratingList.size(); i++) {
                    Profile profile = profileRepository.findProfileByAccountID(modelratingList.get(i).getAccount().getId());
                    modelratingList.get(i).getAccount().setAvatar(profile.getAvatar());
                }
                
                result.add(modelratingList);

                return result;
            }
        }

        return null;
    }

    // levenshtein algorithm
    @Override
    public List<Tradepost> getRelatedTradepost(String modelName) {

        List<Tradepost> result = new ArrayList<>();

        boolean checkEnough = false;

        if (modelName != null) {
            int total = tradepostRepository.getCountAll();

            // similarity algorithm
            if (total > 0) {
                int lastPage = 1;

                if (total % 50 == 0) {
                    lastPage = total / 50;
                } else {
                    lastPage = ((total / 50) + 1);
                }

                for (int i = 1; i <= lastPage && !checkEnough; i++) {
                    List<Tradepost> tradepostList = tradepostRepository.get50TradePost(i, 50);

                    for (int j = 0; j < tradepostList.size() && !checkEnough; j++) {
                        // 70% matching
                        if (StringHelper.similarity(modelName, tradepostList.get(j).getModel()) >= 0.7) {
                            // log to console
                            System.out.println("");
                            System.out.println("Similarity between " + modelName + " vs " + tradepostList.get(j).getModel() + ": "
                            + StringHelper.similarity(modelName, tradepostList.get(j).getModel()));

                            result.add(tradepostList.get(j));

                            if (result.size() == 5) {
                                checkEnough = true;
                            }
                        }
                    }
                }
            }

            return result;
        }

        return null;
    }

    @Override
    public List<Model> getTop5Rating() {

        List<Model> result = modelRepository.getTop5Rating();

        // get thumbImage
        for (int i = 0; i < result.size(); i++) {

            Model model = result.get(i);
            List<Modelimage> modelimageList = modelimageRepository.findImagesByModelID(model.getId());
            // set the first image in galery to be thumbImage
            if (modelimageList.size() > 0) {
                String thumbImage = getThumbImage(modelimageList, "Package");

                if (thumbImage == null) {
                    thumbImage = getThumbImage(modelimageList, "Item picture");

                    if (thumbImage == null) {
                        thumbImage = getThumbImage(modelimageList, "Other picture");

                        if (thumbImage == null) {
                            thumbImage = getThumbImage(modelimageList, "Contents");

                            if (thumbImage == null) {
                                thumbImage = getThumbImage(modelimageList, "About item");

                                if (thumbImage == null) {
                                    thumbImage = getThumbImage(modelimageList, "Color");

                                    if (thumbImage == null) {
                                        thumbImage = getThumbImage(modelimageList, "Assembly guide");
                                    }
                                }
                            }
                        }
                    }
                }

                result.get(i).setThumbImage(thumbImage);
            } else {
                // set default image
                result.get(i).setThumbImage("https://www.1999.co.jp/itbig00/10009254.jpg");
            }

        }

        return result;
    }

    @Override
    public List<Article> getTop5ArticleByModelName(String modelName) {

        List<Article> articleList = articleRepository.getTop5ArticleByModelName(modelName);

        return articleList;
    }

    @Override
    public void deleteModelByModelID(int modelID) {

        Model model = modelRepository.read(modelID);

        if (model != null) {
            List<Modelimage> modelimageList = modelimageRepository.findImagesByModelID(model.getId());

            if (modelimageList != null) {
                // delete in folder /uploads on server and in database
                deleteImage(modelimageList);
            }

            modelRepository.delete(model);
        }
    }

    @Override
    public void updateStatusErrorModel(int modelID) {

        Model model = modelRepository.read(modelID);

        if (model != null) {
            model.setStatus("Unavailable");

            modelRepository.update(model);
        }
    }

    public String getThumbImage(List<Modelimage> modelimageList, String imagetype) {

        for (int i = 0; i < modelimageList.size(); i++) {
            if (modelimageList.get(i).getImagetype().getName().equalsIgnoreCase(imagetype)) {
                return modelimageList.get(i).getImageUrl();
            }
        }

        return null;
    }

    public int[] getCountResultAndLastPagePending(int pageSize, String txtSearch) {

        int[] listResult = new int[2];
        int countResult = modelRepository.getCountSearchPending(txtSearch);
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

    public int[] getCounResultAndLastPageSearch(int pageSize, ModelSDTO modelSDTO) {
        int[] listResult = new int[2];
        int countResult = modelRepository.getCountAllSearch(modelSDTO);
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


}
