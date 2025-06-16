package officersam.navy.scripts.world.systems;

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

import static officersam.navy.scripts.world.systems.ot_addmarket.addMarketplace;

public class SRAC_StarofStonia {
    // Star Orbits
    //asteroids
    final float asteroidBelt1Dist = 2600f;
    final float asteroidBelt2Dist = 9500f;

    //relays
    final float buoy1Dist = 3000f;
    final float relay1Dist = 5100f;
    final float sensor1Dist = 9350f;

    //uncolonized planets
    //final float rockyDist = 1550f;
    //final float rocky2Dist = 2440f;
    //final float rocky3Dist = 6950;
   //final float iceDist = 5900f;

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

        StarSystemAPI system = sector.createStarSystem("Stonia");
        system.getLocation().set(1947, 3962);

        system.setBackgroundTextureFilename("graphics/backgrounds/background5.jpg");

        //praise the sun
        PlanetAPI stoniaStar = system.initStar("S_StarofStonia", "star_white", 650f, 500, 5, 0.5f, 2f);
        system.setLightColor(new Color(255, 255, 255));
        stoniaStar.setCustomDescriptionId("usnc_starofhope_hope");

        //JumppointInner
        JumpPointAPI jumpPoint_inner = Global.getFactory().createJumpPoint(
                "inner_jump",
                "Inner System Jump");

        jumpPoint_inner.setCircularOrbit(system.getEntityById("S_StarofStonia"), 360 * (float) Math.random(), jumpInnerDist, 4000f);
        jumpPoint_inner.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_inner);

        //JumppointOuter
        JumpPointAPI jumpPoint_outer = Global.getFactory().createJumpPoint(
                "outer_jump",
                "Outer System Jump");

        jumpPoint_outer.setCircularOrbit(system.getEntityById("S_StarofStonia"), 360 * (float) Math.random(), jumpOuterDist, 2000f);
        jumpPoint_outer.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_outer);

        //JumppointFring
        JumpPointAPI jumpPoint_fringe = Global.getFactory().createJumpPoint(
                "fringe_jump",
                "Fringe System Jump");

        jumpPoint_fringe.setCircularOrbit(system.getEntityById("S_StarofStonia"), 360 * (float) Math.random(), jumpFringeDist, 6000f);
        jumpPoint_fringe.setStandardWormholeToHyperspaceVisual();

        system.addEntity(jumpPoint_fringe);

        //autogenerate hyperspace points
        system.autogenerateHyperspaceJumpPoints(true, false);

        //asteroid belt1 ring
        system.addAsteroidBelt(stoniaStar, 1250, asteroidBelt1Dist, 800f, 250f, 250f, Terrain.ASTEROID_BELT, "Kepler's Belt");
        system.addRingBand(stoniaStar, "misc", "rings_asteroids0", 256f, 1, Color.gray, 256f, asteroidBelt1Dist - 200, 250f);
        system.addRingBand(stoniaStar, "misc", "rings_asteroids0", 256f, 2, Color.gray, 256f, asteroidBelt1Dist, 350f);
        system.addRingBand(stoniaStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt1Dist + 200, 300f);

        //asteroid belt2 ring
        system.addAsteroidBelt(stoniaStar, 800, asteroidBelt1Dist, 400f, 250f, 250f, Terrain.ASTEROID_BELT, "Koopler's Belt");
        system.addRingBand(stoniaStar, "misc", "rings_asteroids0", 256f, 1, Color.gray, 256f, asteroidBelt2Dist - 200, 750f);
        system.addRingBand(stoniaStar, "misc", "rings_asteroids0", 256f, 0, Color.gray, 256f, asteroidBelt2Dist, 800f);

        // Relays
        SectorEntityToken stoniaStar_relay = system.addCustomEntity("stoniaStar_relay", // unique id
                "SRAC Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "SRAC"); // faction
        stoniaStar_relay.setCircularOrbitPointingDown(stoniaStar, MathUtils.getRandomNumberInRange(0f, 360f), relay1Dist, 220f);

        SectorEntityToken stoniaStar_buoy = system.addCustomEntity("stoniaStar_buoy", // unique id
                "SRAC Nav Buoy", // name - if null, defaultName from custom_entities.json will be used
                "nav_buoy", // type of object, defined in custom_entities.json
                "SRAC"); // faction
        stoniaStar_buoy.setCircularOrbitPointingDown(stoniaStar, MathUtils.getRandomNumberInRange(0f, 360f), buoy1Dist, 380f);

        SectorEntityToken stoniaStar_sensor = system.addCustomEntity("stoniaStar_sensor", // unique id
                "SRAC Sensor Array", // name - if null, defaultName from custom_entities.json will be used
                "sensor_array", // type of object, defined in custom_entities.json
                "SRAC"); // faction
        stoniaStar_sensor.setCircularOrbitPointingDown(stoniaStar, MathUtils.getRandomNumberInRange(0f, 360f), sensor1Dist, 500f);


        //Colonial Capital Planet
        PlanetAPI north;
        north = system.addPlanet("s_north",
                stoniaStar,
                "Northera",
                "tundra",
                360f * (float) Math.random(),
                200f,
                colony1Dist,
                400f);

        north.setCustomDescriptionId("usnc_starofhope_north"); //reference descriptions.csv

        MarketAPI north_market = addMarketplace("SRAC", north, null,
                "SRAC Colony Operations",
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
                        Industries.HIGHCOMMAND

                ),
                0.15f,
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
        north_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.BETA_CORE);

        //Colonial Industry Planet
        PlanetAPI east;
        east = system.addPlanet("s_east",
                stoniaStar,
                "Eastera",
                "tundra",
                360f * (float) Math.random(),
                190f,
                colony2Dist,
                365f);

        east.setCustomDescriptionId("usnc_starofhope_east"); //reference descriptions.csv

        MarketAPI east_market = addMarketplace("SRAC", east, null,
                "SRAC Industrial Operations",
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
                0.15f,
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


