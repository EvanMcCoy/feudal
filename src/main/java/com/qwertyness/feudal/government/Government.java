package com.qwertyness.feudal.government;

public interface Government extends CivilOrganizer {

	public String getName();
	public Church getChurch();
	public Army getArmy();
}
