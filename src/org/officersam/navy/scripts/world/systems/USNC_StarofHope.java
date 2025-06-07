package org.officersam.navy.scripts.world.systems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;
import java.util.Arrays;

import static org.officersam.navy.scripts.world.systems.ot_addmarket.addMarketplace;

public class USNC_StarofHope {
    // Star Orbits
    //asteroids
    final float asteroidBelt1Dist = 2600f;
    final float asteroidBelt2Dist = 9500f;

    //relays
    final float buoy1Dist = 3000f;
    final float relay1Dist = 5100f;
    final float sensor1Dist = 9350f;

    //uncolonized planets
    final float rockyDist = 1550f;
    final float rocky2Dist = 2440f;
    final float rocky3Dist = 6950;
    final float iceDist = 5900f;

    //colonized planets
    final float colony1Dist = 3500f;
    final float colony2Dist = 4400f;
    //final float miningDist = 7500f;

    //stations

    //jumps
    final float jumpInnerDist = 2000f;
    final float jumpOuterDist = 5500f;
    final float jumpFringeDist = 10000f;


    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Hope");
        system.getLocation().set(1947, 3962);

        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        //praise the sun
        PlanetAPI hopeStar = system.initStar("U_StarofHope", "star_white", 650f, 500, 5, 0.5f, 2f);
        system.setLightColor(new Color(255, 255, 255));
        hopeStar.setCustomDescriptionId("usnc_starofhope_hope");

        //JumppointInner
        JumpPointAPI jumpPoint_inner = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner System Jump");

