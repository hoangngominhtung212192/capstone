package com.tks.gwa.service;

import com.tks.gwa.entity.Model;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ModelService {

    /**
     *
     * @param model
     * @return
     */
    public Model createNewModel(Model model, String status);
}
