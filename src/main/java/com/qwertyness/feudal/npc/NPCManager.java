package com.qwertyness.feudal.npc;

import java.io.File;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Army;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.npc.SimpleNPCDataStore;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.NBTStorage;
import net.citizensnpcs.api.util.Storage;
import net.citizensnpcs.api.util.YamlStorage;

public class NPCManager {
	private static NPCRegistry registry;
	private static NPCDataStore dataStore;
	
	public NPCManager() {
		registry = createRegistry();
	}
	
	private static NPCRegistry createRegistry() {
		Storage saves = null; 
		String type = "yaml"; 
		if (type.equalsIgnoreCase("nbt")) { 
			saves = new NBTStorage(new File(Feudal.getInstance().getDataFolder() + File.separator + "NPC.yml"), 
			"Citizens NPC Storage"); 
		} 
		if (saves == null) {
			saves = new YamlStorage(new File(Feudal.getInstance().getDataFolder(), "NPC.yml"), "Feudal NPC Storage");
		}
		dataStore = SimpleNPCDataStore.create(saves);
		NPCRegistry registry = CitizensAPI.createNamedNPCRegistry("FeudalRegistry", dataStore);
		return registry;
	}
	
	public static NPCRegistry getRegistry() {
		return registry;
	}
	public static NPCDataStore getDataStore() {
		return dataStore;
	}
}
