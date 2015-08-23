package com.qwertyness.feudal.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.npc.FeudalNPC;
import com.qwertyness.feudal.npc.NPCProfile;

import net.md_5.bungee.api.ChatColor;

public class ArmyDeployGUI implements FeudalGUI {
	private Player player;
	private Army army;
	private HashMap<NPCProfile, Integer> availableSoldiers;
	private Inventory inventory;
	
	public ArmyDeployGUI(Player player, Army army) {
		this.player = player;
		this.army = army;
		GUIManager.addGUI(this);
	}

	public UUID getPlayer() {
		return this.player.getUniqueId();
	}

	public void openGUI() {
		HashMap<NPCProfile, Integer> availableToDeploy = new HashMap<NPCProfile, Integer>();
		for (FeudalNPC npc : army.getNPCs()) {
			if (availableToDeploy.containsKey(npc.getProfile())) {
				availableToDeploy.put(npc.getProfile(), availableToDeploy.get(npc.getProfile()) + 1);
			}
			else {
				availableToDeploy.put(npc.getProfile(), 1);
			}
		}
		availableSoldiers = availableToDeploy;
		inventory = Bukkit.createInventory(this.player, (int)Math.ceil(availableToDeploy.keySet().size()/9)*9+9, "Buy/Sell Soldiers");
		for (NPCProfile profile : availableToDeploy.keySet()) {
			ItemStack icon = new ItemStack(profile.iconMaterial, 0);
			ItemMeta iconMeta = icon.getItemMeta();
			iconMeta.setDisplayName(profile.profileDisplayName);
			iconMeta.setLore(profile.profileLore);
			iconMeta.getLore().add(ChatColor.GREEN + "Available: " + ChatColor.GOLD + availableToDeploy.get(profile));
			icon.setItemMeta(iconMeta);
			inventory.setItem(NPCProfile.profiles.indexOf(profile), icon);
		}
		
		ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.setDisplayName(ChatColor.GREEN + "Deploy");
		confirm.setItemMeta(confirmMeta);
		inventory.setItem(inventory.getSize() - 4, confirm);
		
		ItemStack cancel = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta cancelMeta = confirm.getItemMeta();
		cancelMeta.setDisplayName(ChatColor.RED + "Cancel");
		cancel.setItemMeta(cancelMeta);
		inventory.setItem(inventory.getSize() - 6, cancel);
		
		player.openInventory(inventory);
	}

	public void closeGUI() {
		player.closeInventory();
	}

	public void handleLeftClick(int slot) {
		ItemStack clickedItem = inventory.getItem(slot);
		if (clickedItem == null) {
			return;
		}
		
		if (clickedItem.getItemMeta().getDisplayName().contains("Cancel")) {
			closeGUI();
		}
		else if (clickedItem.getItemMeta().getDisplayName().contains("Deploy")) {
			HashMap<NPCProfile, Integer> deployNPCs = new HashMap<NPCProfile, Integer>();
			
			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					if (item.getItemMeta().getDisplayName() != null) {
						NPCProfile profile = null;
						for (NPCProfile testProfile : NPCProfile.profiles) {
							if (testProfile.profileDisplayName.equals(item.getItemMeta().getDisplayName())) {
								profile = testProfile;
							}
						}
						if (profile == null) {
							continue;
						}
						deployNPCs.put(profile, item.getAmount());
					}
				}
			}
			
			for (NPCProfile profile : deployNPCs.keySet()) {
				int neededAmount = deployNPCs.get(profile);
				for (FeudalNPC npc : army.getNPCs()) {
					if (neededAmount == 0) {
						break;
					}
					if (npc.getProfile().profileName.equals(profile.profileName)) {
						npc.spawn(player.getLocation());
						neededAmount--;
					}
				}
			}
			closeGUI();
		}
		else {
			NPCProfile profile = null;
			for (NPCProfile testProfile : NPCProfile.profiles) {
				if (testProfile.profileDisplayName.equals(clickedItem.getItemMeta().getDisplayName())) {
					profile = testProfile;
				}
			}
			if (availableSoldiers.get(profile) > clickedItem.getAmount()) {
				clickedItem.setAmount(clickedItem.getAmount()+1);
			}
		}
	}

	public void handleRightClick(int slot) {
		ItemStack clickedItem = inventory.getItem(slot);
		if (clickedItem == null) {
			return;
		}
		
		if (!clickedItem.getItemMeta().getDisplayName().contains("Cancel") && !clickedItem.getItemMeta().getDisplayName().contains("Confirm Purchase")) {
			//Add selling if possible.
			clickedItem.setAmount(clickedItem.getAmount() - 1);
		}
	}

}
