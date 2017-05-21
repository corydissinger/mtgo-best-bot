package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.model.domain.OwnedTradeableCard;
import com.cd.bot.model.domain.PlayerBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Cory on 5/15/2017.
 */
@Repository
public interface OwnedTradeableCardRepository extends JpaRepository<OwnedTradeableCard, Long> {
    List<OwnedTradeableCard> findByPlayerBot(PlayerBot playerBot);
}
