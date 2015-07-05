package com.qwertyness.feudal.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.qwertyness.feudal.Feudal;
import com.qwertyness.feudal.government.Kingdom;
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
		if (!hasBuildPermission(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!hasBuildPermission(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	public boolean hasBuildPermission(Player player) {
		if (this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk()) == null) {
			return true;
		}
		Kingdom owner = this.plugin.getKingdomManager().getLandOwner(player.getLocation().getChunk());
		if (Util.isInKingdom(player) && owner.getSettings().buildPermission != GovernmentPermission.ALL) {
			return false;
		}
		Kingdom playerKingdom = Util.getKingdom(player);
		if (owner.getName().equals(playerKingdom.getName())) {
			if (owner.getSettings().buildPermission.titleHasPermission(Util.getTitle(player, owner, null), TitlePermission.KINGDOM_LEVEL)) {
				return true;
			}
		}
		return false;
	}
}
