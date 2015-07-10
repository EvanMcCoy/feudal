package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.government.settings.Settings;
import com.qwertyness.feudal.util.Util;

public class Kingdom implements Government {
	private String name;
	private ConfigurationSection dataPath;
	private Settings settings;
	
	private UUID king;
	private UUID queen;
	private UUID prince;
	private UUID princess;
	private UUID duke;
	private UUID duchess;
	private List<UUID> earls;
	private Bank imperialVault;
	private Army royalArmy;
	private Church highChurch;
	private List<Fief> fiefs;
	
	private Flag flag;
	
	private Chunk capital;
	private List<Land> land;
	
	//Initialize new kingdom
	public Kingdom(String name, ConfigurationSection section) {
		this.name = name;
		this.dataPath = section;
		this.earls = new ArrayList<UUID>();
		this.fiefs = new ArrayList<Fief>();
		this.land = new ArrayList<Land>();
		
		ConfigurationSection bankSection = section.getConfigurationSection("bank");
		if (bankSection == null) {
			bankSection = section.createSection("bank");
		}
		this.imperialVault = new Bank(bankSection);
		
		ConfigurationSection churchSection = section.getConfigurationSection("church");
		if (churchSection == null) {
			churchSection = section.createSection("church");
		}
		this.highChurch = new Church(churchSection);
		
		ConfigurationSection armySection = section.getConfigurationSection("army");
		if (armySection == null) {
			armySection = section.createSection("army");
		}
		this.royalArmy = new Army(armySection);
		
		ConfigurationSection flagSection = section.getConfigurationSection("flag");
		if (flagSection == null) {
			flagSection = section.createSection("flag");
		}
		this.flag = new Flag(flagSection);
		
		ConfigurationSection settingsSection = section.getConfigurationSection("settings");
		if (settingsSection == null) {
			settingsSection = section.createSection("settings");
		}
		this.settings = new Settings(settingsSection);
	}
	
	//Initialize current kingdom
	public Kingdom(String name, UUID king, UUID queen, UUID prince, UUID princess, UUID duke, UUID duchess, List<UUID> earls, Bank bank, Army army, Church church, List<Fief> fiefs, Chunk capital, Flag flag, Settings settings, ConfigurationSection section) {
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
			this.imperialVault = new Bank(section.createSection("bank"));
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
		
		this.flag = new Flag(section.getConfigurationSection("flag"));
		this.settings = settings;
		this.dataPath = section;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getKing() {return this.king;}
	public void setKing(UUID king) {this.king = king;}
	
	public UUID getQueen() {return this.queen;}
	public void setQueen(UUID queen) {this.queen = queen;}
	
	public UUID getPrince() {return this.prince;}
	public void setPrince(UUID prince) {this.prince = prince;}
	
	public UUID getPrincess() {return this.princess;}
	public void setPrincess(UUID princess) {this.princess = princess;}
	
	public UUID getDuke() {return this.duke;}
	public void setDuke(UUID duke) {this.duke = duke;}
	
	public UUID getDuchess() {return this.duchess;}
	public void setDuchess(UUID duchess) {this.duchess = duchess;}
	
	public List<UUID> getEarls() {return this.earls;}
	public void setEarls(List<UUID> earls) {this.earls = earls;}
	public void addEarl(UUID earl) {this.earls.add(earl);}
	public void removeEarl(int earl) {this.earls.remove(earl);}
	public boolean isEarl(UUID earl) {
		if (Util.toStringList(this.getEarls()).contains(earl.toString())) {
			return true;
		}
		return false;
	}

	public Church getChurch() {return this.highChurch;}

	public Army getArmy() {return this.royalArmy;}
	
	public Bank getBank() {return this.imperialVault;}
	
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
	
	public List<Land> getFortresses() {
		List<Land> fortresses = new ArrayList<Land>();
		for (Land land : this.land) {
			if (land.isFortress()) {
				fortresses.add(land);
			}
		}
		return fortresses;
	}
	
	public List<Fief> getFiefs() {return this.fiefs;}
	public void addFief(Fief fief) {this.fiefs.add(fief);}
	public void removeFief(Fief fief) {
		for (Fief currentFief : this.fiefs) {
			if (currentFief.getName().equals(fief.getName())) {
				this.fiefs.remove(this.fiefs.indexOf(currentFief));
			}
		}
	}
	
	public Flag getFlag() {return this.flag;}
	public void setFlag(Flag flag) {this.flag = flag;}
	
	public Settings getSettings() {return this.settings;}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	
}
