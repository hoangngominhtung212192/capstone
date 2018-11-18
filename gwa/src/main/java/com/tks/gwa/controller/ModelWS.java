package com.tks.gwa.controller;

import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.ModelDTO;
import com.tks.gwa.dto.ModelSDTO;
import com.tks.gwa.dto.ModelSearchFilter;
import com.tks.gwa.entity.*;
import com.tks.gwa.service.ModelService;
import org.omg.CORBA.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/api/model")
public interface ModelWS {

    @RequestMapping(value = "/getAllProductseries", method = RequestMethod.GET)
    ResponseEntity<List<Productseries>> getAllProductseries();

    @RequestMapping(value = "/getAllSeriestitle", method = RequestMethod.GET)
    ResponseEntity<List<Seriestitle>> getAllSeriestitle();

    @RequestMapping(value = "/getAllManufacturer", method = RequestMethod.GET)
    ResponseEntity<List<Manufacturer>> getAllManufacturer();

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    ResponseEntity<Model> createNewModel(@RequestBody Model model);

    @RequestMapping(value = "/create/uploadImages", method = RequestMethod.POST)
    ResponseEntity<Model> updateModelImage(@RequestParam(value = "files", required = false) MultipartFile[] files,
                                               @RequestParam(value = "imagetypes", required = false) String[] imagetypes,
                                               @RequestParam("id") int id);

    @RequestMapping(value = "/crawl/getLog", method = RequestMethod.GET)
    ResponseEntity<List<LogCrawl>> getLogCrawl();

    @RequestMapping(value = "/crawl", method = RequestMethod.GET)
    ResponseEntity<String> crawlModel();

    @RequestMapping(value = "/crawl/getStatus", method = RequestMethod.GET)
    ResponseEntity<LogCrawl> getCrawlStatus();

    @RequestMapping(value = "/searchPending", method = RequestMethod.GET)
    ResponseEntity<List<Object>> searchPendingModel(@RequestParam("pageNumber") int pageNumber,
                                                   @RequestParam("type") String type,
                                                    @RequestParam("txtSearch") String txtSearch,
                                                    @RequestParam("orderBy") String orderBy);

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    ResponseEntity<Model> approvePendingModel(@RequestParam("id") int id);

    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    ResponseEntity<ModelDTO> getModelDetail(@RequestParam("id") int id);

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    ResponseEntity<ModelSDTO> searchModel(@RequestBody ModelSDTO modelSDTO);

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    ResponseEntity<Model> updateModel(@RequestBody Model model);

    @RequestMapping(value = "/update/updateImage", method = RequestMethod.POST)
    ResponseEntity<String> updatedExistImage(@RequestBody List<Modelimage> listUpdateImage);

    @RequestMapping(value = "/update/deleteImage", method = RequestMethod.POST)
    ResponseEntity<String> deleteExistImage(@RequestBody List<Modelimage> modelimageList);

    @RequestMapping(value = "/rating/checkExist", method = RequestMethod.GET)
    ResponseEntity<String> checkExistedRating(@RequestParam("modelID") int modelID, @RequestParam("accountID") int accountID);

    @RequestMapping(value = "/rating/create", method = RequestMethod.POST)
    ResponseEntity<String> createModelRating(@RequestBody Modelrating modelrating);

    @RequestMapping(value = "/rating/getAll", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getAllReviewByModelID(@RequestParam("pageNumber") int pageNumber,
                                                       @RequestParam("modelID") int modelID);

    @RequestMapping(value = "/getRelatedTradePost", method = RequestMethod.GET)
    ResponseEntity<List<Tradepost>> getRelatedTradepost(@RequestParam("modelName") String modelName);

    @RequestMapping(value = "/getTop5Rating", method = RequestMethod.GET)
    ResponseEntity<List<Model>> getTop5Rating();

    @RequestMapping(value = "/getTop5Article", method = RequestMethod.GET)
    ResponseEntity<List<Article>> getTop5ArticleByModelName(@RequestParam("modelName") String modelName);

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    ResponseEntity<String> deleteModel(@RequestParam("modelID") int modelID);

    @RequestMapping(value = "/updateError", method = RequestMethod.POST)
    ResponseEntity<String> updateErrorModel(@RequestParam("modelID") int modelID);

    @RequestMapping(value = "/getSearchFilters", method = RequestMethod.GET)
    ResponseEntity<List<ModelSearchFilter>> getListModelSearchFilters();
}
