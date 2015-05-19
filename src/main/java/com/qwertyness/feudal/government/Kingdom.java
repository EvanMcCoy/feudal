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
	
	public Flag flag;
	
	public Chunk capital;
	public List<Land> land;
	
	public Kingdom(String name, ConfigurationSection section) {
		this.name = name;
		this.dataPath = section;
		this.earls = new ArrayList<UUID>();
		this.fiefs = new ArrayList<Fief>();
		this.land = new ArrayList<Land>();
		
		if (section.getConfigurationSection("bank") == null) {
			section.createSection("bank");
		}
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
	
	public Kingdom(String name, UUID king, UUID queen, UUID prince, UUID princess, UUID duke, UUID duchess, List<UUID> earls, Bank bank, Army army, Church church, List<Fief> fiefs, Chunk capital, ConfigurationSection section) {
		this.name = name;
		this.king = king;
		this.queen = queen;
		this.prince = prince;
		this.princess = princess;
		this.duke = duke;
		this.duchess = duchess;
		this.earls = earls;
		if (earls == null) {
			earls = new ArrayList<UUID>();
		}
		this.imperialVault = bank;
		if (this.imperialVault == null) {
			this.imperialVault = new Bank();
		}
		this.royalArmy = army;
		if (this.royalArmy == null) {
			ConfigurationSection armySection = section.createSection("army");
			this.royalArmy = new Army(armySection);
		}
		this.highChurch = church;
		if (this.highChurch == null) {
			ConfigurationSection churchSection = section.createSection("church");
			this.highChurch = new Church(churchSection);
		}
		this.fiefs = fiefs;
		if (this.fiefs == null) {
			this.fiefs = new ArrayList<Fief>();
		}
		this.capital = capital;
		this.land = new ArrayList<Land>();
		
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
	
	public List<Land> getFortresses() {
		List<Land> fortresses = new ArrayList<Land>();
		for (Land land : this.land) {
			if (land.isFortress()) {
				fortresses.add(land);
			}
		}
		return fortresses;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.imperialVault;
	}
}
