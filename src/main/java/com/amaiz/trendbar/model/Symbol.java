package com.amaiz.trendbar.model;

public enum Symbol {
    EURUSD, EURJPY;

    public static int getSize() {
        return Symbol.values().length;
    }
}