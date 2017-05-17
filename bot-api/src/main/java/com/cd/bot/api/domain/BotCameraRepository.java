package com.cd.bot.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Cory on 5/15/2017.
 */
public interface BotCameraRepository extends JpaRepository<BotCamera, Long> {
    List<BotCamera> findByBot(Bot bot);
}
