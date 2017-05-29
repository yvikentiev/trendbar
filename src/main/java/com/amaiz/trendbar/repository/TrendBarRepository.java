package com.amaiz.trendbar.repository;

import com.amaiz.trendbar.model.Symbol;
import com.amaiz.trendbar.model.TrendBar;
import com.amaiz.trendbar.model.TrendBarPeriod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrendBarRepository extends CrudRepository<TrendBar, Long> {

    @Query("select t from TrendBar t where t.symbol = ?1 and t.timestamp >= ?2 and t.timestamp <= ?3 and t.period = ?4")
    List<TrendBar> getTrendBars(Symbol symbol, long from, long to, TrendBarPeriod period);
}