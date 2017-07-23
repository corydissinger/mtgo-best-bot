package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.trade.ExecutedTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cory on 5/22/2017.
 */
@Repository
public interface ExecutedTradeRepository extends JpaRepository<ExecutedTrade, Long> {
}
