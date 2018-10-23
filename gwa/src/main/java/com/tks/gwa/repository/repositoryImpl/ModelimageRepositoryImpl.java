package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Modelimage;
import com.tks.gwa.repository.ModelimageRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ModelimageRepositoryImpl extends GenericRepositoryImpl<Modelimage, Integer> implements ModelimageRepository {

    public ModelimageRepositoryImpl() {
        super(Modelimage.class);
    }

    @Override
    public Modelimage createNew(Modelimage modelimage) {

        Modelimage newModelImage = this.create(modelimage);

        return newModelImage;
    }
}
