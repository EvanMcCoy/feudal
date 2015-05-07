package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.settings.GovernmentSettings;

public class Fief implements Government {
	public String name;
	public UUID baron;
	public UUID baroness;
	public List<UUID> peasents;
	public List<UUID> serfs;
	public Bank bank;
	public Army army;
	public Church church;
	
	public Chunk capital;
	public List<Chunk> land;
	
	private ConfigurationSection dataPath;
	public GovernmentSettings settings;
	
	public Fief(String name, ConfigurationSection section) {
		this.name = name;
		this.dataPath = section;
		this.peasents = new ArrayList<UUID>();
		this.serfs = new ArrayList<UUID>();
		this.land = new ArrayList<Chunk>();
		
		this.bank = new Bank();
		this.army = new Army(section.getConfigurationSection("army"));
		this.church = new Church(section.getConfigurationSection("church"));
	}
	
	public Fief(String name, UUID baron, UUID baroness, List<UUID> peasents, List<UUID> serfs, Bank bank, Army army, Church church, Chunk capital, List<Chunk> land, ConfigurationSection section) {
		this.name = name;
		this.baron = baron;
		this.baroness = baroness;
		this.peasents = peasents;
		if (this.peasents == null) {
			this.peasents = new ArrayList<UUID>();
		}
		this.serfs = serfs;
		if (this.serfs == null) {
			this.serfs = new ArrayList<UUID>();
		}
		this.bank = bank;
		this.army = army;
		this.church = church;
		this.capital = capital;
		this.land = land;
		
		this.dataPath = section;
	}
	
	public String getName() {
		return this.name;
	}

	public Church getChurch() {
		return this.church;
	}

	public Army getArmy() {
		return this.army;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.bank;
	}
}
