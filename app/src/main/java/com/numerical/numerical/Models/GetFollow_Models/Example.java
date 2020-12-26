package com.numerical.numerical.Models.GetFollow_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("itemID")
    @Expose
    private String itemID;
    @SerializedName("itemType")
    @Expose
    private String itemType;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
