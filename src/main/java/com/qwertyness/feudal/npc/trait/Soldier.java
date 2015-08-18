package com.qwertyness.feudal.npc.trait;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
		for (Entity entity : this.getNPC().getEntity().getLocation().getChunk().getEntities()) {
			if (NPCManager.getRegistry().isNPC(entity)) {
				NPC npc = NPCManager.getRegistry().getNPC(entity);
				for (Attack attack : Feudal.getInstance().getArmyManager().getAttacks(guard.getEntity().getLocation().getChunk())) {
					//TODO: Add power matching and enemy detection
					if (attack.containsNPC(npc)) {
						return npc;
					}
				}
			}
		}
		return null;
	}
	
	protected Location getNavigationLocation(NPC guard, NPC enemy, double range) {
		Vector line = guard.getEntity().getLocation().toVector().subtract(enemy.getEntity().getLocation().toVector());
		line = line.normalize().multiply(range);
		return enemy.getEntity().getLocation().subtract(line);
	}
	
	protected void navigate(NPC npc, Location location) {
		npc.getNavigator().setTarget(location);
	}
	
}
