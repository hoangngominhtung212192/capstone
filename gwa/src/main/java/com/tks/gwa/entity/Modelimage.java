package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "modelimage")
public class Modelimage implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "imageurl")
    private String imageUrl;

    //bi-directional many-to-one association to Model
    @ManyToOne
    @JoinColumn(name="modelid")
    private Model model;

    //bi-directional many-to-one association to Imagetype
    @ManyToOne
    @JoinColumn(name="typeid")
    private Imagetype imagetype;

    public Modelimage(){}

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
