package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.constant.AppConstant;
import com.tks.gwa.dto.ModelSDTO;
import com.tks.gwa.dto.Pagination;
import com.tks.gwa.entity.Manufacturer;
import com.tks.gwa.entity.Model;
import com.tks.gwa.entity.Productseries;
import com.tks.gwa.entity.Seriestitle;
import com.tks.gwa.repository.ManufacturerRepository;
import com.tks.gwa.repository.ModelRepository;
import com.tks.gwa.repository.ProductseriesRepository;
import com.tks.gwa.repository.SeriestitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ModelRepositoryImpl extends GenericRepositoryImpl<Model, Integer> implements ModelRepository {

    @Autowired
    private ProductseriesRepository productseriesRepository;

    @Autowired
    private SeriestitleRepository seriestitleRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public ModelRepositoryImpl() {
        super(Model.class);
    }

    /**
     * @return
     */
    @Override
    public List<Model> getAllModel() {
        return this.getAll();
    }

    /**
     * @param model
     * @return
     */
    @Override
    public Model addNewModel(Model model) {

        Model newModel = this.create(model);

        return newModel;
    }

    /**
     * @param model
     * @return
     */
    @Override
    public Model editModel(Model model) {

        Model updatedModel = this.update(model);

        return updatedModel;
    }

    /**
     * @param model
     * @return
     */
    @Override
    public boolean removeModel(Model model) {

        this.delete(model);

        return true;
    }

    /**
     * @param modelID
     * @return
     */
    @Override
    public Model findModelByID(int modelID) {

        Model model = this.read(modelID);

        return model;
    }

    @Override
    public Model findModelByCode(String code) {

        String sql = "SELECT m FROM " + Model.class.getName() + " AS m WHERE m.code =:code";

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

        String sql = "SELECT m FROM " + Model.class.getName() + " AS m WHERE m.name =:name";

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

    @Override
    public List<Model> getAllPending(int pageNumber, int pageSize) {

        String sql = "SELECT m FROM " + Model.class.getName() + " AS m WHERE m.status='crawlpending' ORDER BY m.createdDate DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Model> modelList = query.getResultList();

        return modelList;
    }

    @Override
    public int getCountAllPending() {

        String sql = "SELECT count(m.id) FROM " + Model.class.getName() + " AS m WHERE m.status='crawlpending'";

        Query query = this.entityManager.createQuery(sql);

        return (int) (long) query.getSingleResult();
    }

    @Override
    public List<String> getAllMd5HashValues() {

        String sql = "SELECT m.md5Hash FROM " + Model.class.getName() + " AS m";

        Query query = this.entityManager.createQuery(sql);

        List<String> md5List = query.getResultList();

        return md5List;
    }

    @Override
    public List<Model> searchModel(ModelSDTO modelSDTO) {

        Pagination pagination = modelSDTO.getPagination();

        List<Model> result = null;

        // create the default query
        String sql = "SELECT m FROM " + Model.class.getName() + " AS m";

        // initialize filter entities
        Productseries productseries = null;
        Seriestitle seriestitle = null;
        Manufacturer manufacturer = null;

        // the query
        Query query = null;

        // check exist
        boolean productseries_flag = false;
        boolean seriestitle_flag = false;
        boolean manufacturer_flag = false;
        boolean searchValue_flag = false;
        boolean mark_first = false;

        // check null
        if (modelSDTO != null) {

            // search value condition
            String searchValue = modelSDTO.getSearchValue();
            if (searchValue.length() > 0) {
                searchValue_flag = true;

                if (!mark_first) {
                    sql += " WHERE m.name LIKE :sName";
                    mark_first = true;
                } else {
                    sql += " AND m.name LIKE :sName";
                }
            }

            // productseries condition
            String productseriesSearch = modelSDTO.getProductseries();
            if (!productseriesSearch.equals("All")) {
                productseries_flag = true;

                productseries = productseriesRepository.findByName(productseriesSearch);

                if (productseries != null) {
                    if (!mark_first) {
                        sql += " WHERE m.productseries =:Sproductseries";
                        mark_first = true;
                    } else {
                        sql += " AND m.productseries =:Sproductseries";
                    }
                }
            }

            // seriestitle condition
            String seriestitleSearch = modelSDTO.getSeriestitle();
            if (!seriestitleSearch.equals("All")) {
                seriestitle_flag = true;

                seriestitle = seriestitleRepository.findByName(seriestitleSearch);

                if (seriestitle != null) {
                    if (!mark_first) {
                        sql += " WHERE m.seriestitle =:Sseriestitle";
                        mark_first = true;
                    } else {
                        sql += " AND m.seriestitle =:Sseriestitle";
                    }
                }
            }

            // manufacturer condition
            String manufacturerSearch = modelSDTO.getManufacturer();
            if (!manufacturerSearch.equals("All")) {
                manufacturer_flag = true;

                manufacturer = manufacturerRepository.findByName(manufacturerSearch);

                if (manufacturer != null) {
                    if (!mark_first) {
                        sql += " WHERE m.manufacturer =:Smanufacturer";
                        mark_first = true;
                    } else {
                        sql += " AND m.manufacturer =:Smanufacturer";
                    }
                }
            }

            // price condition
            String priceSearch = modelSDTO.getPrice();
            if (!priceSearch.equals("All")) {

                if (priceSearch.equals("< 5,000 yen")) {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 < 5000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 < 5000";
                    }
                } else if (priceSearch.equals("5,000 yen - 10,000 yen")) {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 >= 5000 " +
                                "AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 <= 10000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 >= 5000 " +
                                "AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 <= 10000";
                    }
                } else {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 > 10000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 > 10000";
                    }
                }
            }

            // status condition
//            if (!mark_first) {
//                sql += " WHERE m.status='Available'";
//            } else {
//                sql += " AND m.status='Available'";
//            }

            // filter orderby condition
            String orderBy = modelSDTO.getOrderBy();
            String cending = modelSDTO.getCending();

            if (orderBy.equals("Price")) {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 ASC";
                } else {
                    sql += " ORDER BY REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 DESC";
                }
            } else if (orderBy.equals("Created Date")) {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY m.createdDate ASC";
                } else {
                    sql += " ORDER BY m.createdDate DESC";
                }
            } else {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY m.numberOfRating ASC";
                } else {
                    sql += " ORDER BY m.numberOfRating DESC";
                }
            }

            System.out.println("[ModelRepository] Execute query with sql: " + sql);
            query = this.entityManager.createQuery(sql);

            if (searchValue_flag) {
                query.setParameter("sName", "%" + searchValue + "%");
            }
            if (productseries_flag) {
                query.setParameter("Sproductseries", productseries);
            }
            if (seriestitle_flag) {
                query.setParameter("Sseriestitle", seriestitle);
            }
            if (manufacturer_flag) {
                query.setParameter("Smanufacturer", manufacturer);
            }

            query.setFirstResult((pagination.getCurrentPage() - 1) * AppConstant.PAGE_SIZE);
            query.setMaxResults(AppConstant.PAGE_SIZE);

            result = query.getResultList();

            return result;
        }

        return null;
    }

    @Override
    public int getCountAllSearch(ModelSDTO modelSDTO) {

        // create the default query
        String sql = "SELECT count(m.id) FROM " + Model.class.getName() + " AS m";

        // initialize filter entities
        Productseries productseries = null;
        Seriestitle seriestitle = null;
        Manufacturer manufacturer = null;

        // the query
        Query query = null;

        // check exist
        boolean productseries_flag = false;
        boolean seriestitle_flag = false;
        boolean manufacturer_flag = false;
        boolean searchValue_flag = false;
        boolean mark_first = false;

        // check null
        if (modelSDTO != null) {

            // search value condition
            String searchValue = modelSDTO.getSearchValue();
            if (searchValue.length() > 0) {
                searchValue_flag = true;

                if (!mark_first) {
                    sql += " WHERE m.name LIKE :sName";
                    mark_first = true;
                } else {
                    sql += " AND m.name LIKE :sName";
                }
            }

            // productseries condition
            String productseriesSearch = modelSDTO.getProductseries();
            if (!productseriesSearch.equals("All")) {
                productseries_flag = true;

                productseries = productseriesRepository.findByName(productseriesSearch);

                if (productseries != null) {
                    if (!mark_first) {
                        sql += " WHERE m.productseries =:Sproductseries";
                        mark_first = true;
                    } else {
                        sql += " AND m.productseries =:Sproductseries";
                    }
                }
            }

            // seriestitle condition
            String seriestitleSearch = modelSDTO.getSeriestitle();
            if (!seriestitleSearch.equals("All")) {
                seriestitle_flag = true;

                seriestitle = seriestitleRepository.findByName(seriestitleSearch);

                if (seriestitle != null) {
                    if (!mark_first) {
                        sql += " WHERE m.seriestitle =:Sseriestitle";
                        mark_first = true;
                    } else {
                        sql += " AND m.seriestitle =:Sseriestitle";
                    }
                }
            }

            // manufacturer condition
            String manufacturerSearch = modelSDTO.getManufacturer();
            if (!manufacturerSearch.equals("All")) {
                manufacturer_flag = true;

                manufacturer = manufacturerRepository.findByName(manufacturerSearch);

                if (manufacturer != null) {
                    if (!mark_first) {
                        sql += " WHERE m.manufacturer =:Smanufacturer";
                        mark_first = true;
                    } else {
                        sql += " AND m.manufacturer =:Smanufacturer";
                    }
                }
            }

            // price condition
            String priceSearch = modelSDTO.getPrice();
            if (!priceSearch.equals("All")) {

                if (priceSearch.equals("< 5,000 yen")) {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 < 5000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 < 5000";
                    }
                } else if (priceSearch.equals("5,000 yen - 10,000 yen")) {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 >= 5000 " +
                                "AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 <= 10000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 >= 5000 " +
                                "AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 <= 10000";
                    }
                } else {
                    if (!mark_first) {
                        sql += " WHERE REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 > 10000";
                        mark_first = true;
                    } else {
                        sql += " AND REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 > 10000";
                    }
                }
            }

            // status condition
