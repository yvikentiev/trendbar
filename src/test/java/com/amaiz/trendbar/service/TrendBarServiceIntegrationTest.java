package com.amaiz.trendbar.service;

import com.amaiz.trendbar.Application;
import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import com.amaiz.trendbar.repository.TrendBarRepository;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.spec.MGF1ParameterSpec;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Application.class)
public class TrendBarServiceIntegrationTest extends EasyMockSupport {

    @Autowired
    QuoteService quoteService;

    @Autowired
    TrendBarService trendBarService;

    @Test
    public void testTrendBarService() throws InterruptedException
    {
        TrendBarPeriod.M1.getTimeUnit().sleep(TrendBarPeriod.M1.getTime());
        List<TrendBar> trendBars = trendBarService.getTrendBars(Symbol. EURUSD, 0, Long.MAX_VALUE, TrendBarPeriod.M1);
        quoteService.stop();
        trendBarService.stop();
        Assert.assertNotNull(trendBars);
        Assert.assertEquals(1, trendBars.size());
    }

}
