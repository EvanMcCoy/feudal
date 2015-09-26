package com.qwertyness.feudal.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.Trait;
import net.md_5.bungee.api.ChatColor;

public class NPCProfile {
	public static List<NPCProfile> profiles = new ArrayList<NPCProfile>();
	
	public String profileName;
	public Material iconMaterial;
	public String profileDisplayName;
	public List<String> profileLore;
	
	public EntityType type;
	public List<Trait> npcTraits;
	public ItemStack helmet;
	public ItemStack chestplate;
	public ItemStack leggings;
	public ItemStack boots;
	public ItemStack hand;
	public String npcName;
	
	
	public int cost;
	
	public static NPCProfile fromConfigurationSection(ConfigurationSection section) {
		NPCProfile profile = new NPCProfile();
		profile.profileName = section.getName();
		Material material = null;
		try {
			material = Material.valueOf(section.getString("icon.material"));
		} catch(NullPointerException e) {
			material = Material.IRON_CHESTPLATE;
		}
		profile.iconMaterial = material;
		profile.profileDisplayName = ChatColor.translateAlternateColorCodes('&', section.getString("icon.displayName"));
		List<String> lore = section.getStringList("icon.lore");
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		for (int i = 0;i < lore.size();i++) {
			lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
		}
		profile.profileLore = lore;
		
		EntityType type = null;
		try {
			type = EntityType.valueOf(section.getString("type"));
		} catch(NullPointerException e) {
			type = EntityType.ZOMBIE;
		}
		profile.type = type;
		profile.helmet = itemFromConfigurationSection(section.getConfigurationSection("helmet"));
		profile.chestplate = itemFromConfigurationSection(section.getConfigurationSection("chestplate"));
		profile.leggings = itemFromConfigurationSection(section.getConfigurationSection("leggings"));
		profile.boots = itemFromConfigurationSection(section.getConfigurationSection("boots"));
		profile.hand = itemFromConfigurationSection(section.getConfigurationSection("hand"));
		profile.npcName = ChatColor.translateAlternateColorCodes('&', section.getString("name"));
		
		profile.cost = section.getInt("cost");
		
		profile.npcTraits = new ArrayList<Trait>();
		for (String traitName : section.getStringList("traits")) {
			Trait trait = CitizensAPI.getTraitFactory().getTrait(traitName + "trait");
			if (trait != null) {
				profile.npcTraits.add(trait);
			}
		}
		
		return profile;
	}
	
	private static ItemStack itemFromConfigurationSection(ConfigurationSection section) {
		if (section == null) {
			return new ItemStack(Material.AIR);
		}
		Material material = null;
		try {
			material = Material.valueOf(section.getString("material"));
		} catch(NullPointerException e) {
			material = Material.AIR;
		}
		HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		if (section.getConfigurationSection("enchantments") != null) {
			for (String enchantmentString : section.getConfigurationSection("enchantments").getKeys(false)) {
				Enchantment enchantmentType;
				int level;
				enchantmentType = Enchantment.getByName(enchantmentString);
				if (enchantmentType == null) {
					continue;
				}
				level = section.getInt("enchantments." + enchantmentString);
				if (level < 1) {
					continue;
				}
				enchantments.put(enchantmentType, level);
			}
		}
		
		ItemStack item = new ItemStack(material);
		for (Enchantment enchantment : enchantments.keySet()) {
			item.addEnchantment(enchantment, enchantments.get(enchantment));
		}
		
		return item;
	}
	
	public static NPCProfile getProfile(String npcProfile) {
		for (NPCProfile profile : profiles) {
			if (profile.profileName.equals(npcProfile)) {
				return profile;
			}
		}
		return null;
	}
}
