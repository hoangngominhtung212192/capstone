package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tradepostimage")
public class Tradepostimage implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "imageurl")
    private String imageUrl;

    //bi-directional many-to-one association to Tradepost
    @ManyToOne
    @JoinColumn(name="tradepostid")
    private Tradepost tradepost;

    public Tradepostimage(){}

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

    public Tradepost getTradepost() {
        return tradepost;
    }

    public void setTradepost(Tradepost tradepost) {
        this.tradepost = tradepost;
    }
}
