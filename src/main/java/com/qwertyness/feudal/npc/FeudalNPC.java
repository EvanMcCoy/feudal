package com.qwertyness.feudal.npc;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

public class FeudalNPC  {
	protected UUID uuid;
	protected NPC npc;
	protected NPCProfile profile;

	public FeudalNPC(NPCProfile profile) {
		this.uuid = UUID.randomUUID();
	}
	
	public FeudalNPC(UUID uuid, NPCProfile profile) {
		this.uuid = uuid;
		this.profile = profile;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public NPCProfile getProfile() {
		return this.profile;
	}
	
	public boolean isSpawned() {
		if (this.npc == null) {
			return false;
		}
		return true;
	}
	
	public void spawn(Location location) {
		this.npc = NPCManager.getRegistry().createNPC(this.profile.type, this.uuid, NPCManager.getDataStore().createUniqueNPCId(NPCManager.getRegistry()), this.profile.npcName);
		for (Trait trait : this.profile.npcTraits) {
			this.npc.addTrait(trait);
		}
		if (this.npc.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) this.npc.getEntity();
			entity.getEquipment().setArmorContents(new ItemStack[] {this.profile.helmet, this.profile.chestplate, this.profile.leggings, this.profile.boots});
			entity.getEquipment().setItemInHand(this.profile.hand);
		}
		this.npc.spawn(location);
	}
	
	public void despawn() {
		if (this.npc != null) {
			this.npc.destroy();
		}
	}
}