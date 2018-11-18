package tks.com.gwaandroid.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tung Hoang Ngo Minh on 11/14/2018.
 */

public class Account {

    @SerializedName("id")
    private int id;

    @SerializedName("password")
    private String password;

    @SerializedName("status")
    private String status;

    @SerializedName("username")
    private String username;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("role")
    private Role role;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("middlename")
    private String middlename;

    @SerializedName("address")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("message")
    private String message;

    @SerializedName("avatar")
    private String avatar;

    public Account(int id, String password, String status, String username, String createdDate,
                   Role role, String firstname, String lastname, String middlename, String address,
                   String email, String message, String avatar) {
        this.id = id;
        this.password = password;
        this.status = status;
        this.username = username;
        this.createdDate = createdDate;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.address = address;
        this.email = email;
        this.message = message;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
