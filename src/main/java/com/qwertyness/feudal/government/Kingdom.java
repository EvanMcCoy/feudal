package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.settings.GovernmentSettings;

public class Kingdom implements Government {
	public String name;
	private ConfigurationSection dataPath;
	public GovernmentSettings settings;
	
	public UUID king;
	public UUID queen;
	public UUID prince;
	public UUID princess;
	public UUID duke;
	public UUID duchess;
	public List<UUID> earls;
	public Bank imperialVault;
	public Army royalArmy;
	public Church highChurch;
	public List<Fief> fiefs;
	
	public Chunk capital;
	public List<Chunk> fortresses;
	public List<Chunk> land;
	
	public Kingdom(String name, ConfigurationSection section) {
		this.name = name;
		this.dataPath = section;
		this.earls = new ArrayList<UUID>();
		this.fiefs = new ArrayList<Fief>();
		this.fortresses = new ArrayList<Chunk>();
		this.land = new ArrayList<Chunk>();
		
		this.imperialVault = new Bank();
		if (section.getConfigurationSection("church") == null) {
			section.createSection("church");
		}
		this.highChurch = new Church(section.getConfigurationSection("church"));
		if (section.getConfigurationSection("army") == null) {
			section.createSection("army");
		}
		this.royalArmy = new Army(section.getConfigurationSection("army"));
	}
	
	public Kingdom(String name, UUID king, UUID queen, UUID prince, UUID princess, UUID duke, UUID duchess, List<UUID> earls, Bank bank, Army army, Church church, List<Fief> fiefs, Chunk capital, List<Chunk> fortresses, List<Chunk> land, ConfigurationSection section) {
		this.name = name;
		this.king = king;
		this.queen = queen;
		this.prince = prince;
		this.princess = princess;
		this.duke = duke;
		this.duchess = duchess;
		this.earls = earls;
		this.imperialVault = bank;
		this.royalArmy = army;
		this.highChurch = church;
		this.fiefs = fiefs;
		this.capital = capital;
		this.fortresses = fortresses;
		this.land = land;
		
		this.dataPath = section;
	}
	
	public String getName() {
		return this.name;
	}

	public Church getChurch() {
		return this.highChurch;
	}

	public Army getArmy() {
		return this.royalArmy;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.imperialVault;
	}
}
