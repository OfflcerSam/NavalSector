package org.officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class navalInsulatedHull extends BaseHullMod {
    public static float HULL_BONUS = 10f;
    public static float FLUX_RESISTANCE = 15f;

    public static float SMOD_HULL_BONUS = 20f;
    public static float SMOD_FLUX_RESISTANCE = 30f;

    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

        stats.getHullBonus().modifyPercent(id, HULL_BONUS);
        stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f);

        boolean sMod = isSMod(stats);
        if (sMod) {
            stats.getHullBonus().modifyPercent(id, SMOD_HULL_BONUS);
            stats.getEmpDamageTakenMult().modifyMult(id, 1f - SMOD_FLUX_RESISTANCE * 0.01f);
        }
    }

    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + (int) FLUX_RESISTANCE + "%";
        if (index == 1) return "" + (int) HULL_BONUS + "%";
        return null;
    }

    public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "" + (int) SMOD_FLUX_RESISTANCE + "%";
        if (index == 1) return "" + (int) SMOD_HULL_BONUS + "%";
        return null;
    }

}
