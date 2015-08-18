package com.qwertyness.feudal.government;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.BlockState;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.npc.FeudalNPC;
import com.qwertyness.feudal.util.LandUtil;

import net.citizensnpcs.api.npc.NPC;

public class Attack {
	private Kingdom attacker;
	private Kingdom defender;
	private Land disputedLand;
	private List<UUID> attackNPCs;
	
	private List<BlockState> flagStructureHistory;
	
	public Attack(Kingdom attacker, Kingdom defender, Land disputedLand, List<FeudalNPC> attackNPCs, List<BlockState> flagStructureHistory) {
		this.attacker = attacker;
		this.defender = defender;
		this.disputedLand = disputedLand;
		this.attackNPCs = new ArrayList<UUID>();
		for(FeudalNPC npc : attackNPCs) {
			this.attackNPCs.add(npc.getUUID());
		}
		this.flagStructureHistory = flagStructureHistory;
	}
	
	public Kingdom getAttacker() {
		return this.attacker;
	}
	public Kingdom getDefender() {
		return this.defender;
	}
	public Land getDisputedLand() {
		return this.disputedLand;
	}
	public List<UUID> getAttackNPCs() {
		return this.attackNPCs;
	}
	public boolean containsNPC(NPC npc) {
		if (this.attackNPCs.contains(npc.getUniqueId())) {
			return true;
		}
		return false;
	}
	
	public boolean compareDisputedLand(Land land) {
		if (LandUtil.toString(land.getChunk()).equals(LandUtil.toString(this.disputedLand.getChunk()))) {
			return true;
		}
		return false;
	}
	
	public void capture() {
		this.cancel();
		LandUtil.unclaimLand(this.defender, disputedLand.getChunk());
		LandUtil.claimLand(this.attacker, disputedLand.getChunk());
	}
	
	public void destroyFlag() {
		for (BlockState block : this.flagStructureHistory) {
			block.update();
		}
	}
	
	public void removeNPC(FeudalNPC npc) {
		this.attackNPCs.remove(npc.getUUID());
		this.attacker.getArmy().removeNPC(this.attacker.getArmy().getNPCs().indexOf(npc));
		for (Fief fief : this.attacker.getFiefs()) {
			fief.getArmy().removeNPC(this.attacker.getArmy().getNPCs().indexOf(npc));
		}
	}
	
	public void cancel() {
		Feudal.getInstance().getArmyManager().removeAttack(this);
		this.destroyFlag();
	}
}
