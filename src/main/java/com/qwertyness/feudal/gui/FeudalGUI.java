package com.qwertyness.feudal.gui;

import java.util.UUID;

public interface FeudalGUI {

	public UUID getPlayer();
	public void openGUI();
	public void closeGUI();
	public void handleLeftClick(int slot);
	public void handleRightClick(int slot);
}
