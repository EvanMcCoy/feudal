package com.qwertyness.feudal.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Church implements CivilOrganizer {
	//Display as bishop in fiefs
	public UUID pope;
	public List<UUID> abbots;
	public Bank bank;
	
	private ConfigurationSection dataPath;
	
	public Church(UUID pope, List<UUID> abbots, Bank bank, ConfigurationSection section) {
		this.pope = pope;
		this.abbots = abbots;
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
