package officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class navalDedicatedShieldGenerator extends BaseHullMod {
    public static final float HULL_BONUS = -15f;
    public static final float ARMOR_BONUS = -20f;
    public static final float FLUX_BONUS = -25f;

    public static final float MIN_CREW_BONUS = 10f;

    public static final float MAX_SPEED_BONUS = -10f;

    public static final float SUPPLY_BONUS = 25f;

    @Override
    public int getDisplaySortOrder() {
        return 2002;
    }

    @Override
    public int getDisplayCategoryIndex() {
        return 3;
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getHullBonus().modifyPercent(id, HULL_BONUS);
        stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);

        stats.getFluxCapacity().modifyPercent(id,FLUX_BONUS);

        stats.getMinCrewMod().modifyPercent(id, MIN_CREW_BONUS);


        stats.getMaxSpeed().modifyPercent(id, MAX_SPEED_BONUS);


        stats.getMaxTurnRate().modifyPercent(id, MAX_SPEED_BONUS);

        stats.getSuppliesPerMonth().modifyPercent(id, SUPPLY_BONUS);
        stats.getSuppliesToRecover().modifyPercent(id, SUPPLY_BONUS);


        for (String blocked : BLOCKED_HULLMODS) {
            stats.getVariant().removeMod(blocked);
        }
    }

    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        ShieldAPI shield = ship.getShield();
        if (shield == null) {
            ship.setShield(ShieldAPI.ShieldType.FRONT, 0.2f, 0.5f, 360f);
        }
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;

        //HullDurability
        tooltip.addSectionHeading("Durability", Alignment.MID, pad);
        tooltip.addPara("Hull: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) HULL_BONUS));
        tooltip.addPara("Armor: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) ARMOR_BONUS));

        //CrewLogi
        tooltip.addSectionHeading("Crew & Logistics", Alignment.MID, pad);
        tooltip.addPara("Min Crew: %s", pad, Misc.getHighlightColor(),
                String.format("%d%%", (int) MIN_CREW_BONUS));
        tooltip.addPara("Supply Usage: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) SUPPLY_BONUS));

        // Mobile
        tooltip.addSectionHeading("Mobility", Alignment.MID, pad);
        tooltip.addPara("Top Speed: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) MAX_SPEED_BONUS));

        //Shield
        tooltip.addSectionHeading("Shield Installation", Alignment.MID, pad);
        tooltip.addPara("Installs a front-facing bubble shield with arc %s, upkeep %s, and efficiency %s.", pad, Misc.getHighlightColor(),
                "360°", "0.5", "0.2");
    }

    private static final Set<String> REQUIRED_HULLMODS = new HashSet<>(Set.of(
            "on_navalhull"
    ));

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(Set.of(
            "on_noarmor"
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
