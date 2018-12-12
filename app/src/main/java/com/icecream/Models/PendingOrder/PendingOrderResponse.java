
package com.icecream.Models.PendingOrder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingOrderResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private List<List<Msg>> msg = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<List<Msg>> getMsg() {
        return msg;
    }

    public void setMsg(List<List<Msg>> msg) {
        this.msg = msg;
    }

}
