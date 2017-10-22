package com.revauc.revolutionbuy.network.request.auth;

/*
Copyright © 2017 Block Partee. All rights reserved.
Developed by Appster.
*/

public class ReportItemRequest {

    private String description;
    private int buyerProductId;

    public ReportItemRequest(String description, int buyerProductId) {
        this.description = description;
        this.buyerProductId = buyerProductId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuyerProductId() {
        return buyerProductId;
    }

    public void setBuyerProductId(int buyerProductId) {
        this.buyerProductId = buyerProductId;
    }
}
