package com.qwertyness.feudal.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.util.Util;

public class BuildListener implements Listener {
	private Feudal plugin;
	
	public BuildListener(Feudal plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!hasBuildPermission(event.getPlayer(), event.getBlock().getChunk())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!hasBuildPermission(event.getPlayer(), event.getBlock().getChunk())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.cannotBuild);
			return;
		}
		Kingdom kingdomOwner = this.plugin.getKingdomManager().getLandOwner(event.getBlock().getChunk());
		Fief fiefOwner = this.plugin.getFiefManager().getLandOwner(event.getBlock().getChunk());
		if (!Util.getTitle(event.getPlayer(), kingdomOwner, fiefOwner).equalsIgnoreCase("serf")) {
			return;
		}
		List<ItemStack> drops = new ArrayList<ItemStack>(event.getBlock().getDrops());
		List<ItemStack> playerDrops = new ArrayList<ItemStack>();
		List<ItemStack> fiefDrops = new ArrayList<ItemStack>();
		List<ItemStack> kingdomDrops = new ArrayList<ItemStack>();
		event.getBlock().getDrops().clear();
		for (ItemStack item : drops) {
			ItemStack playerDrop  = new ItemStack(item);
			ItemStack fiefDrop  = new ItemStack(item);
			ItemStack kingdomDrop  = new ItemStack(item);
			int playerDropAmount = 0;
			int fiefDropAmount = 0;
			int kingdomDropAmount = 0;
			
			if (fiefOwner != null) {
				playerDropAmount = (int)Math.ceil(item.getAmount()*(100-fiefOwner.getSettings().getBlockTaxPercent()));
				fiefDropAmount = (int)Math.ceil(item.getAmount()*fiefOwner.getSettings().getBlockTaxPercent());
				
				if (fiefDropAmount == 0) {
					int rand = new Random().nextInt(100);
					if (rand < fiefOwner.getSettings().getBlockTaxPercent()) {
						playerDropAmount--;
						fiefDropAmount++;
					}
				}
			}
			if (kingdomOwner != null) {
				fiefDropAmount = (int)Math.ceil(fiefDropAmount*(100-kingdomOwner.getSettings().getBlockTaxPercent()));
				kingdomDropAmount = (int)Math.ceil(fiefDropAmount*kingdomOwner.getSettings().getBlockTaxPercent());
				
				if (kingdomDropAmount == 0) {
					int rand = new Random().nextInt(100);
					if (rand < kingdomOwner.getSettings().getBlockTaxPercent()) {
						fiefDropAmount--;
						kingdomDropAmount++;
					}
				}
			}
			
			if (playerDropAmount != 0) {
				playerDrop.setAmount(playerDropAmount);
				playerDrops.add(playerDrop);
			}
			if (fiefDropAmount != 0) {
				fiefDrop.setAmount(fiefDropAmount);
				fiefDrops.add(fiefDrop);
			}
			if (kingdomDropAmount != 0) {
				kingdomDrop.setAmount(kingdomDropAmount);
				kingdomDrops.add(kingdomDrop);
			}
		}
		for (ItemStack drop : playerDrops) {
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
		}
		for (ItemStack drop : fiefDrops) {
			fiefOwner.getBank().depositItem(drop.getType(), drop.getAmount());
		}
		for (ItemStack drop : kingdomDrops) {
			kingdomOwner.getBank().depositItem(drop.getType(), drop.getAmount());
		}
	}
	
	public boolean hasBuildPermission(Player player, Chunk chunk) {
		if (this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) == null) {
			return true;
		}
		Kingdom owner = this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk());
		Kingdom playerKingdom = Util.getKingdom(player);
		if (playerKingdom == null && owner.getSettings().getBuildPermission() != GovernmentPermission.ALL) {
			return false;
		}
		
		if (owner.getName().equals(playerKingdom.getName())) {
			if (owner.getSettings().getBuildPermission().titleHasPermission(Util.getTitle(player, owner, null), TitlePermission.KINGDOM_LEVEL)) {
				Land land = this.plugin.getLandManager().getLand(Util.toString(chunk));
				if (land.owners.size() > 0) {
					if (land.owners.contains(player.getUniqueId())) {
						return true;
					}
				}
				else {return true;}
			}
		}
		return false;
	}
}
