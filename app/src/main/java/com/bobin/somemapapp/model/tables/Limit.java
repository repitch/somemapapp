package com.bobin.somemapapp.model.tables;

import io.realm.RealmObject;

public class Limit extends RealmObject {
    private int min;
    private int max;
    private int amount;
    private Currency currency;

    public boolean isEmpty() {
        return min == 0 && max == 0 && amount == 0;
    }

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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return getCurrency().getName();
    }
}
