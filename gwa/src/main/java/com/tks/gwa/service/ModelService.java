package com.tks.gwa.service;

import com.tks.gwa.entity.Manufacturer;
import com.tks.gwa.entity.Model;
import com.tks.gwa.entity.Productseries;
import com.tks.gwa.entity.Seriestitle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ModelService {

    /**
     *
     * @param model
     * @return
     */
    public Model createNewModel(Model model, String status);

    /**
     *
     * @return
     */
    public List<Productseries> getAllProductseries();

    /**
     *
     * @return
     */
    public List<Seriestitle> getAllSeriestitle();

    /**
     *
     * @return
     */
    public List<Manufacturer> getAllManufacturer();
}
