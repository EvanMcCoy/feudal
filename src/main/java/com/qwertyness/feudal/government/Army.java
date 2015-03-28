package com.qwertyness.feudal.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

public class Army implements CivilOrganizer {
	public UUID knight;
	public UUID dame;
	public List<UUID> soldiers;
	public Bank bank;
	
	public Army(UUID knight, UUID dame, List<UUID> soldiers, Bank bank) {
		this.knight = knight;
		this.dame = dame;
		this.soldiers = soldiers;
		this.bank = bank;
	}

	public ConfigurationSection getDataPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Bank getBank() {
		return this.bank;
	}
}
