package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ModelWS;
import com.tks.gwa.crawler.ModelCrawl;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.UploadFileResponse;
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
            System.out.println(response.getFileDownloadUri() + " - " + response.getFileName());
        }

        for (String s : imagetypes) {
            types.add(s);
            System.out.print(" - " + s);
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
    public ResponseEntity<List<Object>> getAllPendingModel(int pageNumber, String type) {

        System.out.println("[ModelWS] Begin getAllPendingModel()");

        List<Object> resultList = modelService.getAllPendingModel(pageNumber, type);

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

}
