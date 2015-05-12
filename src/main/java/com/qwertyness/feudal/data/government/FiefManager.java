package com.qwertyness.feudal.data.government;

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

public class FiefManager {
	private Feudal plugin;
	
	public FiefManager() {
		this.plugin = Feudal.getInstance();
	}
	
	public boolean isFief(String kingdomName, String fiefName) {
		if (getFief(kingdomName, fiefName) != null) {
			return true;
		}
		return false;
	}
	
	public Fief getFief(String kingdomName, String fiefName) {
		Kingdom kingdom = this.plugin.kingdomManager.getKingdom(kingdomName);
		
		for (Fief fief : kingdom.fiefs) {
			if (fief.getName().equals(fiefName)) {
				return fief;
			}
		}
		return null;
	}
	
	public Fief loadFief(String kingdomName, String fiefName) {
		ConfigurationSection fiefSection = this.plugin.fiefData.get().getConfigurationSection(kingdomName + "." + fiefName);
		
		String name = fiefSection.getName();
		UUID baron = (fiefSection.getString("baron") != null) ? UUID.fromString(fiefSection.getString("baron")) : null;
		UUID baroness = (fiefSection.getString("baroness") != null) ? UUID.fromString(fiefSection.getString("baroness")) : null;
		List<UUID> peasents = Util.toUUIDList(fiefSection.getStringList("peasents"));
		List<UUID> serfs = Util.toUUIDList(fiefSection.getStringList("serfs"));
		Bank bank = this.plugin.bankManager.loadBank(fiefSection);
		Army army = this.plugin.armyManager.loadArmy(fiefSection);
		Church church = this.plugin.churchManager.loadChurch(fiefSection);
		Chunk capital = Util.toChunk(fiefSection.getString("capital"));
		
		return new Fief(name, baron, baroness, peasents, serfs, bank, army, church, capital, fiefSection);
	}
	
	public void saveFief(Fief fief) {
		ConfigurationSection fiefSection = fief.getDataPath();
		
		fiefSection.set("baron", (fief.baron == null) ? null : fief.baron.toString());
		fiefSection.set("baroness", (fief.baroness == null) ? null : fief.baroness.toString());
		fiefSection.set("peasents", Util.toStringList(fief.peasents));
		fiefSection.set("serfs", Util.toStringList(fief.serfs));
		fiefSection.set("capital", Util.toString(fief.capital));
		this.plugin.bankManager.saveBank(fief);
		this.plugin.armyManager.saveArmy(fief.army);
		this.plugin.churchManager.saveChurch(fief.church);
	}
	
	public void deleteFief(Kingdom kingdom, Fief fief) {
		this.plugin.fiefData.get().set(fief.getDataPath().getCurrentPath(), null);
		kingdom.fiefs.remove(fief);
	}
	
	public Fief getLandOwner(Chunk chunk) {
		for (Kingdom kingdom : this.plugin.kingdomManager.kingdoms) {
			Fief fief = getLandOwner(kingdom, chunk);
			if (fief != null) {
				return fief;
			}
		}
		return null;
	}
	
	public Fief getLandOwner(Kingdom kingdom, Chunk chunk) {
		return this.plugin.landManager.getLand(Util.toString(chunk)).fief;
	}
}
