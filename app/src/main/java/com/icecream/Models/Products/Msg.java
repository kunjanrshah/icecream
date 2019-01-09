
package com.icecream.Models.Products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Msg {

    @SerializedName("ProductId")
    @Expose
    private String productId;
    @SerializedName("CategoryId")
    @Expose
    private String categoryId;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("PackingType")
    @Expose
    private String packingType;
    @SerializedName("PricePerKG")
    @Expose
    private String pricePerKG;
    @SerializedName("StockStatus")
    @Expose
    private String stockStatus;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("UpdatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("CartonAvailability")
    @Expose
    private String CartonAvailability;

    public String getCartonAvailability() {
        return CartonAvailability;
    }

    public void setCartonAvailability(String cartonAvailability) {
        CartonAvailability = cartonAvailability;
    }

    public String getCartonQty() {
        return CartonQty;
    }

    public void setCartonQty(String cartonQty) {
        CartonQty = cartonQty;
    }

    @SerializedName("CartonQty")
    @Expose
    private String CartonQty;




    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getPricePerKG() {
        return pricePerKG;
    }

    public void setPricePerKG(String pricePerKG) {
        this.pricePerKG = pricePerKG;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
