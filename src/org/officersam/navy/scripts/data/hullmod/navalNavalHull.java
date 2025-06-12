package org.officersam.navy.scripts.data.hullmod;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;

import java.util.HashMap;
import java.util.Map;

public class navalNavalHull extends BaseHullMod {

    private final Map<Integer, String> SWITCH_HULLSPECS = new HashMap<>();

    {
        SWITCH_HULLSPECS.put(0, "on_noarmor");
        SWITCH_HULLSPECS.put(1, "on_thinarmor");
        SWITCH_HULLSPECS.put(2, "on_defaultarmor");
        SWITCH_HULLSPECS.put(3, "on_thickarmor");
        SWITCH_HULLSPECS.put(4, "on_thickestarmor");
    }


    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {

    }


}
