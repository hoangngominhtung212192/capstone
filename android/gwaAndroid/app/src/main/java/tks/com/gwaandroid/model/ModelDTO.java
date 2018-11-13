package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class ModelDTO {

    @SerializedName("model")
    private Model model;

    @SerializedName("modelimageList")
    private List<Modelimage> modelimageList;

    @SerializedName("message")
    private String message;

    public ModelDTO(Model model, List<Modelimage> modelimageList, String message) {
        this.model = model;
        this.modelimageList = modelimageList;
        this.message = message;
    }

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
