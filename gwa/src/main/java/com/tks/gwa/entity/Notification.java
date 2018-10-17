package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "objectid")
    private int objectID;

    @Column(name = "seen")
    private int seen;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="accountid")
    private Account account;

    //bi-directional many-to-one association to Notificationtype
    @ManyToOne
    @JoinColumn(name="notificationtypeid")
    private Notificationtype notificationtype;
}
