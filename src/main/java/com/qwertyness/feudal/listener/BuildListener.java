package com.qwertyness.feudal.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.data.FeudalPlayer;
import com.qwertyness.feudal.government.Fief;
import com.qwertyness.feudal.government.Kingdom;
import com.qwertyness.feudal.government.Land;
import com.qwertyness.feudal.government.settings.Settings.GovernmentPermission;
import com.qwertyness.feudal.government.settings.Settings.TitlePermission;
import com.qwertyness.feudal.util.LandUtil;
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
		Bukkit.broadcastMessage("EventFire");
		if (!hasBuildPermission(event.getPlayer(), event.getBlock().getChunk())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(Configuration.instance.messages.prefix + Configuration.instance.messages.cannotBuild);
			return;
		}
		Bukkit.broadcastMessage("HasBuildPerm");
		FeudalPlayer player = this.plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
		Kingdom kingdomOwner = this.plugin.getKingdomManager().getKingdom(player.kingdom);
		Fief fiefOwner = this.plugin.getFiefManager().getFief(player.kingdom, player.fief);
		Bukkit.broadcastMessage("Kingdom: " + kingdomOwner + " Fief: " + fiefOwner);
		String title = Util.getTitle(event.getPlayer(), kingdomOwner, fiefOwner);
		Bukkit.broadcastMessage("Title: " + title);
		if (title != null) {
			if (!Util.getTitle(event.getPlayer(), kingdomOwner, fiefOwner).equalsIgnoreCase("serf")) {
				return;
			}
		}
		else {
			return;
		}
		Bukkit.broadcastMessage("IsSerf");
		List<ItemStack> drops = new ArrayList<ItemStack>(event.getBlock().getDrops());
		
		
		
		Bukkit.broadcastMessage("PreDrops: " + drops);
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
				Bukkit.broadcastMessage("NonNullFief");
				Bukkit.broadcastMessage("PercentTax: " + fiefOwner.getSettings().getBlockTaxPercent());
				playerDropAmount = (int)Math.ceil(item.getAmount()*0.01*(100-fiefOwner.getSettings().getBlockTaxPercent()));
				Bukkit.broadcastMessage("PDA: " + playerDropAmount);
				fiefDropAmount = (int)Math.floor(item.getAmount()*0.01*fiefOwner.getSettings().getBlockTaxPercent());
				Bukkit.broadcastMessage("FDA: " + fiefDropAmount);
				
				if (fiefDropAmount == 0) {
					Bukkit.broadcastMessage("Zero FDA");
					int rand = new Random().nextInt(100);
					if (rand < fiefOwner.getSettings().getBlockTaxPercent()) {
						playerDropAmount--;
						fiefDropAmount++;
					}
					Bukkit.broadcastMessage(playerDropAmount + ":" + fiefDropAmount);
				}
			}
			if (kingdomOwner != null) {
				Bukkit.broadcastMessage("NonNullKingdom");
				Bukkit.broadcastMessage("PercentTax: " + kingdomOwner.getSettings().getBlockTaxPercent());
				fiefDropAmount = (int)Math.ceil(fiefDropAmount*0.01*(100-kingdomOwner.getSettings().getBlockTaxPercent()));
				Bukkit.broadcastMessage("FDA: " + fiefDropAmount);
				kingdomDropAmount = (int)Math.floor(fiefDropAmount*0.01*kingdomOwner.getSettings().getBlockTaxPercent());
				Bukkit.broadcastMessage("KDA: " + kingdomDropAmount);
				
				if (kingdomDropAmount == 0) {
					Bukkit.broadcastMessage("Zero KDA");
					int rand = new Random().nextInt(100);
					if (rand < kingdomOwner.getSettings().getBlockTaxPercent()) {
						fiefDropAmount--;
						kingdomDropAmount++;
					}
					Bukkit.broadcastMessage(fiefDropAmount + ":" + kingdomDropAmount);
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
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		event.getPlayer().getWorld().playEffect(event.getPlayer().getLocation(), Effect.STEP_SOUND, event.getBlock().getType());
	}
	
	public boolean hasBuildPermission(Player player, Chunk chunk) {
		Kingdom owner = this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk());
		Kingdom playerKingdom = Util.getKingdom(player);
		if (owner == null) {
			return true;
		}
		else if (playerKingdom == null) {
			if (owner.getSettings().getBuildPermission() == GovernmentPermission.ALL) {
				return true;
			}
		}
		else if (owner.getName().equals(playerKingdom.getName())) {
			if (owner.getSettings().getBuildPermission().titleHasPermission(Util.getTitle(player, owner, null), TitlePermission.KINGDOM_LEVEL)) {
				Land land = this.plugin.getLandManager().getLand(LandUtil.toString(chunk));
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
