package com.tks.gwa.controller.controllerImpl;

import com.tks.gwa.controller.ModelWS;
import com.tks.gwa.entity.Manufacturer;
import com.tks.gwa.entity.Productseries;
import com.tks.gwa.entity.Seriestitle;
import com.tks.gwa.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModelWSImpl implements ModelWS {

    @Autowired
    private ModelService modelService;

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
}
