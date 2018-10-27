package com.tks.gwa.controller;

import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.entity.*;
import com.tks.gwa.service.ModelService;
import org.omg.CORBA.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RequestMapping(value = "/getAllPending", method = RequestMethod.GET)
    ResponseEntity<List<Object>> getAllPendingModel(@RequestParam("pageNumber") int pageNumber,
                                                   @RequestParam("type") String type);

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    ResponseEntity<Model> approvePendingModel(@RequestParam("id") int id);
}
