package com.polycoding.nmz;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;

import com.polycoding.nmz.util.DynamicArea.PrincipalWind;
import com.polycoding.nmz.util.NMZArea;
import com.polycoding.nmz.util.enums.HealthDegrade;
import com.polycoding.nmz.util.enums.PotionData;
import com.polycoding.nmz.util.enums.RegularPotionData;
import com.polycoding.nmz.util.enums.SpecialWeapon;

public class ScriptSettings {

	public final Skill[] trackedSkills = { Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS, Skill.RANGED,
			Skill.MAGIC };

	/**
	 * Area around NMZ, encompasses NMZ, Ardougne bank, and some of inside
	 * Ardougne
	 */
	public Area nmzOutsideArea = null;

	/**
	 * The area of the inside of NMZ game
	 */
	public Area nmzInsideArea = null;

	public Area bankArea = null;

	public Position bankTile = null;
	public Position nmzTile = null;

	public boolean overload = false;
	public boolean absorption = false;
	public boolean prayerFlicking = false;

	public boolean isBlowpiping = false;
	public double blowpipeCharges = 0;
	public double blowpipeRechargePercent = 50;
	public int blowRechargeDartNum = 1000;
	public Filter<Item> blowpipeItem = new Filter<Item>() {
		@Override
		public boolean match(Item it) {
			return it.getName().contains("oxic blowp");
		}
	};

	public boolean only1Round = false;
	public boolean onlyBuyPotions = false; // no getting from barrels

	public HealthDegrade healthDegrade = null;
	public PrincipalWind afkLocation = null;
	public int maxDistFromAfk = 0;

	/**
	 * Used to prevent GetPotions Task from repeating, set true after
	 * GetPotion#execute(), set false after EnterDream#execute()
	 */
	public boolean hasGetPotion = false;

	public LinkedHashMap<PotionData, Integer> potionsData = new LinkedHashMap<PotionData, Integer>();

	public HashMap<RegularPotionData, Integer> bankPotionData = new HashMap<RegularPotionData, Integer>();

	public boolean hasSetDream = false;
	public boolean hasCheckedBlowpipe = false;
	public boolean hasBanked = false;
	public boolean hp1 = false;
	public boolean prayMelee = false;

	public SpecialWeapon specialWeapon = null;
	public String mainWeapon = "";

	public int rounds = 0;

	public int startPoints = 0;

	public Position dreamStartPos = null;
	public Position dreamCenterPos = null;
	public Position dreamGoalPos = null;
	public NMZArea dreamDynamicArea = null;
	public Area dreamArea = null;

	public boolean hasEnteredDream = false; // for some reason, since 2.4.112
											// the client has throw a webwalk
											// issue. WalkToNMZ tries to run and
											// breaks the script when entering
											// the dream. This my ghetto rig

}
