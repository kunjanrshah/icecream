package com.icecream.Models.CompleteOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CBInfo implements Serializable {

    public String getTC() {
        return TC;
    }

    public void setTC(String TC) {
        this.TC = TC;
    }

    public String getTB() {
        return TB;
    }

    public void setTB(String TB) {
        this.TB = TB;
    }

    @SerializedName("TC")
    @Expose
    private String TC;

    @SerializedName("TB")
    @Expose
    private String TB;
}
