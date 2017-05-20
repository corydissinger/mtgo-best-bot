package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.BotCamera;
import com.cd.bot.model.domain.PlayerBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by Cory on 5/15/2017.
 */
@Repository
public interface BotCameraRepository extends JpaRepository<BotCamera, Long> {
    @Transactional
    List<BotCamera> findByPlayerBot(PlayerBot playerBot);

    @Modifying
    @Transactional
    @Query("delete from BotCamera bc where bc.timeTaken < ?1")
    void deleteOlderThan(Date timeTaken);
}
