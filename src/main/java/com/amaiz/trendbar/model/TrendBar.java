package com.amaiz.trendbar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Trend bar
 * <p>
 * open price - a price for a given symbol at the beginning of a trendbar period
 * close price - a price of from the last received quote in this trendbar period
 * high price - max value of the price during a period
 * low price - min value of the price during a period
 * trendbar period - certain time interval during which quotes are accumulated (e.g. M1 - one
 * minute, H1 - one hour, D1 - one day and so on).
 * timestamp - a moment of time at which trendbar is created
 */
@Entity
public class TrendBar {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Symbol symbol;

    @Column(nullable = false)
    private double openPrice;

    @Column(nullable = false)
    private double closePrice;

    @Column(nullable = false)
    private double highPrice;

    @Column(nullable = false)
    private double lowPrice;

    @Column(nullable = false)
    private TrendBarPeriod period;

    @Column(nullable = false)
    private long timestamp;

    public TrendBar() {
    }

    public TrendBar(Symbol symbol, double openPrice, double closePrice, double highPrice, double lowPrice, TrendBarPeriod period, long timestamp) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.period = period;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public TrendBarPeriod getPeriod() {
        return period;
    }

    public void setPeriod(TrendBarPeriod period) {
        this.period = period;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TrendBar{" +
                "id=" + id +
                ", symbol=" + symbol +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", period=" + period +
                ", timestamp=" + timestamp +
                '}';
    }
}
