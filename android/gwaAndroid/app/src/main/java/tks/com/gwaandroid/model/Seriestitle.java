package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tung Hoang Ngo Minh on 11/12/2018.
 */

public class Seriestitle {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public Seriestitle(int id, String name) {
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
