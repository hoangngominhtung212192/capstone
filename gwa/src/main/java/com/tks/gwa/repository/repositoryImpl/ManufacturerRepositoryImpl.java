package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Manufacturer;
import com.tks.gwa.repository.ManufacturerRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ManufacturerRepositoryImpl extends GenericRepositoryImpl<Manufacturer, Integer> implements ManufacturerRepository {

    public ManufacturerRepositoryImpl() {
        super(Manufacturer.class);
    }

    @Override
    public Manufacturer findByName(String name) {

        String sql = "SELECT m FROM " + Manufacturer.class.getName()+ " AS m WHERE m.name =:name";

        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", name);

        Manufacturer manufacturer= null;

        try {
            manufacturer = (Manufacturer) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return manufacturer;
    }

    @Override
    public Manufacturer createNew(Manufacturer manufacturer) {

        Manufacturer newManufacturer = this.create(manufacturer);

        return newManufacturer;
    }
}
