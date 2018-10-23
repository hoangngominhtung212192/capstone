package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Model;
import com.tks.gwa.repository.ModelRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ModelRepositoryImpl extends GenericRepositoryImpl<Model, Integer> implements ModelRepository {

    /*

     */
    public ModelRepositoryImpl() {
            super(Model.class);
    }

    /**
     *
     * @return
     */
    @Override
    public List<Model> getAllModel() {
        return this.getAll();
    }

    /**
     *
     * @param model
     * @return
     */
    @Override
    public Model addNewModel(Model model) {

        Model newModel = this.create(model);

        return newModel;
    }

    /**
     *
     * @param model
     * @return
     */
    @Override
    public Model editModel(Model model) {

        Model updatedModel = this.update(model);

        return updatedModel;
    }

    /**
     *
     * @param model
     * @return
     */
    @Override
    public boolean removeModel(Model model) {

        this.delete(model);

        return true;
    }

    /**
     *
     * @param modelID
     * @return
     */
    @Override
    public Model findModelByID(int modelID) {

        Model model = this.read(modelID);

        return null;
    }

    @Override
    public Model findModelByCode(String code) {

        String sql = "SELECT m FROM " + Model.class.getName()+ " AS m WHERE m.code =:code";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("code", code);

        Model model = null;

        try {
            model = (Model) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return model;
    }

    @Override
    public Model findModelByName(String name) {

        String sql = "SELECT m FROM " + Model.class.getName()+ " AS m WHERE m.name =:name";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", name);

        Model model = null;

        try {
            model = (Model) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return model;
    }
}
