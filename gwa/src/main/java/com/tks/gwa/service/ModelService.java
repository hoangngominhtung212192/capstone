package com.tks.gwa.service;

import com.tks.gwa.dto.LogCrawl;
import com.tks.gwa.dto.ModelDTO;
import com.tks.gwa.dto.ModelSDTO;
import com.tks.gwa.dto.ModelSearchFilter;
import com.tks.gwa.entity.*;
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

    /**
     *
     * @return
     */
    public List<LogCrawl> getLogCrawlFromFile();

    /**
     *
     * @return
     */
    public List<Object> searchPendingModel(int pageNumber, String type, String txtSearch, String orderBy);

    /**
     *
     * @return
     */
    public Model approveModel(int id);

    /**
     *
     * @param images
     * @param types
     * @return
     */
    public Model uploadModelImages(List<String> images, List<String> types, int modelID);

    /**
     *
     * @param id
     * @return
     */
    public ModelDTO findModelByID(int id);

    /**
     *
     * @return
     */
    public ModelSDTO searchModel(ModelSDTO modelSDTO);

    /**
     *
     * @param model
     * @return
     */
    public Model updateModel(Model model);

    /**
     *
     * @param modelimageList
     */
    public void updatedImage(List<Modelimage> modelimageList);

    /**
     *
     * @param modelimageList
     */
    public void deleteImage(List<Modelimage> modelimageList);

    /**
     *
     * @param modelID
     * @param accountID
     * @return
     */
    public Modelrating checkExistRating(int modelID, int accountID);

    /**
     *
     * @param modelrating
     */
    public void createModelRating(Modelrating modelrating);

    /**
     *
     * @param pageNumber
     * @param modelID
     * @return
     */
    public List<Object> getAllModelRatingByModelID(int pageNumber, int modelID);

    /**
     *
     * @param modelName
     * @return
     */
    public List<Tradepost> getRelatedTradepost(String modelName);

    /**
     *
     * @return
     */
    public List<Model> getTop5Rating();

    /**
     *
     * @param modelName
     * @return
     */
    public List<Article> getTop5ArticleByModelName(String modelName);

    /**
     *
     * @param modelID
     */
    public void deleteModelByModelID(int modelID);

    /**
     *
     * @param modelID
     */
    public void updateStatusErrorModel(int modelID);

    /**
     *
     * @return
     */
    public List<ModelSearchFilter> getListFilters();

    /**
     *
     * @return
     */
    public List<Object> manageModel(int pageNumber, String type, String txtSearch, String orderBy, String status);
}
