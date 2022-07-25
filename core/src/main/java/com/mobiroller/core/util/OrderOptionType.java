package com.mobiroller.core.util;

public enum OrderOptionType {

    Title("Title"),
    StockCode("StockCode"),
    Price("Price"),
    Currency("Currency"),
    Stock("Stock"),
    ShippingPrice("ShippingPrice"),
    CampaignPrice("CampaignPrice"),
    MaxQuantityPerOrder("MaxQuantityPerOrder"),
    OrderIndex("OrderIndex"),
    PublishmentDate("PublishmentDate"),
    EndDate("EndDate"),
    CalculatedPrice("CalculatedPrice"),
    StatsOrderCount("Stats.OrderCount");

    private String type = "";

    OrderOptionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
