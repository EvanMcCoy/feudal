package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.settings.GovernmentSettings;
import com.qwertyness.feudal.util.Util;

public class Fief implements Government {
	private String name;
	private UUID baron;
	private UUID baroness;
	private List<UUID> peasents;
	private List<UUID> serfs;
	private Bank bank;
	private Army army;
	private Church church;
	
	private Chunk capital;
	private List<Land> land;
	
	private ConfigurationSection dataPath;
	public GovernmentSettings settings;
	
	public Fief(String name, ConfigurationSection section) {
		this.name = name;
		this.dataPath = section;
		this.peasents = new ArrayList<UUID>();
		this.serfs = new ArrayList<UUID>();
		this.land = new ArrayList<Land>();
		
		ConfigurationSection bankSection = section.getConfigurationSection("bank");
		if (bankSection == null) {
			bankSection = section.createSection("bank");
		}
		this.bank = new Bank(bankSection);

		ConfigurationSection churchSection = section.getConfigurationSection("church");
		if (churchSection == null) {
			churchSection = section.createSection("church");
		}
		this.church = new Church(churchSection);
		
		ConfigurationSection armySection = section.getConfigurationSection("army");
		if (armySection == null) {
			armySection = section.createSection("army");
		}
		this.army = new Army(armySection);
	}
	
	public Fief(String name, UUID baron, UUID baroness, List<UUID> peasents, List<UUID> serfs, Bank bank, Army army, Church church, Chunk capital, ConfigurationSection section) {
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
		if (this.bank == null) {
			this.bank = new Bank(section.createSection("bank"));
		}
		this.army = army;
		if (this.army == null) {
			this.army = new Army(section.createSection("army"));
		}
		this.church = church;
		if (this.church == null) {
			this.church = new Church(section.createSection("church"));
		}
		this.capital = capital;
		this.land = new ArrayList<Land>();
		
		this.dataPath = section;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getBaron() {return this.baron;}
	public void setBaron(UUID baron) {this.baron = baron;}
	
	public UUID getBaroness() {return this.baroness;}
	public void setBaroness(UUID baroness) {this.baroness = baroness;}
	
	public List<UUID> getPeasents() {return this.peasents;}
	public void setPeasents(List<UUID> peasents) {this.peasents = peasents;}
	public void addPeasent(UUID peasent) {this.peasents.add(peasent);}
	public void removePeasent(int peasent) {this.peasents.remove(peasent);}
	public boolean isPeasent(UUID peasent) {
		if (Util.toStringList(this.peasents).contains(peasent.toString())) {
			return true;
		}
		return false;
	}
	
	public List<UUID> getSerfs() {return this.serfs;}
	public void setSerfs(List<UUID> serfs) {this.serfs = serfs;}
	public void addSerf(UUID serf) {this.serfs.add(serf);}
	public void removeSerf(int serf) {this.serfs.remove(serf);}
	public boolean isSerf(UUID serf) {
		if (Util.toStringList(this.serfs).contains(serf.toString())) {
			return true;
		}
		return false;
	}
	
	public Chunk getCapital() {return this.capital;}
	public void setCapital(Chunk capital) {this.capital = capital;}
	
	public List<Land> getLand() {return this.land;}
	public void addLand(Land land) {this.land.add(land);}
	public void removeLand(Land land) {
		for (Land currentLand : this.land) {
			if (land.getCoordinates().equals(currentLand.getCoordinates())) {
				this.land.remove(this.land.indexOf(currentLand));
			}
		}
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
