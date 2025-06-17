package officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class navalSAP1 extends BaseHullMod {

    public static final float SHIELD_DAMAGE_BONUS = 10f;
    public static final float HULL_DAMAGE_BONUS = 50f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getDamageToTargetShieldsMult().modifyPercent(id, SHIELD_DAMAGE_BONUS);
        stats.getDamageToTargetHullMult().modifyPercent(id, HULL_DAMAGE_BONUS);

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

        //Damage
        tooltip.addSectionHeading("Damage Improvements", Alignment.MID, pad);
        tooltip.addPara("Against Shield: %s", pad, Misc.getPositiveHighlightColor(),
                String.format("%d%%", (int) SHIELD_DAMAGE_BONUS));
        tooltip.addPara("Against Hull: %s", pad, Misc.getPositiveHighlightColor(),
                String.format("%d%%", (int) HULL_DAMAGE_BONUS));

    }

    private static final Set<String> REQUIRED_HULLMODS = new HashSet<>(Set.of(
            "on_navalhull"
    ));

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(Set.of(
            "on_ashe1"
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
