package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ModelWS;
import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.dto.*;
import com.tks.gwa.entity.*;
import com.tks.gwa.listener.ModelThreadCrawler;
import com.tks.gwa.service.ModelService;
import org.hibernate.type.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ModelWSImpl implements ModelWS {

    @Autowired
    private FileControllerWsImpl fileControllerWs;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelThreadCrawler modelThreadCrawler;

    @Override
    public ResponseEntity<List<Productseries>> getAllProductseries() {

        System.out.println("[ModelWS] begin getAllProductseries");

        List<Productseries> productseriesList = modelService.getAllProductseries();

        return new ResponseEntity<>(productseriesList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Seriestitle>> getAllSeriestitle() {

        System.out.println("[ModelWS] begin getAllSeriestitle");

        List<Seriestitle> seriestitleList = modelService.getAllSeriestitle();

        return new ResponseEntity<>(seriestitleList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Manufacturer>> getAllManufacturer() {

        System.out.println("[ModelWS] begin getAllManufacturer");

        List<Manufacturer> manufacturerList = modelService.getAllManufacturer();

        return new ResponseEntity<>(manufacturerList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> createNewModel(@RequestBody Model model) {

        System.out.println("[ModelWS] Begin createNewModel with data: ");

        System.out.println("- Code: " + model.getCode());
        System.out.println("- Name: " + model.getName());
        System.out.println("- Manufacturer: " + model.getManufacturer().getName());
        System.out.println("- Product series: " + model.getProductseries().getName());
        System.out.println("- Series title: " + model.getSeriestitle().getName());
        System.out.println("- Price: " + model.getPrice());
        System.out.println("- Status: " + model.getStatus());
        System.out.println("- Description: " + model.getDescription());

        Model newModel = modelService.createNewModel(model, model.getStatus());

        if (newModel == null) {
            newModel = new Model();
            newModel.setMessage("Cannot create new model");
            return new ResponseEntity<>(newModel, HttpStatus.valueOf(400));
        }

        if (newModel.getMessage() != null) {
            return new ResponseEntity<>(newModel, HttpStatus.valueOf(400));
        }

        return new ResponseEntity<>(newModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> updateModelImage(MultipartFile[] files, String[] imagetypes, int id) {
        System.out.println("[ModelWS] Begin updateModelImage with data:");
        System.out.println("Model ID: " + id);

        List<UploadFileResponse> uploadFileResponseList = Arrays.asList(files).
                stream().map(file -> fileControllerWs.uploadFile(file)).collect(Collectors.toList());


        List<String> images = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for (UploadFileResponse response : uploadFileResponseList) {
            images.add(response.getFileDownloadUri());
            System.out.println("");
            System.out.print(response.getFileDownloadUri() + " - " + response.getFileName());
        }

        for (String s : imagetypes) {
            types.add(s);
            System.out.print(" - " + s);
            System.out.println("");
        }

        Model newModel = modelService.uploadModelImages(images, types, id);

        return new ResponseEntity<>(newModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<LogCrawl>> getLogCrawl() {

        System.out.println("[ModelWS] Begin getLogCrawl");

        List<LogCrawl> logCrawlList = modelService.getLogCrawlFromFile();

        return new ResponseEntity<>(logCrawlList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> crawlModel() {

        if (modelThreadCrawler.isInprogress()) {
            return new ResponseEntity<String>("System is already crawling", HttpStatus.valueOf(400));
        }

        Thread modelCrawl = new Thread(modelThreadCrawler, "ModelThreadCrawler");
        modelCrawl.start();

        return new ResponseEntity<String>("System is beginning crawling", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LogCrawl> getCrawlStatus() {

        System.out.println("[ModelWS] Begin getCrawlStatus()");

        LogCrawl logCrawl = new LogCrawl();

        boolean inProgress = modelThreadCrawler.isInprogress();
        logCrawl.setInProgress(inProgress);

        int records = modelThreadCrawler.getCrawlRecords();
        logCrawl.setNumberOfRecords(records + "");

        int newRecords = modelThreadCrawler.getNewRecords();
        logCrawl.setNumberOfNewRecords(newRecords + "");

        return new ResponseEntity<>(logCrawl, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> searchPendingModel(int pageNumber, String type, String txtSearch, String orderBy) {

        System.out.println("[ModelWS] Begin searchPendingModel() with data:");
        System.out.println("Page number: " + pageNumber);
        System.out.println("Type: " + type);
        System.out.println("Search value: " + txtSearch);
        System.out.println("OrderBy: " + orderBy);

        List<Object> resultList = modelService.searchPendingModel(pageNumber, type, txtSearch, orderBy);

        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> approvePendingModel(int id) {

        System.out.println("[ModelWS] Begin approvePendingModel with modelID: " + id);

        Model newModel = modelService.approveModel(id);

        if (newModel != null) {
            return new ResponseEntity<>(newModel, HttpStatus.OK);
        }

        return null;
    }

    @Override
    public ResponseEntity<ModelDTO> getModelDetail(int id) {

        System.out.println("Begin getModelDetail with id: " + id);

        ModelDTO modelDTO = modelService.findModelByID(id);

        if (modelDTO != null) {
            return new ResponseEntity<>(modelDTO, HttpStatus.OK);
        }

        modelDTO = new ModelDTO();
        modelDTO.setMessage("Cannot get model detail");

        return new ResponseEntity<>(modelDTO, HttpStatus.valueOf(400));
    }

    @Override
    public ResponseEntity<ModelSDTO> searchModel(@RequestBody ModelSDTO modelSDTO) {

        Pagination pagination = modelSDTO.getPagination();
        System.out.println("Current Page: " + pagination.getCurrentPage());
        System.out.println("Type: " + pagination.getType());

        System.out.println("Search value: " + modelSDTO.getSearchValue());
        System.out.println("Productseries: " + modelSDTO.getProductseries());
        System.out.println("Seriestitle: " + modelSDTO.getSeriestitle());
        System.out.println("Manufacturer: " + modelSDTO.getManufacturer());
        System.out.println("Price: " + modelSDTO.getPrice());
        System.out.println("Order By: " + modelSDTO.getOrderBy());
        System.out.println("Cending: " + modelSDTO.getCending());

        ModelSDTO result = modelService.searchModel(modelSDTO);

        return new ResponseEntity<ModelSDTO>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Model> updateModel(@RequestBody Model model) {

        System.out.println("[ModelWS] Begin updateModel with data: ");

        System.out.println("- ID: " + model.getId());
        System.out.println("- CreatedDate: " + model.getCreatedDate());
        System.out.println("- Code: " + model.getCode());
        System.out.println("- Name: " + model.getName());
        System.out.println("- Manufacturer: " + model.getManufacturer().getName());
        System.out.println("- Product series: " + model.getProductseries().getName());
        System.out.println("- Series title: " + model.getSeriestitle().getName());
        System.out.println("- Price: " + model.getPrice());
        System.out.println("- Status: " + model.getStatus());
        System.out.println("- Description: " + model.getDescription());

        Model updatedModel = modelService.updateModel(model);

        if (updatedModel == null) {
            updatedModel = new Model();
            updatedModel.setMessage("Cannot update model");
            return new ResponseEntity<>(updatedModel, HttpStatus.valueOf(400));
        }

        if (updatedModel.getMessage() != null) {
            return new ResponseEntity<>(updatedModel, HttpStatus.valueOf(400));
        }

        return new ResponseEntity<>(updatedModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updatedExistImage(@RequestBody List<Modelimage> listUpdateImage) {

        System.out.println("[ModelWS] Begin updatedExistImage with data:");

        for (int i = 0; i < listUpdateImage.size(); i++) {
            System.out.println("Url: " + listUpdateImage.get(i).getImageUrl() + " - "
            + listUpdateImage.get(i).getImagetype().getName());
        }

        modelService.updatedImage(listUpdateImage);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteExistImage(@RequestBody List<Modelimage> modelimageList) {

        System.out.println("[ModelWS] Begin deleteExistImage with data: ");

        for (int i = 0; i < modelimageList.size(); i++) {
            System.out.println("Url: " + modelimageList.get(i).getImageUrl());
        }

        modelService.deleteImage(modelimageList);

        return null;
    }

    @Override
    public ResponseEntity<String> checkExistedRating(int modelID, int accountID) {

        System.out.println("[ModelWS] Begin checkExistedRating with data: ");
        System.out.println("ModelID: " + modelID);
        System.out.println("AccountID: " + accountID);

        Modelrating modelrating = modelService.checkExistRating(modelID, accountID);

        if (modelrating == null) {
            return new ResponseEntity<>("Not yet", HttpStatus.OK);
        }

        return new ResponseEntity<>("Existed", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createModelRating(@RequestBody Modelrating modelrating) {

        System.out.println("[ModelWS] Begin createModelRating with data:");
        System.out.println("AccountID: " + modelrating.getAccount().getId());
        System.out.println("ModelID: " + modelrating.getModel().getId());
        System.out.println("Rating: " + modelrating.getRating());
        System.out.println("Feedback: " + modelrating.getFeedback());

        modelService.createModelRating(modelrating);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Object>> getAllReviewByModelID(int pageNumber, int modelID) {

        System.out.println("[ModelWS] Begin getAllReviewByModelID with data:");
        System.out.println("ModelId: " + modelID);
        System.out.println("PageNumber: " + pageNumber);

        List<Object> result = modelService.getAllModelRatingByModelID(pageNumber, modelID);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        result = new ArrayList<>();
        result.add(0);
        return new ResponseEntity<List<Object>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Tradepost>> getRelatedTradepost(String modelName) {

        System.out.println("[ModelWS] Begin getRelatedTradepost with data: ");
        System.out.println("ModelName: " + modelName);

        List<Tradepost> result = modelService.getRelatedTradepost(modelName);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Model>> getTop5Rating() {

        System.out.println("[ModelWS] Begin getTop5Rating");

        List<Model> result = modelService.getTop5Rating();

        if (result == null) {
            result = new ArrayList<Model>();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Article>> getTop5ArticleByModelName(String modelName) {

        System.out.println("[ModelWS] Begin getTop5ArticleByModelName with modelName: " + modelName);

        List<Article> result = modelService.getTop5ArticleByModelName(modelName);

        if (result == null) {
            result = new ArrayList<Article>();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
