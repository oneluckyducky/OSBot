package com.polycoding.jades;

import org.osbot.rs07.api.map.Area;

public class InstanceSettings {

	public Area edgeBank = null;
	public Area edgeFurnace = null;

	public ActiveState activeState = null;

	public enum ActiveState {
		CRAFTING, ENCHANTING;
	}
}
