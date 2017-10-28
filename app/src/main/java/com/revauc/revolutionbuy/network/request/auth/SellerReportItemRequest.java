package com.revauc.revolutionbuy.network.request.auth;

/*
Copyright © 2017 Block Partee. All rights reserved.
Developed by Appster.
*/

public class SellerReportItemRequest {

    private String description;
    private int sellerId;

    public SellerReportItemRequest(String description, int sellerId) {
        this.description = description;
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
}
