package org.officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.magiclib.util.MagicIncompatibleHullmods;

public class navalThinArmor extends BaseHullMod {

    public static final float HULL_BONUS = -20f;
    public static final float ARMOR_BONUS = -40f;

    public static final float MIN_CREW_BONUS = -5f;
    public static final float MAX_CREW_BONUS = 15f;

    public static final float MAX_SPEED_BONUS = 40f;
    public static final float ACCEL_BONUS = 20f;
    public static final float DECEL_BONUS = 10f;

    public static final float ENGINE_HP_BONUS = -15f;
    public static final float WEAPON_HP_BONUS = -20f;

    public static final float CARGO_BONUS = 10f;
    public static final float FUEL_BONUS = 10f;
    public static final float FUEL_USAGE_BONUS = -15f;

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

        stats.getHullBonus().modifyPercent(id,HULL_BONUS);
        stats.getArmorBonus().modifyPercent(id,ARMOR_BONUS);

        stats.getMinCrewMod().modifyPercent(id,MIN_CREW_BONUS);
        stats.getMaxCrewMod().modifyPercent(id,MAX_CREW_BONUS);

        stats.getMaxSpeed().modifyPercent(id,MAX_SPEED_BONUS);
        stats.getAcceleration().modifyPercent(id,ACCEL_BONUS);
        stats.getDeceleration().modifyPercent(id,DECEL_BONUS);

        stats.getMaxTurnRate().modifyPercent(id,MAX_SPEED_BONUS);
        stats.getTurnAcceleration().modifyPercent(id,ACCEL_BONUS);

        stats.getEngineHealthBonus().modifyPercent(id,ENGINE_HP_BONUS);
        stats.getWeaponHealthBonus().modifyPercent(id,WEAPON_HP_BONUS);

        stats.getCargoMod().modifyPercent(id,CARGO_BONUS);
        stats.getSuppliesPerMonth().modifyPercent(id,FUEL_USAGE_BONUS);
        stats.getSuppliesToRecover().modifyPercent(id,FUEL_USAGE_BONUS);
        stats.getFuelMod().modifyPercent(id,FUEL_BONUS);
        stats.getFuelUseMod().modifyPercent(id,FUEL_USAGE_BONUS);

        MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), "on_noarmor", "on_thinarmor");
        MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), "on_defaultarmor", "on_thinarmor");
        MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), "on_thickarmor", "on_thinarmor");
        MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), "on_thickestarmor", "on_thinarmor");

    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + (int) HULL_BONUS + "%";
        if (index == 1) return "" + (int) ARMOR_BONUS + "%";

        if (index == 2) return "" + (int) MIN_CREW_BONUS + "%";
        if (index == 3) return "" + (int) MAX_CREW_BONUS + "%";

        if (index == 4) return "" + (int) MAX_SPEED_BONUS + "%";
        if (index == 5) return "" + (int) ACCEL_BONUS + "%";
        if (index == 6) return "" + (int) DECEL_BONUS + "%";

        if (index == 7) return "" + (int) ENGINE_HP_BONUS + "%";
        if (index == 8) return "" + (int) WEAPON_HP_BONUS + "%";

        if (index == 9) return "" + (int) CARGO_BONUS + "%";
        if (index == 10) return "" + (int) FUEL_USAGE_BONUS + "%";
        if (index == 11) return "" + (int) FUEL_BONUS + "%";
        if (index == 12) return "" + (int) FUEL_USAGE_BONUS + "%";
        return null;
    }

    public boolean isApplicableToShip(ShipAPI ship) {
        if (ship.getVariant().hasHullMod("on_navalhull")) return true;
        if (ship.getVariant().hasHullMod("on_noarmor")) return false;
        if (ship.getVariant().hasHullMod("on_defaultarmor")) return false;
        if (ship.getVariant().hasHullMod("on_thickarmor")) return false;
        if (ship.getVariant().hasHullMod("on_thickestarmor")) return false;
        return ship != null;
    }

    public String getUnapplicableReason(ShipAPI ship) {
        if (ship.getVariant().hasHullMod("on_noarmor")) {
            return "Incompatible with No Armor Layout.";
        }
        if (ship.getVariant().hasHullMod("on_defaultarmor")) {
            return "Incompatible with Default Armor Layout.";
        }
        if (ship.getVariant().hasHullMod("on_thickarmor")) {
            return "Incompatible with Thick Armor Layout.";
        }
        if (ship.getVariant().hasHullMod("on_thickestarmor")) {
            return "Incompatible with Thickest Armor Layout.";
        }
        return "Incompatible";
    }
}
