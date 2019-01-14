
package com.icecream.Models.ConfirmOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {


    @SerializedName("ProductId")
    @Expose
    private String ProductId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
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
        return ProductId;
    }

    public void setProductId(String productId) {
        this.ProductId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
