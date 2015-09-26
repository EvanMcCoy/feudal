package com.qwertyness.feudal.npc.trait;

import java.util.Calendar;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.npc.NPC;

public class Archer extends Soldier {
	private long nextFire = -1;

	public Archer(EntityType mountEntity) {
		super("archertrait", mountEntity);
	}
	
	@Override
	public void run() {
		if (nextFire == -1) {
			nextFire = Calendar.getInstance().getTimeInMillis();
		}
		
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if (currentTime >= nextFire) {
			shoot();
			//TODO: Add configurable delay
			nextFire = currentTime += 1000;
		}
	}
	
	public void shoot() {
		//TODO: Add shoot mechanics
		Entity guard = this.getNPC().getEntity();
		NPC target = this.getTrackingEnemy(this.getNPC());
		Location targetLoc = target.getEntity().getLocation();
		
		if (guard.getLocation().distance(targetLoc) < 10) {
			//TODO: Add accuracy configuration option
			Location shootLoc = guard.getLocation().add(0, 1, 0);
			Arrow arrow = (Arrow) guard.getWorld().spawnEntity(shootLoc, EntityType.ARROW);
			arrow.getLocation().setDirection(shootLoc.getDirection().multiply(1.5));
			arrow.getLocation().setPitch((float)(new Random().nextFloat() - 0.5)*5);
			arrow.getLocation().setY((float)(new Random().nextFloat() - 0.5)*5);
		}
		else {
			this.navigate(this.getNPC(), this.getNavigationLocation(this.getNPC(), target, 10.0));
		}
	}
}
