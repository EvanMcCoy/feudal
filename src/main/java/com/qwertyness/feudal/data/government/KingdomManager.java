package com.qwertyness.feudal.data.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.government.Church;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.util.Util;

public class KingdomManager {
	private Feudal plugin;

	public List<Kingdom> kingdoms;
	
	public KingdomManager() {
		this.plugin = Feudal.getInstance();
		this.kingdoms = new ArrayList<Kingdom>();
		for (String string : this.plugin.kingdomData.get().getKeys(false)) {
			this.registerKingdom(this.loadKingdom(string));
		}
	}
	
	public Kingdom getKingdom(String kingdomName) {
		for (Kingdom kingdom : this.kingdoms) {
			if (kingdom.name.equals(kingdomName)) {
				return kingdom;
			}
		}
		return null;
	}
	
	public boolean isKingdom(String kingdomName) {
		if (getKingdom(kingdomName) != null) {
			return true;
		}
		return false;
	}
	
	public void registerKingdom(Kingdom kingdom) {
		if (!isKingdom(kingdom.name)) {
			this.kingdoms.add(kingdom);
		}
	}
	
	public void unregisterKingdom(String kingdomName) {
		if (!isKingdom(kingdomName)) {
			return;
		}
		this.kingdoms.remove(getKingdom(kingdomName));
	}
	
	public Kingdom loadKingdom(String kingdomName) {
		ConfigurationSection kingdomSection = this.plugin.kingdomData.get().getConfigurationSection(kingdomName);
		
		String name = kingdomSection.getName();
		UUID king = (kingdomSection.getString("king") != null) ? UUID.fromString(kingdomSection.getString("king")) : null;
		UUID queen = (kingdomSection.getString("queen") != null) ? UUID.fromString(kingdomSection.getString("queen")) : null;
		UUID prince = (kingdomSection.getString("prince") != null) ? UUID.fromString(kingdomSection.getString("prince")) : null;
		UUID princess = (kingdomSection.getString("princess") != null) ? UUID.fromString(kingdomSection.getString("princess")) : null;
		UUID duke = (kingdomSection.getString("duke") != null) ? UUID.fromString(kingdomSection.getString("duke")) : null;
		UUID duchess = (kingdomSection.getString("duchess") != null) ? UUID.fromString(kingdomSection.getString("duchess")) : null;
		List<UUID> earls = Util.toUUIDList(kingdomSection.getStringList("earls"));
		Bank bank = this.plugin.bankManager.loadBank(kingdomSection);
		Army army = this.plugin.armyManager.loadArmy(kingdomSection);
		Church church = this.plugin.churchManager.loadChurch(kingdomSection);
		List<Fief> fiefs = new ArrayList<Fief>();
		for (String string : kingdomSection.getStringList("fiefs")) {
			this.plugin.fiefManager.loadFief(kingdomSection.getName(), string);
		}
		
		Chunk capital = Util.toChunk(kingdomSection.getString("captial"));
		
		return new Kingdom(name, king, queen, prince, princess, duke, duchess, earls, bank, army, church, fiefs, capital, kingdomSection);
	}
	
	public void saveKingdom(Kingdom kingdom) {
		//TODO: Add in name change system
		ConfigurationSection kingdomSection = kingdom.getDataPath();
		
		kingdomSection.set("king", (kingdom.king == null) ? null : kingdom.king.toString());
		kingdomSection.set("queen", (kingdom.queen == null) ? null : kingdom.queen.toString());
		kingdomSection.set("prince", (kingdom.prince == null) ? null : kingdom.prince.toString());
		kingdomSection.set("princess", (kingdom.princess == null) ? null : kingdom.princess.toString());
		kingdomSection.set("duke", (kingdom.duke == null) ? null : kingdom.duke.toString());
		kingdomSection.set("dutchess", (kingdom.duchess == null) ? null : kingdom.duchess.toString());
		kingdomSection.set("earls", Util.toStringList(kingdom.earls));
		this.plugin.bankManager.saveBank(kingdom);
		this.plugin.armyManager.saveArmy(kingdom.royalArmy);
		this.plugin.churchManager.saveChurch(kingdom.highChurch);
		kingdom.flag.setFlag(kingdomSection.getConfigurationSection("flag"));
		List<String> fiefs = new ArrayList<String>();
		for (Fief fief : kingdom.fiefs) {
			this.plugin.fiefManager.saveFief(fief);
			fiefs.add(fief.name);
		}
		kingdomSection.set("fiefs", fiefs);
		kingdomSection.set("capital", Util.toString(kingdom.capital));
	}
	
	public void deleteKingdom(Kingdom kingdom) {
		this.plugin.kingdomData.get().set(kingdom.getDataPath().getCurrentPath(), null);
		this.unregisterKingdom(kingdom.getName());
	}
	
	public void saveAll() {
		for (Kingdom kingdom : this.kingdoms) {
			this.saveKingdom(kingdom);
		}
	}
	
	public Kingdom getLandOwner(Chunk chunk) {
		return this.plugin.landManager.getLand(Util.toString(chunk)).kingdom;
	}
}
