package com.cd.bot.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Cory on 5/15/2017.
 */
public interface BotRepository extends JpaRepository<Bot, Long> {
    Bot findByName(String name);
}
