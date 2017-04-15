package com.polycoding.powertraining;

import java.util.LinkedList;

import com.polycoding.powertraining.util.SpecialWeapon;

public class InstanceSettings {

	public LinkedList<String> npcList = new LinkedList<String>();
	public LinkedList<String> itemList = new LinkedList<String>();
	public LinkedList<String> lvlList = new LinkedList<String>();

	public String ammoName = "";
	public String bonesName = "";

	public int eatPercent = 50;
	public int maxDistance = 15;
	public int buryAt = 4;
	public long profit = 0;

	public boolean lootBuryBones = false;
	public boolean isRanging = false;
	public boolean lootValuables = false;
	public boolean stopOutOfFood = false;

	public String stopLootName = "";

	public SpecialWeapon specialWeapon = null;
	public String mainWeapon = "";

}
