package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class Productseries implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public Productseries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
