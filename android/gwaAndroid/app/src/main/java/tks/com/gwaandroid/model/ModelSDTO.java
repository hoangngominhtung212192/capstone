package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class ModelSDTO {

    @SerializedName("searchValue")
    private String searchValue;

    @SerializedName("productseries")
    private String productseries;

    @SerializedName("seriestitle")
    private String seriestitle;

    @SerializedName("price")
    private String price;

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("pagination")
    private Pagination pagination;

    @SerializedName("orderBy")
    private String orderBy;

    @SerializedName("cending")
    private String cending;

    @SerializedName("modelDTOList")
    private List<ModelDTO> modelDTOList;

    public ModelSDTO() {}

    public ModelSDTO(String searchValue, String productseries, String seriestitle,
                     String price, String manufacturer, Pagination pagination, String orderBy,
                     String cending, List<ModelDTO> modelDTOList) {
        this.searchValue = searchValue;
        this.productseries = productseries;
        this.seriestitle = seriestitle;
        this.price = price;
        this.manufacturer = manufacturer;
        this.pagination = pagination;
        this.orderBy = orderBy;
        this.cending = cending;
        this.modelDTOList = modelDTOList;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

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