        jumpPoint_inner.setCircularOrbit(system.getEntityById("U_StarofHope"), 360 * (float) Math.random(), jumpInnerDist, 4000f);
        jumpPoint_inner.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_inner);

        //JumppointOuter
        JumpPointAPI jumpPoint_outer = Global.getFactory().createJumpPoint(
                "outer_jump",
                "Outer System Jump");

        jumpPoint_outer.setCircularOrbit(system.getEntityById("U_StarofHope"), 360 * (float) Math.random(), jumpOuterDist, 2000f);
        jumpPoint_outer.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_outer);

        //JumppointFring
        JumpPointAPI jumpPoint_fringe = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Fringe System Jump");

        jumpPoint_fringe.setCircularOrbit(system.getEntityById("U_StarofHope"), 360 * (float) Math.random(), jumpFringeDist, 6000f);
        jumpPoint_fringe.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_fringe);

        //autogenerate hyperspace points
        system.autogenerateHyperspaceJumpPoints(true, false);


        //asteroid belt1 ring
        system.addAsteroidBelt(hopeStar, 1250, asteroidBelt1Dist, 800f, 250f, 250f, Terrain.ASTEROID_BELT, "Kepler's Belt");
        system.addRingBand(hopeStar, "misc", "rings_asteroids0", 256f, 1, Color.gray, 256f, asteroidBelt1Dist - 200, 250f);
        system.addRingBand(hopeStar, "misc", "rings_asteroids0", 256f, 2, Color.gray, 256f, asteroidBelt1Dist, 350f);
        system.addRingBand(hopeStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt1Dist + 200, 300f);

        //asteroid belt2 ring
        system.addAsteroidBelt(hopeStar, 800, asteroidBelt1Dist, 400f, 250f, 250f, Terrain.ASTEROID_BELT, "Koopler's Belt");
        system.addRingBand(hopeStar, "misc", "rings_asteroids0", 256f, 1, Color.gray, 256f, asteroidBelt2Dist - 200, 750f);
        system.addRingBand(hopeStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt2Dist, 800f);


        // Relays
        SectorEntityToken hopeStar_relay = system.addCustomEntity("hopeStar_relay", // unique id
                "USNC Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "USNC"); // faction
        hopeStar_relay.setCircularOrbitPointingDown(hopeStar, MathUtils.getRandomNumberInRange(0f, 360f), relay1Dist, 220f);

        SectorEntityToken hopeStar_buoy = system.addCustomEntity("hopeStar_buoy", // unique id
                "USNC Nav Buoy", // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "USNC"); // faction
        hopeStar_buoy.setCircularOrbitPointingDown(hopeStar, MathUtils.getRandomNumberInRange(0f, 360f), buoy1Dist, 380f);

        SectorEntityToken hopeStar_sensor = system.addCustomEntity("hopeStar_sensor", // unique id
                "USNC Sensor Array", // name - if null, defaultName from custom_entities.json will be used
                "sensor_array", // type of object, defined in custom_entities.json
                "USNC"); // faction
        hopeStar_sensor.setCircularOrbitPointingDown(hopeStar, MathUtils.getRandomNumberInRange(0f, 360f), sensor1Dist, 500f);

        // rock1 Planet
        PlanetAPI rocky = system.addPlanet("u_rock", hopeStar, "Wrocoko", "rocky_metallic", 360 * (float) Math.random(), 100f, rockyDist, 200f);
        //rocky.setCustomDescriptionId(""); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(rocky, StarAge.AVERAGE);

        // rock2
        PlanetAPI rocky2 = system.addPlanet("u_rock2", hopeStar, "Rocoko", "barren2", 360f * (float) Math.random(), 125f, rocky2Dist, 220f);
        //rocky.setCustomDescriptionId(""); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(rocky2, StarAge.AVERAGE);

        // rock3
        PlanetAPI rocky3 = system.addPlanet("u_rock2", hopeStar, "Icrocoko", "rocky_ice", 360f * (float) Math.random(), 125f, rocky3Dist, 500f);
        //rocky.setCustomDescriptionId(""); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(rocky3, StarAge.AVERAGE);


        //Colonial Capital Planet
        PlanetAPI north;
        north = system.addPlanet("u_north",
                hopeStar,
                "Northera",
                "tundra",
                360f * (float) Math.random(),
                200f,
                colony1Dist,
                400f);

        north.setCustomDescriptionId("usnc_starofhope_north"); //reference descriptions.csv

        MarketAPI north_market = addMarketplace("USNC", north, null,
                "USNC Colony Operations",
                8,
                Arrays.asList(
                        Conditions.COLD,
                        Conditions.HABITABLE,
                        Conditions.RUINS_SCATTERED,

                        Conditions.POPULATION_8,
                        Conditions.REGIONAL_CAPITAL,

                        Conditions.ORE_MODERATE,
                        Conditions.FARMLAND_ADEQUATE,
                        Conditions.ORGANICS_ABUNDANT,

                        Conditions.STEALTH_MINEFIELDS
                ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,

                        Industries.FARMING,
                        Industries.MINING,
                        Industries.LIGHTINDUSTRY,

                        Industries.STARFORTRESS_MID,
                        Industries.HEAVYBATTERIES,
                        Industries.MILITARYBASE

                ),
                0.16f,
                false,
                true);

        north_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.BETA_CORE);

        north_market.getIndustry(Industries.FARMING).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.MINING).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.LIGHTINDUSTRY).setAICoreId(Commodities.BETA_CORE);

        north_market.getIndustry(Industries.STARFORTRESS_MID).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.BETA_CORE);
        north_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.BETA_CORE);

        //Colonial Industry Planet
        PlanetAPI east;
        east = system.addPlanet("u_east",
                hopeStar,
                "Eastera",
                "tundra",
                360f * (float) Math.random(),
                190f,
                colony2Dist,
                365f);

        east.setCustomDescriptionId("usnc_starofhope_east"); //reference descriptions.csv

        MarketAPI east_market = addMarketplace("USNC", east, null,
                "USNC Industrial Operations",
                6,
                Arrays.asList(
                        Conditions.COLD,
                        Conditions.HABITABLE,
                        Conditions.RUINS_WIDESPREAD,

                        Conditions.POPULATION_6,

                        Conditions.ORE_SPARSE,
                        Conditions.RARE_ORE_ABUNDANT,
                        Conditions.FARMLAND_ADEQUATE,
                        Conditions.ORGANICS_ABUNDANT,

                        Conditions.STEALTH_MINEFIELDS
                ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,

                        Industries.FARMING,
                        Industries.TECHMINING,
                        Industries.MINING,
                        Industries.HEAVYINDUSTRY,
                        Industries.REFINING,

                        Industries.STARFORTRESS_MID,
                        Industries.HEAVYBATTERIES,
                        Industries.PATROLHQ

                ),
                0.16f,
                false,
                true);

        east_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.BETA_CORE);

        east_market.getIndustry(Industries.FARMING).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.TECHMINING).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.MINING).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.HEAVYINDUSTRY).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.REFINING).setAICoreId(Commodities.BETA_CORE);

        east_market.getIndustry(Industries.STARFORTRESS_MID).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.BETA_CORE);
        east_market.getIndustry(Industries.PATROLHQ).setAICoreId(Commodities.BETA_CORE);


        // ice
        PlanetAPI ice = system.addPlanet("u_ice", hopeStar, "Icrocoko", "ice_giant", 360f * (float) Math.random(), 375f, iceDist, 600f);
        //rocky.setCustomDescriptionId(""); //reference descriptions.csv
        PlanetConditionGenerator.generateConditionsForPlanet(ice, StarAge.AVERAGE);

        // Asteroid Field
        SectorEntityToken asteroidF1 = system.addTerrain(Terrain.ASTEROID_FIELD,
                new AsteroidFieldTerrainPlugin.AsteroidFieldParams(400f, 600f, 30, 80, 2f, 25f, "Asteroids Field"));
        asteroidF1.setCircularOrbit(ice, 360f * (float) Math.random(), 0f, 30f);

        // USNC Command Station
        SectorEntityToken usncc = system.addCustomEntity("u_usnc", "USNC Command Station", "orbital_dockyard", "USNC");
        usncc.setCircularOrbitPointingDown(ice, 360f * (float) Math.random(), 460f, 82f);
        usncc.setCustomDescriptionId("usnc_starofhope_dockyard");
        MarketAPI usncc_market = addMarketplace("USNC", usncc, null,
                "USNC Command Station",
                4,
                Arrays.asList(
                        Conditions.POPULATION_4,
                        Conditions.NO_ATMOSPHERE,
                        Conditions.LOW_GRAVITY,
                        Conditions.OUTPOST,

                        Conditions.ORE_MODERATE,
                        Conditions.VOLATILES_ABUNDANT,

                        Conditions.AI_CORE_ADMIN
                ),
                Arrays.asList(
                        Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN,
                        Submarkets.SUBMARKET_STORAGE,
                        Submarkets.SUBMARKET_BLACK
                ),
                Arrays.asList(
                        Industries.POPULATION,
                        Industries.MEGAPORT,
                        Industries.WAYSTATION,

                        Industries.MINING,
                        Industries.FUELPROD,
                        Industries.ORBITALWORKS,

                        Industries.HEAVYBATTERIES,
                        Industries.HIGHCOMMAND
                ),
                0.15f,
                true,
                false);

        usncc_market.getIndustry(Industries.POPULATION).setAICoreId(Commodities.BETA_CORE);
        usncc_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.BETA_CORE);
        usncc_market.getIndustry(Industries.WAYSTATION).setAICoreId(Commodities.BETA_CORE);

        usncc_market.getIndustry(Industries.MINING).setAICoreId(Commodities.BETA_CORE);
        usncc_market.getIndustry(Industries.FUELPROD).setAICoreId(Commodities.BETA_CORE);
        usncc_market.getIndustry(Industries.ORBITALWORKS).setAICoreId(Commodities.BETA_CORE);

        usncc_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.BETA_CORE);
        usncc_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.BETA_CORE);

    }

    private void addDerelict(StarSystemAPI system, SectorEntityToken focus, String variantId, ShipRecoverySpecial.ShipCondition condition, float orbitRadius, boolean recoverable) {

        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), true);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true);

        float orbitDays = 60f;
        ship.setCircularOrbit(focus, (float) MathUtils.getRandomNumberInRange(-2, 2) + 90f, orbitRadius, orbitDays);

        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator creator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
        }
    }

}


