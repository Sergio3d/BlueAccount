package com.cumn.blueaccount;

import com.google.gson.annotations.SerializedName;

public class ExangerateResponse {

    @SerializedName("base")
    private String base;

    @SerializedName("date")
    private String date;

    @SerializedName("rates")
    private ExangeRate rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ExangeRate getRates() {
        return rates;
    }

    public void setRates(ExangeRate rates) {
        this.rates = rates;
    }
}
