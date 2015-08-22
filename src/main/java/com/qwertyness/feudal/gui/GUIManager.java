package com.qwertyness.feudal.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIManager implements Listener {
	private static List<FeudalGUI> guis;
	
	public GUIManager() {
		guis = new ArrayList<FeudalGUI>();
	}
	
	public static void addGUI(FeudalGUI gui) {
		if (guis != null) {
			guis.add(gui);
		}
	}
	
	public static void removeGUI(FeudalGUI gui) {
		if (guis != null) {
			guis.remove(gui);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		FeudalGUI clickedGUI = null;
		for (FeudalGUI gui : guis) {
			if (gui.getPlayer().compareTo(event.getWhoClicked().getUniqueId()) == 0) {
				clickedGUI = gui;
			}
		}
		if (clickedGUI == null) {
			return;
		}
		if (event.getSlot() < 0) {
			return;
		}
		
		if (event.getClick() == ClickType.LEFT) {
			clickedGUI.handleLeftClick(event.getSlot());
		}
		if (event.getClick() == ClickType.RIGHT) {
			clickedGUI.handleRightClick(event.getSlot());
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		for (FeudalGUI gui : new ArrayList<FeudalGUI>(guis)) {
			if (gui.getPlayer().compareTo(event.getPlayer().getUniqueId()) == 0) {
				removeGUI(gui);
			}
		}
	}
	
}
