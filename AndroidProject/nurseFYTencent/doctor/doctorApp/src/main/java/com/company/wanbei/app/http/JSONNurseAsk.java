package com.company.wanbei.app.http;

import com.company.wanbei.app.bean.AddressBean;
import com.company.wanbei.app.bean.NurseAskBean;
import com.company.wanbei.app.bean.NurseAskScheduleBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YC on 2018/8/4.
 */

public class JSONNurseAsk {
    private String code;
    @SerializedName("msgbox")
    private String msgBox;
    private NurseAskBean infoJson;
    private ArrayList<NurseAskScheduleBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgBox() {
        return msgBox;
    }

    public void setMsgBox(String msgBox) {
        this.msgBox = msgBox;
    }

    public NurseAskBean getInfoJson() {
        return infoJson;
    }

    public void setInfoJson(NurseAskBean infoJson) {
        this.infoJson = infoJson;
    }

    public ArrayList<NurseAskScheduleBean> getData() {
        return data;
    }

    public void setData(ArrayList<NurseAskScheduleBean> data) {
        this.data = data;
    }
}
