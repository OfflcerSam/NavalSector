package org.officersam.navy.scripts.world;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import org.officersam.navy.scripts.world.systems.USNC_StarofHope;

@SuppressWarnings("unchecked")
public class Naval_gen implements SectorGeneratorPlugin {

    @Override
    public void generate(SectorAPI sector) {
	
        new USNC_StarofHope().generate(sector);

		
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("USNC");

        FactionAPI usnc = sector.getFaction("USNC");

        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT); 
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);   
        FactionAPI kol = sector.getFaction(Factions.KOL);	
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT); 
		FactionAPI persean = sector.getFaction(Factions.PERSEAN);
        FactionAPI guard = sector.getFaction(Factions.LIONS_GUARD);

        usnc.setRelationship(player.getId(), 0.1f);
        usnc.setRelationship(hegemony.getId(), 0.0f);
        usnc.setRelationship(tritachyon.getId(), 0.2f);
        usnc.setRelationship(pirates.getId(), -1.0f);
        usnc.setRelationship(independent.getId(), 0.6f);
        usnc.setRelationship(persean.getId(), 0.2f);
        usnc.setRelationship(church.getId(), -0.3f);
        usnc.setRelationship(path.getId(), -1.0f);
        usnc.setRelationship(kol.getId(), 0.1f);
        usnc.setRelationship(diktat.getId(), 0.1f);
        usnc.setRelationship(guard.getId(), -0.2f);

    }
}
