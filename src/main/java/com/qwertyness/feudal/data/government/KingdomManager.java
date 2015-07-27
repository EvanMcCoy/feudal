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
import com.qwertyness.feudal.government.Flag;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.settings.Settings;
import com.qwertyness.feudal.util.LandUtil;
import com.qwertyness.feudal.util.Util;

public class KingdomManager {
	private Feudal plugin;

	public List<Kingdom> kingdoms;
	
	public KingdomManager() {
		this.plugin = Feudal.getInstance();
		this.kingdoms = new ArrayList<Kingdom>();
		this.plugin.getKingdomData().get().getKeys(false).forEach((String k) -> this.registerKingdom(this.loadKingdom(k)));
	}
	
	public Kingdom getKingdom(String kingdomName) {
		return this.kingdoms.stream().filter((Kingdom kingdom) -> kingdom.getName().equals(kingdomName)).findFirst().orElse(null);
	}
	
	public boolean isKingdom(String kingdomName) {
		return getKingdom(kingdomName) != null;
	}
	
	public void registerKingdom(Kingdom kingdom) {
		if (!isKingdom(kingdom.getName())) {
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
		ConfigurationSection kingdomSection = this.plugin.getKingdomData().get().getConfigurationSection(kingdomName);
		
		String name = kingdomSection.getName();
		
		UUID king = (kingdomSection.getString("king") != null) ? UUID.fromString(kingdomSection.getString("king")) : null;
		UUID queen = (kingdomSection.getString("queen") != null) ? UUID.fromString(kingdomSection.getString("queen")) : null;
		UUID prince = (kingdomSection.getString("prince") != null) ? UUID.fromString(kingdomSection.getString("prince")) : null;
		UUID princess = (kingdomSection.getString("princess") != null) ? UUID.fromString(kingdomSection.getString("princess")) : null;
		UUID duke = (kingdomSection.getString("duke") != null) ? UUID.fromString(kingdomSection.getString("duke")) : null;
		UUID duchess = (kingdomSection.getString("duchess") != null) ? UUID.fromString(kingdomSection.getString("duchess")) : null;
		List<UUID> earls = Util.toUUIDList(kingdomSection.getStringList("earls"));
		
		Bank bank = this.plugin.getBankManager().loadBank(kingdomSection);
		Army army = this.plugin.getArmyManager().loadArmy(kingdomSection);
		Church church = this.plugin.getChurchManager().loadChurch(kingdomSection);
		List<Fief> fiefs = new ArrayList<Fief>();
		kingdomSection.getStringList("fiefs").forEach((String f) -> fiefs.add(this.plugin.getFiefManager().loadFief(kingdomSection.getName(), f)));
		Flag flag = new Flag(kingdomSection.getConfigurationSection("flag"));
		Settings settings = new Settings(kingdomSection.getConfigurationSection("settings"));
		
		Chunk capital = Util.toChunk(kingdomSection.getString("capital"));
		
		return new Kingdom(name, king, queen, prince, princess, duke, duchess, earls, bank, army, church, fiefs, capital, flag, settings, kingdomSection);
	}
	
	public void saveKingdom(Kingdom kingdom) {
		//TODO: Add in name change system
		ConfigurationSection kingdomSection = kingdom.getDataPath();
		
		kingdomSection.set("king", (kingdom.getKing() == null) ? null : kingdom.getKing().toString());
		kingdomSection.set("queen", (kingdom.getQueen() == null) ? null : kingdom.getQueen().toString());
		kingdomSection.set("prince", (kingdom.getPrince() == null) ? null : kingdom.getPrince().toString());
		kingdomSection.set("princess", (kingdom.getPrincess() == null) ? null : kingdom.getPrincess().toString());
		kingdomSection.set("duke", (kingdom.getDuke() == null) ? null : kingdom.getDuke().toString());
		kingdomSection.set("dutchess", (kingdom.getDuchess() == null) ? null : kingdom.getDuchess().toString());
		kingdomSection.set("earls", Util.toStringList(kingdom.getEarls()));
		
		this.plugin.getBankManager().saveBank(kingdom.getBank());
		this.plugin.getArmyManager().saveArmy(kingdom.getArmy());
		this.plugin.getChurchManager().saveChurch(kingdom.getChurch());
		kingdom.getFlag().saveFlag();
		kingdom.getSettings().saveSettings();
		
		List<String> fiefs = new ArrayList<String>();
		for (Fief fief : kingdom.getFiefs()) {
			this.plugin.getFiefManager().saveFief(fief);
			fiefs.add(fief.getName());
		}
		kingdomSection.set("fiefs", fiefs);
		kingdomSection.set("capital", LandUtil.toString(kingdom.getCapital()));
	}
	
	public void deleteKingdom(Kingdom kingdom) {
		this.plugin.getKingdomData().get().set(kingdom.getDataPath().getCurrentPath(), null);
		this.unregisterKingdom(kingdom.getName());
	}
	
	public void saveAll() {
		for (Kingdom kingdom : this.kingdoms) {
			this.saveKingdom(kingdom);
		}
	}
	
	public Kingdom getLandOwner(Chunk chunk) {
		Land land = this.plugin.getLandManager().getLand(LandUtil.toString(chunk));
		if (land != null) {
			return land.kingdom;
		}
		return null;
	}
}
