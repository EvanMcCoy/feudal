package com.qwertyness.feudal.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Util;
import com.qwertyness.feudal.government.Church;

public class ChurchManager {

	public Church loadChurch(ConfigurationSection government) {
		ConfigurationSection section = government.getConfigurationSection("church");
		
		UUID pope = (section.getString("pope") != null) ? UUID.fromString(section.getString("pope")) : null;
		List<UUID> abbots = Util.toUUIDList(section.getStringList("abbots"));
		
		return new Church(pope, abbots, section);
	}
	
	public void saveChurch(Church church) {
		ConfigurationSection section = church.getDataPath();
		
		section.set("pope", church.pope.toString());
		section.set("abbots", Util.toStringList(church.abbots));
	}
}
