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

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Trend bar services
 */
@Service
public class TrendBarService {

    private Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private ScheduledExecutorService trendBarExecutor;

    @Autowired
    private TrendBarRepository trendBarRepository;

    @Autowired
    private QuoteService quoteService;

    public TrendBarService() {
    }

    public void setTrendBarRepository(TrendBarRepository trendBarRepository) {
        this.trendBarRepository = trendBarRepository;
    }

    @PostConstruct
    public void init()
    {
        trendBarExecutor = Executors.newScheduledThreadPool(Symbol.getSize());
        registerTrendBars(Symbol.EURUSD);
        registerTrendBars(Symbol.EURJPY);
    }

    public void stop()
    {
        trendBarExecutor.shutdown();
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

    void registerTrendBars(Symbol symbol) {
        trendBarExecutor.scheduleAtFixedRate(new TrendBarTask(symbol), 5000, 1000, TimeUnit.MILLISECONDS);
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
            trendBar.setTimestamp(System.currentTimeMillis());
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

    private class TrendBarTask implements Runnable {

        private Symbol symbol;

        private List<TrendBar> trendBars;

        public TrendBarTask(Symbol symbol) {
            this.symbol = symbol;
            this.trendBars = Arrays.asList(
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.M1, System.currentTimeMillis()),
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.H1, System.currentTimeMillis()),
                new TrendBar(symbol, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.D1, System.currentTimeMillis())
            );
        }

        public void run() {
            try {
                Quote quote = quoteService.getQuote(symbol);
                for (TrendBar trendBar : trendBars) {
                    updateTrendBar(trendBar, quote);
                }
            } catch (Exception e) {
                logger.error("Exception happened", e);
            }
        }
    }
}
