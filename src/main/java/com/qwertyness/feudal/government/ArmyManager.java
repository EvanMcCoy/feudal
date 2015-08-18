package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.npc.FeudalNPC;
import com.qwertyness.feudal.npc.NPCProfile;
import com.qwertyness.feudal.util.LandUtil;
import com.qwertyness.feudal.util.Util;

public class ArmyManager {
	private List<Attack> attacks;
	
	public ArmyManager() {
		this.attacks = new ArrayList<Attack>();
	}
	
	public List<Attack> getAttacks() {
		return this.attacks;
	}
	public Attack[] getAttacks(Chunk chunk) {
		return (Attack[]) this.attacks.stream().filter(a -> a.compareDisputedLand(Feudal.getInstance().getLandManager().getLand(LandUtil.toString(chunk)))).toArray();
	}
	public void addAttack(Attack attack) {
		this.attacks.add(attack);
	}
	public void removeAttack(Attack attack) {
		for (Attack testAttack : new ArrayList<Attack>(this.attacks)) {
			if (LandUtil.toString(testAttack.getDisputedLand().getChunk()).equals(LandUtil.toString(attack.getDisputedLand().getChunk()))) {
				this.attacks.remove(testAttack);
			}
		}
	}
	
	public Army loadArmy(ConfigurationSection government) {
		ConfigurationSection section = government.getConfigurationSection("army");
		
		UUID knight = (section.getString("knight") != null) ? UUID.fromString(section.getString("knight")) : null;
		UUID dame = (section.getString("dame") != null) ? UUID.fromString(section.getString("dame")) : null;
		List<UUID> soldiers = Util.toUUIDList(section.getStringList("soldiers"));
		Bank bank = Feudal.getInstance().getBankManager().loadBank(section);
		ConfigurationSection npcSection = section.getConfigurationSection("npcs");
		List<FeudalNPC> npcs = new ArrayList<FeudalNPC>();
		if (npcSection != null) {
			for (String uuid : npcSection.getKeys(false)) {
				npcs.add(new FeudalNPC(UUID.fromString(uuid), NPCProfile.getProfile(npcSection.getString(uuid))));
			}
		}
		return new Army(knight, dame, soldiers, npcs, bank, section);
	}
	
	public void saveArmy(Army army) {
		ConfigurationSection section = army.getDataPath();
		
		section.set("knight", (army.getKnight() == null) ? null : army.getKnight().toString());
		section.set("dame", (army.getDame() == null) ? null : army.getDame().toString());
		section.set("soldiers", Util.toStringList(army.getSoldiers()));
		Feudal.getInstance().getBankManager().saveBank(army.getBank());
		
		for (FeudalNPC npc : army.getNPCs()) {
			section.set("npcs." + npc.getUUID().toString(), npc.getProfile().profileName);
		}
	}
}
