package com.qwertyness.feudal.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Army implements CivilOrganizer {
	public UUID knight;
	public UUID dame;
	public List<UUID> soldiers;
	
	public Army(UUID knight, UUID dame, List<UUID> soldiers) {
		this.knight = knight;
		this.dame = dame;
		this.soldiers = soldiers;
	}

	public ConfigurationSection getDataPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
