package com.amaiz.trendbar.service;

import com.amaiz.trendbar.model.Quote;
import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import com.amaiz.trendbar.repository.TrendBarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Trend bar services
 */
@Service
public class TrendBarService {

    private Logger logger = LoggerFactory.getLogger(QuoteService.class);

    @Autowired
    private TrendBarRepository trendBarRepository;

    private ConcurrentHashMap<Symbol, List<TrendBar>> trendBarsMap = new ConcurrentHashMap<>();

    public TrendBarService() {
    }

    public void setTrendBarRepository(TrendBarRepository trendBarRepository) {
        this.trendBarRepository = trendBarRepository;
    }

    void registerTrendBars(Symbol symbol) {
        this.trendBarsMap.put(symbol, Arrays.asList(
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.M1, -1),
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.H1, -1),
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.D1, -1)));
    }

    /**
     * Get trend bars
     * @param symbol
     * @param from
     * @param to
     * @param period
     * @return
     * @throws IllegalArgumentException
     */
    public List<TrendBar> getTrendBars(Symbol symbol, long from, long to, TrendBarPeriod period) throws IllegalArgumentException {
        if (from > to) {
            throw new IllegalArgumentException("From should be less than to");
        }
        return trendBarRepository.getTrendBars(symbol, from, to, period);
    }

    public List<TrendBar> updateTrendBars(Quote quote) {
        List<TrendBar> trendBars = trendBarsMap.get(quote.getSymbol());
        if (trendBars == null)
            throw new IllegalStateException("Trendbar is not registered for symbol " + quote.getSymbol());
        for (TrendBar trendBar : trendBarsMap.get(quote.getSymbol())) {
            updateTrendBar(trendBar, quote);
        }
        return trendBars;
    }

    /**
     * Update trend
     *
     * @param trendBar
     * @param quote
     */
    private void updateTrendBar(TrendBar trendBar, Quote quote) {
        double quotePrice = quote.getPrice();
        if (trendBar.getTimestamp() < 0) {
            trendBar.setOpenPrice(quotePrice);
            trendBar.setTimestamp(quote.getTimestamp());
        } else {
            trendBar.setClosePrice(quotePrice);
        }
        if (quotePrice < trendBar.getLowPrice()) {
            trendBar.setLowPrice(quotePrice);
        }
        if (quotePrice > trendBar.getHighPrice()) {
            trendBar.setHighPrice(quotePrice);
        }
        if (System.currentTimeMillis() - trendBar.getTimestamp() > trendBar.getPeriod().getTimeInMillis()) {
            trendBarRepository.save(trendBar);
            logger.debug("" + trendBar);
            trendBar.setId(null);
            trendBar.setTimestamp(-1);
            trendBar.setClosePrice(0.0);
            trendBar.setLowPrice(Double.MAX_VALUE);
            trendBar.setHighPrice(Double.MIN_VALUE);
        }
    }

}
