package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.trade.NpcBotCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cory on 5/21/2017.
 */
@Repository
public interface NpcBotCreditRepositry extends JpaRepository<NpcBotCredit, Long> { }
