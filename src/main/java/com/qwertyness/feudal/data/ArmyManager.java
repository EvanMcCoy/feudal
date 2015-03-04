package com.qwertyness.feudal.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Util;
import com.qwertyness.feudal.government.Army;

public class ArmyManager {
	
	public Army loadArmy(ConfigurationSection government) {
		ConfigurationSection section = government.getConfigurationSection("army");
		
		UUID knight = (section.getString("knight") != null) ? UUID.fromString(section.getString("knight")) : null;
		UUID dame = (section.getString("dame") != null) ? UUID.fromString(section.getString("dame")) : null;
		List<UUID> soldiers = Util.toUUIDList(section.getStringList("soldiers"));
		
		return new Army(knight, dame, soldiers);
	}
	
	public void saveArmy(Army army) {
		ConfigurationSection section = army.getDataPath().getConfigurationSection("army");
		
		section.set("knight", army.knight.toString());
		section.set("dame", army.dame.toString());
		section.set("soldiers", Util.toStringList(army.soldiers));
	}
}
