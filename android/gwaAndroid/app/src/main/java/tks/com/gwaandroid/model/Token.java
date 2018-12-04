package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Tung Hoang Ngo Minh on 12/3/2018.
 */

public class Token implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("token")
    private String token;

    @SerializedName("account")
    private Account account;

    public Token(int id, String token, Account account) {
        this.id = id;
        this.token = token;
        this.account = account;
    }

    public Token() {
    }

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
