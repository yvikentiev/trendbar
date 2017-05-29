package com.amaiz.trendbar.repository;

import com.amaiz.trendbar.Application;
import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=Application.class)
public class TrendBarRepositoryTest {

    @Autowired
    TrendBarRepository trendBarRepository;

    @Test
    public void testTrendBar()
    {
        TrendBar trendBar = new TrendBar(Symbol.EURUSD, 0.0, 0.0, Double.MIN_VALUE, Double.MAX_VALUE, TrendBarPeriod.M1, System.currentTimeMillis());
        trendBarRepository.save(trendBar);
        List<TrendBar> trendBars =  trendBarRepository.getTrendBars(Symbol.EURUSD, 0,System.currentTimeMillis(), TrendBarPeriod.M1);
        Assert.assertTrue(trendBars.size() == 1);
    }

}
