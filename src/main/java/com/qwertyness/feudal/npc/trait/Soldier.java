package com.qwertyness.feudal.npc.trait;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Attack;
import com.qwertyness.feudal.npc.NPCManager;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

public class Soldier extends Trait {

	@Persist("mountEntity") EntityType mountEntity = null;
	
	protected Soldier(String name, EntityType mountEntity) {
		super(name);
		this.mountEntity = mountEntity;
	}
	
	public Soldier(EntityType mountEntity) {
		this("soldiertrait", mountEntity);
	}
	
	@Override
	public void run() {
		if (this.getNPC().isSpawned()) {
			NPC guard = this.getNPC();
			Entity vehicle = this.getNPC().getEntity().getVehicle();
			if (vehicle != null) {
				guard = NPCManager.getRegistry().getNPC(vehicle);
			}
			NPC enemy = getTrackingEnemy(this.getNPC());
			if (enemy != null) {
				navigate(guard, getNavigationLocation(guard, enemy, 1.5));
			}
		}
		
	}
	
	protected NPC getTrackingEnemy(NPC guard) {
		Entity guardEntity = guard.getEntity();
		int guardRating = getRating(guard);
		
		NPC closestNPC = null;
		int differential = 0;
		for (Entity entity : this.getNPC().getEntity().getLocation().getChunk().getEntities()) {
			if (NPCManager.getRegistry().isNPC(entity)) {
				NPC npc = NPCManager.getRegistry().getNPC(entity);
				for (Attack attack : Feudal.getInstance().getArmyManager().getAttacks(guard.getEntity().getLocation().getChunk())) {
					//TODO: Add power matching and enemy detection
					if (!attack.containsNPC(npc)) {
						continue;
					}
					
					int rating = getRating(npc);
					if (guardRating - rating < differential) {
						closestNPC = npc;
						differential = guardRating - rating;
					}
					else if (guardRating - rating <= guardRating) {
						if (guardEntity.getLocation().distance(entity.getLocation()) < closestNPC.getEntity().getLocation().distance(guardEntity.getLocation())) {
							closestNPC = npc;
						}
					}
				}
			}
		}
		return closestNPC;
	}
	
	protected Location getNavigationLocation(NPC guard, NPC enemy, double range) {
		Vector line = guard.getEntity().getLocation().toVector().subtract(enemy.getEntity().getLocation().toVector());
		line = line.normalize().multiply(range);
		return enemy.getEntity().getLocation().subtract(line);
	}
	
	protected void navigate(NPC npc, Location location) {
		npc.getNavigator().setTarget(location);
	}
	
	protected int getRating(NPC npc) {
		LivingEntity entity = (LivingEntity) npc.getEntity();
		int score = 0;
		//Helmet
		ItemStack helmet = entity.getEquipment().getHelmet();
		switch (helmet.getType()) {
			default: score+=0;
			case LEATHER_HELMET: score+=1;
			case CHAINMAIL_HELMET: score+=2;
			case IRON_HELMET: score+=3;
			case DIAMOND_HELMET: score+=4;
		}
		score += helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
		
		//Chestplate
		ItemStack chestplate = entity.getEquipment().getChestplate();
		switch (chestplate.getType()) {
			default: score+=0;
			case LEATHER_CHESTPLATE: score+=1;
			case CHAINMAIL_CHESTPLATE: score+=2;
			case IRON_CHESTPLATE: score+=3;
			case DIAMOND_CHESTPLATE: score+=4;
		}
		score += chestplate.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
		score += chestplate.getEnchantmentLevel(Enchantment.THORNS);
		
		//Leggings
		ItemStack leggings = entity.getEquipment().getLeggings();
		switch (leggings.getType()) {
			default: score+=0;
			case LEATHER_LEGGINGS: score+=1;
			case CHAINMAIL_LEGGINGS: score+=2;
			case IRON_LEGGINGS: score+=3;
			case DIAMOND_LEGGINGS: score+=4;
		}
		score += leggings.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
		
		//Boots
		ItemStack boots = entity.getEquipment().getBoots();
		switch (boots.getType()) {
			default: score+=0;
			case LEATHER_BOOTS: score+=1;
			case CHAINMAIL_BOOTS: score+=2;
			case IRON_BOOTS: score+=3;
			case DIAMOND_BOOTS: score+=4;
		}
		score+= boots.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
		
		ItemStack weapon = entity.getEquipment().getItemInHand();
		switch (weapon.getType()) {
			default: score+=1;
			case WOOD_SWORD: score+=1;
			case STONE_SWORD: score+=2;
			case IRON_SWORD: score+=3;
			case DIAMOND_SWORD: score+=4;
			case BOW: score+=2;
			case STONE_AXE: score+=1;
			case IRON_AXE: score+=2;
			case DIAMOND_AXE: score+=3;
		}
		score += weapon.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
		score += weapon.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
		score += weapon.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
		score += weapon.getEnchantmentLevel(Enchantment.ARROW_FIRE);
		score += weapon.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK);
		score += weapon.getEnchantmentLevel(Enchantment.KNOCKBACK);
		
		return score;
	}
	
}
