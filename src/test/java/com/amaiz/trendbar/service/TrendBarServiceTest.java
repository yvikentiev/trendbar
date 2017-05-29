package com.amaiz.trendbar.service;

import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import com.amaiz.trendbar.repository.TrendBarRepository;
import com.amaiz.trendbar.service.TrendBarService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

public class TrendBarServiceTest extends EasyMockSupport {

    @TestSubject
    TrendBarService trendBarService;
    @Mock
    TrendBarRepository trendBarRepository;

    @Before
    public void setUp() throws Exception {
        trendBarService = new TrendBarService();
        trendBarRepository = EasyMock.createMock(TrendBarRepository.class);
        trendBarService.setTrendBarRepository(trendBarRepository);
    }

    @Test
    public void testGetTrendBars()
    {
        expect(trendBarRepository.getTrendBars(Symbol.EURUSD, 0, 1, TrendBarPeriod.M1)).andReturn(Collections.emptyList());
        replay(trendBarRepository);
        List<TrendBar> trendBars = trendBarService.getTrendBars(Symbol.EURUSD, 0, 1, TrendBarPeriod.M1);
        Assert.assertNotNull(trendBars);
        Assert.assertTrue(trendBars.isEmpty());
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTrendBarsWrongArgs()
    {
        trendBarService.getTrendBars(Symbol.EURJPY, 1, 0, TrendBarPeriod.M1);
    }

}
