package officersam.navy.scripts.data.hullmod;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.HullModFleetEffect;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;


public class navalSensorEnhancement extends BaseHullMod implements HullModFleetEffect {

    public static final float MIN_CREW_BONUS = 5f;
    public static final String FLEET_SENSOR_RANGE = "naval_sensor_enhancement_mod";
    public static final float MIN_CR = 0.1f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        stats.getMinCrewMod().modifyPercent(id, MIN_CREW_BONUS);

        float SENSOR_RANGE_BONUS = 0f;
        float SENSOR_PROFILE_BONUS = 0f;

        switch (hullSize) {
            case FRIGATE -> {
                    SENSOR_RANGE_BONUS = 15f;
                    SENSOR_PROFILE_BONUS = 5f;
            }
            case DESTROYER -> {
                    SENSOR_RANGE_BONUS = 20f;
                    SENSOR_PROFILE_BONUS = 10f;
            }
            case CRUISER -> {
                    SENSOR_RANGE_BONUS = 40f;
                    SENSOR_PROFILE_BONUS = 15f;
            }
            case CAPITAL_SHIP -> {
                    SENSOR_RANGE_BONUS = 80f;
                    SENSOR_PROFILE_BONUS = 25f;
            }
        }

        stats.getSensorStrength().modifyPercent(id, SENSOR_RANGE_BONUS);
        stats.getSensorProfile().modifyPercent(id, SENSOR_PROFILE_BONUS);

        // Used for fleet-wide sync
        stats.getDynamic().getMod(FLEET_SENSOR_RANGE).modifyFlat(id, SENSOR_RANGE_BONUS);

        for (String blocked : BLOCKED_HULLMODS) {
                stats.getVariant().removeMod(blocked);
        }
    }


    @Override
    public void onFleetSync(CampaignFleetAPI fleet) {
        float modifier = getAdjustedSensorModifier(fleet, null, 0f);
        if (modifier <= 0f) {
            fleet.getSensorRangeMod().unmodifyFlat(FLEET_SENSOR_RANGE);
        } else {
            fleet.getSensorRangeMod().modifyFlat(FLEET_SENSOR_RANGE, modifier, "Enhanced sensors from naval ships");
        }
    }

    private static float getAdjustedSensorModifier(CampaignFleetAPI fleet, String skipId, float add) {
        float max = 0f;
        float total = 0f;
        for (FleetMemberAPI member : fleet.getFleetData().getMembersListCopy()) {
            if (member.isMothballed() || member.getRepairTracker().getCR() < MIN_CR) continue;
            if (member.getId().equals(skipId)) continue;

            float v = member.getStats().getDynamic().getMod(FLEET_SENSOR_RANGE).computeEffective(0f);
            if (v <= 0f) continue;
            if (v > max) max = v;
            total += v;
        }
        if (add > max) max = add;
        total += add;

        if (max <= 0f) return 0f;

        float units = total / max;
        float mult = Misc.logOfBase(2.5f, units) + 1f;
        return Math.round(total * mult / units * 100f) / 100f;
    }

    @Override
    public void advanceInCampaign(CampaignFleetAPI fleet) {
    }

    @Override
    public boolean withAdvanceInCampaign() {
        return false;
    }

    @Override
    public boolean withOnFleetSync() {
            return true;
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
            return null;
    }

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 3f;
        float opad = 10f;
        Color h = Misc.getHighlightColor();

        tooltip.addSectionHeading("Sensor Enhancements", Alignment.MID, opad);

        tooltip.addPara("Increases sensor range and profile based on hull size:", opad);
        tooltip.addPara("Frigate: +15%% range / +5%% profile", pad);
        tooltip.addPara("Destroyer: +20%% range / +10%% profile", pad);
        tooltip.addPara("Cruiser: +40%% range / +15%% profile", pad);
        tooltip.addPara("Capital Ship: +80%% range / +25%% profile", pad);

        tooltip.addPara("Each ship equipped with this hullmod adds to the fleet's sensor range, with diminishing returns.", opad);

        if (!isForModSpec && ship != null && Global.getSector() != null) {
            CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
            float fleetMod = getAdjustedSensorModifier(fleet, null, 0f);
            float currShipMod = ship.getMutableStats().getDynamic().getMod(FLEET_SENSOR_RANGE).computeEffective(0f);
            float withOneMore = getAdjustedSensorModifier(fleet, null, currShipMod);
            float withoutThis = getAdjustedSensorModifier(fleet, ship.getFleetMemberId(), 0f);

            tooltip.addPara("Current fleetwide sensor range increase: %s units.", opad, h, String.valueOf((int) fleetMod));

            if (fleetMod > currShipMod) {
                tooltip.addPara("Removing this ship would reduce it to %s. Adding another similar ship would increase it to %s.", pad, h,
                        String.valueOf((int) withoutThis), String.valueOf((int) withOneMore));
            } else {
                tooltip.addPara("Adding another similar ship would increase it to %s.", pad, h,
                        String.valueOf((int) withOneMore));
            }
        }
    }

    private static final Set<String> REQUIRED_HULLMODS = new HashSet<>(Set.of(
            "on_navalhull"
    ));

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(Set.of(
            //"on_armorpack"
    ));

    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        Set<String> installed = (Set<String>) ship.getVariant().getHullMods();

        if (!installed.containsAll(REQUIRED_HULLMODS)) {
            return false;
        }

        for (String blocked : BLOCKED_HULLMODS) {
            if (installed.contains(blocked)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getUnapplicableReason(ShipAPI ship) {
        Set<String> installed = (Set<String>) ship.getVariant().getHullMods();

        for (String required : REQUIRED_HULLMODS) {
            if (!installed.contains(required)) {
                return "Requires: " + formatHullmodName(required);
            }
        }

        for (String blocked : BLOCKED_HULLMODS) {
            if (installed.contains(blocked)) {
                return "Incompatible with: " + formatHullmodName(blocked);
            }
        }

        return null;
    }

    private String formatHullmodName(String id) {
        String[] parts = id.split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1)).append(" ");
            }
        }
        return result.toString().trim();
    }
}
