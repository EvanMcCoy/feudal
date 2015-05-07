package com.qwertyness.feudal.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.government.Church;
import com.qwertyness.feudal.util.Util;

public class ChurchManager {

	public Church loadChurch(ConfigurationSection government) {
		ConfigurationSection section = government.getConfigurationSection("church");
		
		UUID pope = (section.getString("pope") != null) ? UUID.fromString(section.getString("pope")) : null;
		List<UUID> abbots = Util.toUUIDList(section.getStringList("abbots"));
		Bank bank = Feudal.getInstance().bankManager.loadBank(section);
		
		return new Church(pope, abbots, bank, section);
	}
	
	public void saveChurch(Church church) {
		ConfigurationSection section = church.getDataPath();
		
		section.set("pope", church.pope.toString());
		section.set("abbots", Util.toStringList(church.abbots));
		Feudal.getInstance().bankManager.saveBank(church);
	}
}
