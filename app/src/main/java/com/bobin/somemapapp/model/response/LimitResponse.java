package com.bobin.somemapapp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LimitResponse implements Serializable {
    @SerializedName("min")
    @Expose
    private int min;
    @SerializedName("max")
    @Expose
    private int max;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("currency")
    @Expose
    private CurrencyResponse currency;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CurrencyResponse getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyResponse currency) {
        this.currency = currency;
    }
}
