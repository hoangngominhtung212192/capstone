package com.tks.gwa.dto;

import java.util.List;

public class ModelSDTO {

    private String searchValue;
    private String productseries;
    private String seriestitle;
    private String price;
    private String manufacturer;
    private Pagination pagination;
    private String orderBy;
    private String cending;
    private List<ModelDTO> modelDTOList;

    public String getProductseries() {
        return productseries;
    }

    public void setProductseries(String productseries) {
        this.productseries = productseries;
    }

    public String getSeriestitle() {
        return seriestitle;
    }

    public void setSeriestitle(String seriestitle) {
        this.seriestitle = seriestitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCending() {
        return cending;
    }

    public void setCending(String cending) {
        this.cending = cending;
    }

    public List<ModelDTO> getModelDTOList() {
        return modelDTOList;
    }

    public void setModelDTOList(List<ModelDTO> modelDTOList) {
        this.modelDTOList = modelDTOList;
    }
}
