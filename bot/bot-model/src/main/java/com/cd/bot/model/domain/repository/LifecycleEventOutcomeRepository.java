package com.cd.bot.model.domain.repository;

import com.cd.bot.model.domain.bot.LifecycleEventOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cory on 6/10/2017.
 */
@Repository
public interface LifecycleEventOutcomeRepository extends JpaRepository<LifecycleEventOutcome, Long> { }
