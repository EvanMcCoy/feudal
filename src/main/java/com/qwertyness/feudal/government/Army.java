package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.npc.FeudalNPC;
import com.qwertyness.feudal.util.Util;

public class Army implements CivilOrganizer {
	private UUID knight;
	private UUID dame;
	private List<UUID> soldiers;
	private List<FeudalNPC> npcs;
	private Bank bank;
	
	private ConfigurationSection dataPath;
	
	public Army(ConfigurationSection section) {
		this.dataPath = section;
		
		ConfigurationSection bankSection = section.getConfigurationSection("bank");
		if (bankSection == null) {
			bankSection = section.createSection("bank");
		}
		this.bank = new Bank(bankSection);
		
		this.soldiers = new ArrayList<UUID>();
		this.npcs = new ArrayList<FeudalNPC>();
	}
	
	public Army(UUID knight, UUID dame, List<UUID> soldiers, List<FeudalNPC> npcs, Bank bank, ConfigurationSection section) {
		this.knight = knight;
		this.dame = dame;
		this.soldiers = soldiers;
		this.npcs = npcs;
		this.bank = bank;
		
		this.dataPath = section;
	}
	
	public UUID getKnight() {return this.knight;}
	public void setKnight(UUID knight) {
		this.knight = knight;
	}
	
	public UUID getDame() {return this.dame;}
	public void setDame(UUID dame) {
		this.dame = dame;
	}
	
	public List<UUID> getSoldiers() {return this.soldiers;}
	public void addSoldier(UUID soldier) {
		this.soldiers.add(soldier);
	}
	public void removeSoldier(int soldier) {
		this.soldiers.remove(soldier);
	}
	public boolean isSoldier(UUID soldier) {
		if (Util.toStringList(this.getSoldiers()).contains(soldier.toString())) {
			return true;
		}
		return false;
	}
	
	public List<FeudalNPC> getNPCs() {return this.npcs;}
	public void addNPC(FeudalNPC npc) {
		this.npcs.add(npc);
	}
	public void removeNPC(int npc) {
		this.npcs.remove(npc);
	}
	public boolean isNPC(UUID npc) {
		for (FeudalNPC fNPC : this.getNPCs()) {
			if (fNPC.getUUID().compareTo(npc) == 0) {
				return true;
			}
		}
		return false;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.bank;
	}
}
