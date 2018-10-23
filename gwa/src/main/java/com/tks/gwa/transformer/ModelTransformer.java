package com.tks.gwa.transformer;

import com.tks.gwa.entity.*;
import com.tks.gwa.jaxb.Image;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelTransformer {

    public Model convertToEntity(com.tks.gwa.jaxb.Model jaxb_model) {

        Model entity_model = null;

        if (jaxb_model != null) {
            entity_model = new Model();

            entity_model.setCode(jaxb_model.getCode());
            entity_model.setName(jaxb_model.getName());
            entity_model.setDescription(jaxb_model.getDescription());
            entity_model.setPrice(jaxb_model.getPrice());
            entity_model.setReleasedDate(jaxb_model.getReleasedDate());

            // set manufacturer
            if (jaxb_model.getManufacturer() != null) {
                Manufacturer manufacturer = new Manufacturer();
                manufacturer.setName(jaxb_model.getManufacturer());
                entity_model.setManufacturer(manufacturer);
            }

            // set product series (scale)
            if (jaxb_model.getProductSeries() != null) {
                Productseries productseries = new Productseries();
                if (jaxb_model.getProductSeries().contains("1/144")) {
                    productseries.setName("1/144");
                } else if (jaxb_model.getProductSeries().contains("1/100")) {
                    productseries.setName("1/100");
                } else {
                    productseries.setName("1/60");
                }
                entity_model.setProductseries(productseries);
            }

            // set series title
            if (jaxb_model.getSeriesTitle() != null) {
                Seriestitle seriestitle = new Seriestitle();
                if (jaxb_model.getSeriesTitle().contains("MG")) {
                    seriestitle.setName("Master Grade");
                } else if (jaxb_model.getSeriesTitle().contains("HG")) {
                    seriestitle.setName("High Grade");
                } else if (jaxb_model.getSeriesTitle().contains("PG")) {
                    seriestitle.setName("Perfect Grade");
                } else if (jaxb_model.getSeriesTitle().contains("RG")) {
                    seriestitle.setName("Real Grade");
                } else {
                    seriestitle.setName(jaxb_model.getSeriesTitle());
                }
                entity_model.setSeriestitle(seriestitle);
            }

            List<Modelimage> entity_list_images = new ArrayList<Modelimage>();
            List<Image> jaxb_list_images = jaxb_model.getImage();

            if (jaxb_list_images != null) {
                for (Image jaxb_image : jaxb_list_images) {
                    Modelimage entity_image = new Modelimage();
                    // set image url
                    entity_image.setImageUrl(jaxb_image.getUrl());

                    // get image type
                    Imagetype imagetype = new Imagetype();
                    imagetype.setName(jaxb_image.getType());
                    entity_image.setImagetype(imagetype);

                    // add to list
                    entity_list_images.add(entity_image);
                }
            }

            entity_model.setModelimages(entity_list_images);
        }

        return entity_model;
    }
}
