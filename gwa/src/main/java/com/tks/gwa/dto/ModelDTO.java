package com.tks.gwa.dto;

import com.tks.gwa.entity.Model;
import com.tks.gwa.entity.Modelimage;

import java.util.List;

public class ModelDTO {

    private Model model;
    private List<Modelimage> modelimageList;
    private String message;

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<Modelimage> getModelimageList() {
        return modelimageList;
    }

    public void setModelimageList(List<Modelimage> modelimageList) {
        this.modelimageList = modelimageList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
