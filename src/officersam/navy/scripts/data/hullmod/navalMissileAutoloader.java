package officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;

import java.util.HashSet;
import java.util.Set;

public class navalMissileAutoloader extends BaseHullMod {

    public static final float CARGO_BONUS = -25f;
    public static String MR_DATA_KEY = "core_reload_data_key";

    public static class PeriodicMissileReloadData {
        IntervalUtil interval = new IntervalUtil(15f, 30f);
    }

    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        for (String blocked : BLOCKED_HULLMODS) {
            stats.getVariant().removeMod(blocked);
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        super.advanceInCombat(ship, amount);

        if (!ship.isAlive()) return;

        CombatEngineAPI engine = Global.getCombatEngine();

        String key = MR_DATA_KEY + "_" + ship.getId();
        PeriodicMissileReloadData data = (PeriodicMissileReloadData) engine.getCustomData().get(key);
        if (data == null) {
            data = new PeriodicMissileReloadData();
            engine.getCustomData().put(key, data);
        }

        data.interval.advance(amount);
        if (data.interval.intervalElapsed()) {
            for (WeaponAPI w : ship.getAllWeapons()) {
                if (w.getType() !=WeaponType.MISSILE) continue;

                if (w.usesAmmo() && w.getAmmo() < w.getMaxAmmo()) {
                    w.setAmmo(w.getMaxAmmo());
                }
            }
        }

    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;

        //Logic
        tooltip.addSectionHeading("Crew & Logistics", Alignment.MID, pad);
        tooltip.addPara("Cargo Capacity: %s", pad, Misc.getNegativeHighlightColor(),
                String.format("%d%%", (int) CARGO_BONUS));
    }

    private static final Set<String> REQUIRED_HULLMODS = new HashSet<>(Set.of(
            "on_navalhull"
    ));

    private static final Set<String> BLOCKED_HULLMODS = new HashSet<>(Set.of(

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
