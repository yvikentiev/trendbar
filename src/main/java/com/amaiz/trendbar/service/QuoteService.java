package com.amaiz.trendbar.service;

import com.amaiz.trendbar.model.Quote;
import com.amaiz.trendbar.model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Quote service
 */
@Service
public class QuoteService {

    Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private final Random random;

    private final ConcurrentHashMap<Symbol, BlockingQueue<Quote>> quotes = new ConcurrentHashMap<>();

    private ScheduledExecutorService quoteExecutor;

    public QuoteService() {
        this.random = new Random();
    }

    @PostConstruct
    public void init()
    {
        quoteExecutor = Executors.newScheduledThreadPool(Symbol.getSize());
        registerQuoteProvider(Symbol.EURUSD);
        registerQuoteProvider(Symbol.EURJPY);
    }

    public void stop()
    {
        quoteExecutor.shutdown();
    }

    /**
     * Register quote provider for symbol
     *
     * @param symbol
     */
    public void registerQuoteProvider(Symbol symbol) {
        BlockingQueue<Quote> queue = new ArrayBlockingQueue<>(1000);
        quotes.put(symbol, queue);
        quoteExecutor.scheduleAtFixedRate(new QuoteUpdater(symbol), 5000, 1000, TimeUnit.MILLISECONDS);
    }

    public Quote getQuote(Symbol symbol) {
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
                BlockingQueue<Quote> quoteQueue = quotes.get(symbol);
                quoteQueue.put(quote);
                logger.debug("" + quote);
            } catch (Exception e) {
                logger.error("Exception happened", e);
            }
        }
    }
}
