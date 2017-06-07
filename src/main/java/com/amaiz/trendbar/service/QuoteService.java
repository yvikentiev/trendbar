package com.amaiz.trendbar.service;

import com.amaiz.trendbar.model.Quote;
import com.amaiz.trendbar.model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Quote service
 */
@Service
public class QuoteService {

    private final Random random;
    Logger logger = LoggerFactory.getLogger(QuoteService.class);
    private ScheduledExecutorService quoteExecutor;

    @Autowired
    private TrendBarService trendBarService;

    public QuoteService() {
        this.random = new Random();
    }

    public void init()
    {
        quoteExecutor = Executors.newScheduledThreadPool(Symbol.getSize());
        registerQuoteProvider(Symbol.EURUSD);
        registerQuoteProvider(Symbol.EURJPY);
    }

    public void shutdown()
    {
        quoteExecutor.shutdown();
    }

    /**
     * Register quote provider for symbol
     *
     * @param symbol
     */
    public void registerQuoteProvider(Symbol symbol) {
        quoteExecutor.scheduleAtFixedRate(new QuoteUpdater(symbol), 5000, 1000, TimeUnit.MILLISECONDS);
        trendBarService.registerTrendBars(symbol);
    }

    private Quote getQuote(Symbol symbol) {
        return new Quote(symbol, random.nextDouble() * 100, System.currentTimeMillis());
    }

    private class QuoteUpdater implements Runnable {

        private Symbol symbol;

        public QuoteUpdater(Symbol symbol) {
            this.symbol = symbol;
        }

        public void run() {
            try {
                Quote quote = getQuote(symbol);
                trendBarService.updateTrendBars(quote);
                logger.debug("" + quote);
            } catch (Exception e) {
                logger.error("Exception happened", e);
            }
        }
    }
}
