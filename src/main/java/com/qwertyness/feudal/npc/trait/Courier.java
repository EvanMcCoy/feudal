package com.qwertyness.feudal.npc.trait;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.qwertyness.feudal.npc.NPCManager;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

public class Courier extends Trait {
	
	@Persist EntityType mountEntity = null;

	protected Courier(String name, EntityType mountEntity) {
		super(name);
		this.mountEntity = mountEntity;
	}
	
	public Courier(EntityType mountEntity) {
		this("couriertrait", mountEntity);
	}
	
	@Override
	public void run() {
		Player target = getTarget();
		if (target != null) {
			Location location = getNextTargetLocation(target);
			
			NPC courier = this.getNPC();
			Entity vehicle = this.getNPC().getEntity().getVehicle();
			if (vehicle != null) {
				courier = NPCManager.getRegistry().getNPC(vehicle);
			}
			navigate(courier, getNextTargetLocation(target));
		}
	}

	protected Player getTarget() {
		return null;
	}
	
	protected Location getNextTargetLocation(Player player) {
		Vector line = this.getNPC().getEntity().getLocation().toVector().subtract(player.getLocation().toVector());
		line = line.normalize().multiply(16);
		return this.getNPC().getEntity().getLocation().add(line);
	}
	
	protected void navigate(NPC courier, Location location) {
		courier.getNavigator().setTarget(location);
	}
}
