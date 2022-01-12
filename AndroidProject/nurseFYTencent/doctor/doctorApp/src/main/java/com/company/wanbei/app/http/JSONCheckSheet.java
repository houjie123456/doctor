package com.company.wanbei.app.http;

import com.company.wanbei.app.bean.AddressBean;
import com.company.wanbei.app.bean.CheckSheetBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YC on 2018/8/4.
 */

public class JSONCheckSheet {
    private String code;
    @SerializedName("msgbox")
    private String msgBox;
    private ArrayList<CheckSheetBean> table;

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

    public ArrayList<CheckSheetBean> getTable() {
        return table;
    }

    public void setTable(ArrayList<CheckSheetBean> table) {
        this.table = table;
    }
}
