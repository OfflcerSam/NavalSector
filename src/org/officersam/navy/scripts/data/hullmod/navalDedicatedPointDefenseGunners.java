package org.officersam.navy.scripts.data.hullmod;

import java.util.Iterator;
import java.util.List;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class navalDedicatedPointDefenseGunners extends BaseHullMod {

	public static float DAMAGE_BONUS = 40f;
	public static float CREW_PERCENT = 15f;
	public static float CREW_PERCENT_SMOD = 5f;

	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

		stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 0.2f);
		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		stats.getMinCrewMod().modifyPercent(id,CREW_PERCENT);

		boolean sMod = isSMod(stats);
		if (sMod) {
			stats.getMinCrewMod().modifyPercent(id,CREW_PERCENT_SMOD);
			stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 0.65f);
		}

	}

	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		boolean sMod = isSMod(ship);
		
		if (sMod) {
			List weapons = ship.getAllWeapons();
			Iterator iter = weapons.iterator();
			while (iter.hasNext()) {
				WeaponAPI weapon = (WeaponAPI)iter.next();

				boolean sizeMatches = weapon.getSize() == WeaponSize.SMALL;

				if (sizeMatches && weapon.getType() != WeaponType.MISSILE && !weapon.hasAIHint(AIHints.STRIKE)) {
					weapon.setPD(true);
				}
			}
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(DAMAGE_BONUS) + "%";
		if (index == 1) return "" + (int)Math.round(CREW_PERCENT) + "%";
		return null;
	}

	public String getSModDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(CREW_PERCENT_SMOD) + "%";
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().hasHullMod("pointdefenseai")) return false;
		return ship != null;
	}

	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().hasHullMod("pointdefenseai")) {
			return "Incompatible with Integrated Point Defense AI.";
		}
		return "Incompatible";
	}

}