//            if (!mark_first) {
//                sql += " WHERE m.status='Available'";
//            } else {
//                sql += " AND m.status='Available'";
//            }

            // filter orderby condition
            String orderBy = modelSDTO.getOrderBy();
            String cending = modelSDTO.getCending();

            if (orderBy.equals("Price")) {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 ASC";
                } else {
                    sql += " ORDER BY REPLACE(REPLACE(m.price, ',', ''), ' yen', '') + 0 DESC";
                }
            } else if (orderBy.equals("Created Date")) {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY m.createdDate ASC";
                } else {
                    sql += " ORDER BY m.createdDate DESC";
                }
            } else {
                if (cending.equals("Ascending")) {
                    sql += " ORDER BY m.numberOfRating ASC";
                } else {
                    sql += " ORDER BY m.numberOfRating DESC";
                }
            }

            System.out.println("[ModelRepository] Execute query with sql: " + sql);
            query = this.entityManager.createQuery(sql);

            if (searchValue_flag) {
                query.setParameter("sName", "%" + searchValue + "%");
            }
            if (productseries_flag) {
                query.setParameter("Sproductseries", productseries);
            }
            if (seriestitle_flag) {
                query.setParameter("Sseriestitle", seriestitle);
            }
            if (manufacturer_flag) {
                query.setParameter("Smanufacturer", manufacturer);
            }

            return (int) (long) query.getSingleResult();
        }

        return 0;
    }

    @Override
    public Model getModelByMD5(String md5) {

        String sql = "SELECT m FROM " + Model.class.getName() + " AS m WHERE m.md5Hash =:md5";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("md5", md5);

        Model model = null;

        try {
            model = (Model) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return model;
    }

    @Override
    public List<Model> getTop5Rating() {

        String sql = "SELECT m FROM " + Model.class.getName() + " AS m ORDER BY m.numberOfRating DESC";

        Query query = this.entityManager.createQuery(sql);
        query.setMaxResults(5);

        List<Model> result = query.getResultList();

        return result;
    }
}
