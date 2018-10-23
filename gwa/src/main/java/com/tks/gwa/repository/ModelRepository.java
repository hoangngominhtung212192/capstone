package com.tks.gwa.repository;

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
}
