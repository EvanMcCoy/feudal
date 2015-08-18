package com.qwertyness.feudal.chat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import com.qwertyness.feudal.Configuration;
import com.qwertyness.feudal.util.Util;

@SuppressWarnings("deprecation")
public class ChatManager implements Listener {
	@EventHandler
	public void onMessage(PlayerChatEvent event) {
		if (Configuration.instance.titlesEnabled) {
			String title = Util.getTitle(event.getPlayer(), Util.getKingdom(event.getPlayer()), Util.getFief(event.getPlayer()));
			
			if (title == null) {
				title = Configuration.instance.defaultTitle;
			}
			else {
				title = title.substring(0, 1).toUpperCase() + title.substring(1);
			}
			for (String colorSection : Configuration.instance.titleColors.getKeys(false)) {
				if (title.equalsIgnoreCase(colorSection)) {
					title = Configuration.instance.titleColors.getString(colorSection) + title;
				}
			}
			String currentDisplayName = event.getPlayer().getDisplayName();
			if (currentDisplayName == null) {
				currentDisplayName = event.getPlayer().getName();
			}
			event.getPlayer().setDisplayName(ChatColor.translateAlternateColorCodes('&', Configuration.instance.titleLayout.replace("%title%", title) + currentDisplayName));
		}
		
	}
}
