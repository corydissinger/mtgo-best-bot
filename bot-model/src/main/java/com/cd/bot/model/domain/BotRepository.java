package com.cd.bot.model.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cory on 5/15/2017.
 */
@Repository
public interface BotRepository extends JpaRepository<Bot, Long> {
    Bot findByName(String name);
}
