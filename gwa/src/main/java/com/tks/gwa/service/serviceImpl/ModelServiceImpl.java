package com.tks.gwa.service.serviceImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.entity.*;
import com.tks.gwa.repository.*;
import com.tks.gwa.service.ModelService;
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

    @Override
    public Model createNewModel(Model model, String status) {

        if (model.getProductseries() != null && model.getCode() != null && model.getSeriestitle() != null) {
            if (!checkExistedModel(model.getName())) {
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

                // create new entity model
                Model newModel = modelRepository.addNewModel(model);

                if (newModel != null) {
                    // to save all new model images which are saved to database
                    List<Modelimage> newModelImageList = new ArrayList<Modelimage>();

                    System.out.println("Save model code: " + newModel.getCode() + " successfully!");

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

    public boolean checkExistedModel(String name) {
        // if existed
        if (modelRepository.findModelByName(name) != null) {
            System.out.println("Model " + name + " has been existed!");

            return true;
        }

        // not exist
        return false;
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
    public List<Object> getAllPendingModel(int pageNumber, String type) {

        List<Object> result = new ArrayList<>();

        int beginPage = 0;
        int currentPage = 0;
        int countTotal = 0;
        int lastPage = 0;

        int[] resultList = getCountResultAndLastPage(AppConstant.PAGE_SIZE);
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

        List<Model> modelList = modelRepository.getAllPending(currentPage, AppConstant.PAGE_SIZE);

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

    public int[] getCountResultAndLastPage(int pageSize) {

        int[] listResult = new int[2];
        int countResult = modelRepository.getCountAllPending();
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
