package officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class navalFreightConversion extends BaseHullMod {

    public static final float HULL_BONUS = -25f;
    public static final float MAX_CREW_BONUS = -50f;

    public static final float CARGO_BONUS = 200f;
    public static final float SUPPLY_BONUS = -25f;
    public static final float FUEL_BONUS = 50f;

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
        stats.getMaxCrewMod().modifyPercent(id, MAX_CREW_BONUS);
        stats.getCargoMod().modifyPercent(id, CARGO_BONUS);
        stats.getSuppliesPerMonth().modifyPercent(id, SUPPLY_BONUS);
        stats.getFuelMod().modifyPercent(id, FUEL_BONUS);

        for (String blocked : BLOCKED_HULLMODS) {
            stats.getVariant().removeMod(blocked);
        }
    }
 
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;

        //CrewLogi
        tooltip.addSectionHeading("Logistics Modification", Alignment.MID, pad);
        tooltip.addPara("Hull: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) HULL_BONUS));
        tooltip.addPara("Max Crew: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) MAX_CREW_BONUS));
        tooltip.addPara("Cargo Capacity: %s", pad, Misc.getPositiveHighlightColor(),
                String.format("%d%%", (int) CARGO_BONUS));
        tooltip.addPara("Supply Usage: %s", pad, Misc.getPositiveHighlightColor(),
                String.format("%d%%", (int) SUPPLY_BONUS));
        tooltip.addPara("Fuel Capacity: %s", pad, Misc.getPositiveHighlightColor(),
                String.format("%d%%", (int) FUEL_BONUS));

    }

    private static final Set<String> REQUIRED_HULLMODS = new HashSet<>(Set.of(
            "on_navalhull"
    ));

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(Set.of(
            "on_passenger",
            "on_armorpack",
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
