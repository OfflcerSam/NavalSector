package officersam.navy.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import officersam.navy.scripts.world.Naval_gen;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
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

    public static boolean hasGraphicsLib;

    @Override
    public void onApplicationLoad() {
        hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");

        if (hasGraphicsLib) {
            ShaderLib.init();
            LightData.readLightDataCSV("data/lights/on_light_data.csv");
            TextureData.readTextureDataCSV("data/lights/on_texture_data.csv");
        }
    }
}


