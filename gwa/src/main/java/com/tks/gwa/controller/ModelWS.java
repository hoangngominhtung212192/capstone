package com.tks.gwa.controller;

import com.tks.gwa.entity.Manufacturer;
import com.tks.gwa.entity.Productseries;
import com.tks.gwa.entity.Seriestitle;
import com.tks.gwa.service.ModelService;
import org.omg.CORBA.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
