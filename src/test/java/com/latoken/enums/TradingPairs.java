package com.latoken.enums;

public enum TradingPairs {
    USDT("USDT"),
    BTC("BTC"),
    ETH("ETH"),
    TRX("TRX"),
    LA("LA");

    private String tradingPair;

    TradingPairs(String tradingPair) {
        this.tradingPair = tradingPair;
    }

    public String getTradingPair() {
        return tradingPair;
    }
}
