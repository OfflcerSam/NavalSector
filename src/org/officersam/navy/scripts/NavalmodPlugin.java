package org.officersam.navy.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.officersam.navy.scripts.world.Naval_gen;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.TextureData;
import exerelin.campaign.SectorManager;

public class NavalmodPlugin extends BaseModPlugin {

    @Override
    public void onNewGame() {
	boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
	if (!haveNexerelin || SectorManager.getManager().isCorvusMode()){
            new Naval_gen().generate(Global.getSector());
        }
    }

    private static Logger log = Global.getLogger(NavalmodPlugin.class);
    public static boolean hasGraphicsLib = false;
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
        hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");
        // Test that the .jar is loaded and working, using the most obnoxious way possible.
        //throw new RuntimeException("Template mod loaded! Remove this crash in TemplateModPlugin.");
        if (hasGraphicsLib) {
            //TextureData.readTextureDataCSV("data/lights/on_texture_data.csv");
            LightData.readLightDataCSV("data/lights/on_light_data.csv");
        }
    }
 
}
	

