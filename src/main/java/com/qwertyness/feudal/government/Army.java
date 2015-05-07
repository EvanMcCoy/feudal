package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Army implements CivilOrganizer {
	public UUID knight;
	public UUID dame;
	public List<UUID> soldiers;
	public Bank bank;
	
	public ConfigurationSection dataPath;
	
	public Army(ConfigurationSection section) {
		this.dataPath = section;
		
		this.soldiers = new ArrayList<UUID>();
	}
	
	public Army(UUID knight, UUID dame, List<UUID> soldiers, Bank bank, ConfigurationSection section) {
		this.knight = knight;
		this.dame = dame;
		this.soldiers = soldiers;
		this.bank = bank;
		
		this.dataPath = section;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.bank;
	}
}
