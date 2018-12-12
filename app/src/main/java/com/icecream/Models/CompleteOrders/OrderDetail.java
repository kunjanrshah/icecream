
package com.icecream.Models.CompleteOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("ProductId")
    @Expose
    private String productId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("PackingType")
    @Expose
    private String packingType;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("ActualQty")
    @Expose
    private String actualQty;
    @SerializedName("ActualTotalPrice")
    @Expose
    private String actualTotalPrice;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPackingType() {
        return packingType;
    }

    public void setPackingType(String packingType) {
        this.packingType = packingType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActualQty() {
        return actualQty;
    }

    public void setActualQty(String actualQty) {
        this.actualQty = actualQty;
    }

    public String getActualTotalPrice() {
        return actualTotalPrice;
    }

    public void setActualTotalPrice(String actualTotalPrice) {
        this.actualTotalPrice = actualTotalPrice;
    }

}
