package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.trade.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cory on 5/15/2017.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNameAndSetAndPremium(String name, String set, Boolean isPremium);
}
