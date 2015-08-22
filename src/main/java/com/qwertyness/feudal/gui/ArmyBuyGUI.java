package com.qwertyness.feudal.gui;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.government.Army;
import com.qwertyness.feudal.npc.FeudalNPC;
import com.qwertyness.feudal.npc.NPCProfile;

import net.md_5.bungee.api.ChatColor;

public class ArmyBuyGUI implements FeudalGUI {
	private Player player;
	private Army army;
	private Inventory inventory;
	
	public ArmyBuyGUI(Player player, Army army) {
		this.player = player;
		this.army = army;
		GUIManager.addGUI(this);
	}

	public UUID getPlayer() {
		return this.player.getUniqueId();
	}

	public void openGUI() {
		inventory = Bukkit.createInventory(this.player, (int)Math.ceil(NPCProfile.profiles.size()/9)*9+9, "Buy/Sell Soldiers");
		
		for (NPCProfile profile : NPCProfile.profiles) {
			ItemStack icon = new ItemStack(profile.iconMaterial, 0);
			ItemMeta iconMeta = icon.getItemMeta();
			iconMeta.setDisplayName(profile.profileDisplayName);
			iconMeta.setLore(profile.profileLore);
			iconMeta.getLore().add(ChatColor.GREEN + "Cost: " + ChatColor.GOLD + profile.cost);
			icon.setItemMeta(iconMeta);
			inventory.setItem(NPCProfile.profiles.indexOf(profile), icon);
		}
		
		ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta confirmMeta = confirm.getItemMeta();
		confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm Purchase");
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
		else if (clickedItem.getItemMeta().getDisplayName().contains("Confirm Purchase")) {
			System.out.println("Confirm Purchase");
			HashMap<NPCProfile, Integer> boughtNPCs = new HashMap<NPCProfile, Integer>();
			int cost = 0;
			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					if (item.getItemMeta().getDisplayName() != null) {
						NPCProfile profile = NPCProfile.profiles.stream().filter(p -> p.profileDisplayName.equals(item.getItemMeta().getDisplayName())).findFirst().orElse(null);
						if (profile == null) {
							continue;
						}
						boughtNPCs.put(profile, item.getAmount());
						cost += profile.cost*item.getAmount();
					}
				}
			}
			
			if (Configuration.useEconomy) {
				if (army.getBank().getBalance() < cost) {
					closeGUI();
					player.sendMessage(ChatColor.RED + Configuration.instance.messages.insufficientBankMoney);
					return;
				}
				army.getBank().withdrawMoney(cost);
				for (NPCProfile profile : boughtNPCs.keySet()) {
					for (int i = 0;i < boughtNPCs.get(profile);i++) {
						army.addNPC(new FeudalNPC(profile));
					}
				}
				closeGUI();
			}
		}
		else {
			clickedItem.setAmount(clickedItem.getAmount()+1);
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
