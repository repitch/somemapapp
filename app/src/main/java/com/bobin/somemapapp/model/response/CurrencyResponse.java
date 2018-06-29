package com.bobin.somemapapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrencyResponse implements Serializable {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("name")
    @Expose
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
