package com.qwertyness.feudal.data.government;

import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.government.Bank;
import com.qwertyness.feudal.util.Util;

public class ArmyManager {
	
	public Army loadArmy(ConfigurationSection government) {
		ConfigurationSection section = government.getConfigurationSection("army");
		
		UUID knight = (section.getString("knight") != null) ? UUID.fromString(section.getString("knight")) : null;
		UUID dame = (section.getString("dame") != null) ? UUID.fromString(section.getString("dame")) : null;
		List<UUID> soldiers = Util.toUUIDList(section.getStringList("soldiers"));
		Bank bank = Feudal.getInstance().getBankManager().loadBank(section);
		
		return new Army(knight, dame, soldiers, bank, section);
	}
	
	public void saveArmy(Army army) {
		ConfigurationSection section = army.getDataPath().getConfigurationSection("army");
		
		section.set("knight", (army.getKnight() == null) ? null : army.getKnight().toString());
		section.set("dame", (army.getDame() == null) ? null : army.getDame().toString());
		section.set("soldiers", Util.toStringList(army.getSoldiers()));
		Feudal.getInstance().getBankManager().saveBank(army);
	}
}
