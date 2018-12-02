package com.tks.gwa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "token")
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "token")
    private String token;

    //bi-directional many-to-one association to Account
    @ManyToOne
    @JoinColumn(name="accountid")
    private Account account;

    public Token() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
