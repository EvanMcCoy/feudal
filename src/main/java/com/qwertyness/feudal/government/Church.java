package com.qwertyness.feudal.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Church implements CivilOrganizer {
	//Display as bishop in fiefs
	public UUID pope;
	public List<UUID> abbots;
	
	private ConfigurationSection dataPath;
	
	public Church(UUID pope, List<UUID> abbots, ConfigurationSection section) {
		this.pope = pope;
		this.abbots = abbots;
		
		this.dataPath = section;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
}
