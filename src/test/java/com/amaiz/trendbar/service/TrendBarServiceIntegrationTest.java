package com.amaiz.trendbar.service;

import com.amaiz.trendbar.Application;
import com.amaiz.trendbar.model.Quote;
import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Application.class)
public class TrendBarServiceIntegrationTest extends EasyMockSupport {

    @Autowired
    TrendBarService trendBarService;

    @Test
    public void testTrendBarService() throws InterruptedException
    {
        trendBarService.registerTrendBars(Symbol.EURUSD);
        Quote quote1 = new Quote(Symbol.EURUSD, 10, System.currentTimeMillis());
        Quote quote2 = new Quote(Symbol.EURUSD, 90, System.currentTimeMillis());
        trendBarService.updateTrendBars(quote1);
        TrendBarPeriod.M1.getTimeUnit().sleep(TrendBarPeriod.M1.getTime());
        trendBarService.updateTrendBars(quote2);
        List<TrendBar> trendBars = trendBarService.getTrendBars(Symbol. EURUSD, 0, Long.MAX_VALUE, TrendBarPeriod.M1);
        Assert.assertNotNull(trendBars);
        Assert.assertEquals(1, trendBars.size());
        Assert.assertEquals(Symbol.EURUSD, trendBars.get(0).getSymbol());
        Assert.assertEquals(quote1.getPrice(), trendBars.get(0).getOpenPrice(), 0.01);
        Assert.assertEquals(quote1.getPrice(), trendBars.get(0).getLowPrice(), 0.01);
        Assert.assertEquals(quote2.getPrice(), trendBars.get(0).getHighPrice(), 0.01);
        Assert.assertEquals(quote2.getPrice(), trendBars.get(0).getClosePrice(), 0.01);
    }

}
