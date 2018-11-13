package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class Modelimage {

    @SerializedName("id")
    private int id;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("model")
    private Model model;

    @SerializedName("imagetype")
    private Imagetype imagetype;

    public Modelimage(int id, String imageUrl, Model model, Imagetype imagetype) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.model = model;
        this.imagetype = imagetype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Imagetype getImagetype() {
        return imagetype;
    }

    public void setImagetype(Imagetype imagetype) {
        this.imagetype = imagetype;
    }
}
