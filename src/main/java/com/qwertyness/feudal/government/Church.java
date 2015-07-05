package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.util.Util;

public class Church implements CivilOrganizer {
	//Display as bishop in fiefs
	private UUID pope;
	private List<UUID> abbots;
	private Bank bank;
	
	private ConfigurationSection dataPath;
	
	public Church(ConfigurationSection section) {
		this.dataPath = section;
		
		ConfigurationSection bankSection = section.getConfigurationSection("bank");
		if (bankSection == null) {
			bankSection = section.createSection("bank");
		}
		this.bank = new Bank(bankSection);
		
		this.abbots = new ArrayList<UUID>();
	}
	
	public Church(UUID pope, List<UUID> abbots, Bank bank, ConfigurationSection section) {
		this.pope = pope;
		this.abbots = abbots;
		this.bank = bank;
		
		this.dataPath = section;
	}
	
	public UUID getPope() {return pope;}
	public void setPope(UUID pope) {
		this.pope = pope;
	}
	
	public List<UUID> getAbbots() {return this.abbots;}
	public void addAbbot(UUID abbot) {
		this.abbots.add(abbot);
	}
	public void removeAbbot(int abbot) {
		this.abbots.remove(abbot);
	}
	public boolean isAbbot(UUID abbot) {
		if (Util.toStringList(this.getAbbots()).contains(abbot.toString())) {
			return true;
		}
		return false;
	}

	public ConfigurationSection getDataPath() {
		return this.dataPath;
	}
	
	public Bank getBank() {
		return this.bank;
	}

	
}
