package com.tks.gwa.repository;

import com.tks.gwa.dto.ModelSDTO;
import com.tks.gwa.entity.Model;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends GenericRepository<Model, Integer> {

    /**
     *
     * @return
     */
    List<Model> getAllModel();

    /**
     *
     * @param model
     * @return
     */
    Model addNewModel(Model model);

    /**
     *
     * @param model
     * @return
     */
    Model editModel(Model model);

    /**
     *
     * @param model
     * @return
     */
    boolean removeModel(Model model);

    /**
     *
     * @param modelID
     * @return
     */
    Model findModelByID(int modelID);

    /**
     *
     * @param code
     * @return
     */
    Model findModelByCode(String code);

    /**
     *
     * @param name
     * @return
     */
    Model findModelByName(String name);

    /**
     *
     * @return
     */
    List<Model> searchPending(int pageNumber, int pageSize, String txtSearch, String orderBy);

    /**
     *
     * @return
     */
    int getCountSearchPending(String txtSearch);

    /**
     *
     * @return
     */
    List<String> getAllMd5HashValues();

    /**
     *
     * @param modelSDTO
     * @return
     */
    List<Model> searchModel(ModelSDTO modelSDTO);

    /**
     *
     * @param modelSDTO
     * @return
     */
    int getCountAllSearch(ModelSDTO modelSDTO);

    /**
     *
     * @param md5
     * @return
     */
    Model getModelByMD5(String md5);

    /**
     *
     * @return
     */
    List<Model> getTop5Rating();
}
