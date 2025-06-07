package org.officersam.navy.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

import java.util.List;

import static com.fs.starfarer.api.Global.getSector;

public class ot_addmarket {

    /**
     * Shorthand function for adding a market -- this is derived from tahlan mod
     */
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, List<SectorEntityToken> connectedEntities, String name,
                                           int popSize, List<String> marketConditions, List<String> submarkets, List<String> industries, float tariff,
                                           boolean isFreePort, boolean floatyJunk) {
        EconomyAPI globalEconomy = getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market"; //IMPORTANT this is a naming convention for markets. didn't want to have to pass in another variable :D

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, popSize);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        //newMarket.getTariff().modifyFlat("generator", tariff);
        newMarket.getTariff().setBaseValue(tariff);

        //Add submarkets, if any
        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        //Add conditions
        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        //Add industries
        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        //Set free port
        newMarket.setFreePort(isFreePort);

        //Add connected entities, if any
        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        //set market in global, factions, and assign market, also submarkets
        globalEconomy.addMarket(newMarket, floatyJunk);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        //Finally, return the newly-generated market
        return newMarket;
    }
}