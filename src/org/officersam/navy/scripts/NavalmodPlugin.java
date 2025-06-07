package org.officersam.navy.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.officersam.navy.scripts.world.Naval_gen;
import exerelin.campaign.SectorManager;

//public static boolean hasGraphicsLib;

public class NavalmodPlugin extends BaseModPlugin {

    @Override
    public void onNewGame() {
	boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
	if (!haveNexerelin || SectorManager.getManager().isCorvusMode()){
            new Naval_gen().generate(Global.getSector());
        }
    }
 
}
	

