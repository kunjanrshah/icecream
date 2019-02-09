
package com.icecream.Models.PendingOrder;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Msg implements Serializable {

    @SerializedName("OrderId")
    @Expose
    private String orderId;
    @SerializedName("DistributorCode")
    @Expose
    private String distributorCode;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("ActualAmount")
    @Expose
    private String actualAmount;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("UpdatedOn")
    @Expose
    private String updatedOn;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;

    public String getCustomerOrderId() {
        return CustomerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        CustomerOrderId = customerOrderId;
    }

    @SerializedName("CustomerOrderId")
    @Expose
    private String CustomerOrderId;

    @SerializedName("OrderDetails")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public List<com.icecream.Models.PendingOrder.CBInfo> getCBInfo() {
        return CBInfo;
    }

    public void setCBInfo(List<com.icecream.Models.PendingOrder.CBInfo> CBInfo) {
        this.CBInfo = CBInfo;
    }

    @SerializedName("CBInfo")
    @Expose
    private List<CBInfo> CBInfo = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDistributorCode() {
        return distributorCode;
    }

    public void setDistributorCode(String distributorCode) {
        this.distributorCode = distributorCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
