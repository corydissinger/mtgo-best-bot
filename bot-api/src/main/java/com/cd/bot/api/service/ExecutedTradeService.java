package com.cd.bot.api.service;

import com.cd.bot.model.domain.*;
import com.cd.bot.model.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Cory on 5/22/2017.
 */
@Service
public class ExecutedTradeService {
    @Autowired
    private ExecutedTradeRepository executedTradeRepository;

    @Autowired
    private NpcBotRepository npcBotRepository;

    @Autowired
    private NpcTradeableCardRepository npcTradeableCardRepository;

    @Autowired
    private PlayerBotRepository playerBotRepository;

    @Autowired
    private OwnedTradeableCardRepository ownedTradeableCardRepository;

    @Transactional
    public Long recordExecutedTrade(ExecutedTrade executedTrade) {
        if(executedTrade.getPlayerBot().getId() == null) {
            executedTrade.setPlayerBot(playerBotRepository.findByName(executedTrade.getPlayerBot().getName()));
        } else if (!playerBotRepository.exists(executedTrade.getPlayerBot().getId())){
            throw new RuntimeException("Request is missing player bot!");
        }

        if(executedTrade.getNpcBot() != null && executedTrade.getNpcBot().getId() == null) {
            NpcBot exists = npcBotRepository.findByName(executedTrade.getNpcBot().getName());

            if(exists == null) {
                exists = npcBotRepository.save(executedTrade.getNpcBot());
            }

            executedTrade.setNpcBot(exists);
        }

        final List<OwnedTradeableCard> allOwnedCards = ownedTradeableCardRepository.findByPlayerBot(executedTrade.getPlayerBot());

        final Date timeRecorded = new Date();
        final List<OwnedTradeableCard> soldOwnedCards = allOwnedCards.stream().filter(ownedCard -> executedTrade.getSellOrder().getOwnedTradeableCards().contains(ownedCard)).collect(Collectors.toList());
        final SellOrder wiredSellOrder = new SellOrder(timeRecorded, soldOwnedCards);

        final List<NpcTradeableCard> boughtCards = executedTrade.getBuyOrder().getNpcTradeableCards().stream().map(aRawTradeableCard -> npcTradeableCardRepository.save(aRawTradeableCard)).collect(Collectors.toList());
        final BuyOrder wiredBuyOrder = new BuyOrder(timeRecorded, boughtCards);

        Long tradeId = executedTradeRepository.save(new ExecutedTrade(executedTrade.getPlayerBot(), executedTrade.getNpcBot(), wiredSellOrder, wiredBuyOrder)).getId();

        return tradeId;
    }
}
