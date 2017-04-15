package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.api.ui.Skill;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.Timer;
import com.polycoding.nmz.util.enums.HealthDegrade;

public class RockCaking extends Task {

	public RockCaking(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "RockCaking ";
	}

	@Override
	public boolean validate() throws Exception {
		return s.ss.hp1 && hasRockCake() && (s.rockCakeTimer == null || !s.rockCakeTimer.isRunning());
	}

	@Override
	public void execute() throws Exception {
		int hp = s.skills.getDynamic(Skill.HITPOINTS);
		if (hp > 1 && s.ss.healthDegrade == HealthDegrade.DWARVEN_ROCK_CAKE) {
			if (s.rockCakeTimer == null || !s.rockCakeTimer.isRunning()) {
				do {
					s.inventory.getItem("Dwarven rock cake").interact("Guzzle");
					devSleep(900, 200);
				} while (s.skills.getDynamic(Skill.HITPOINTS) > 1);

				s.mouse.moveOutsideScreen();
				s.rockCakeTimer = new Timer(Math.round(nextDeviation(180000, 50000)));
			}
		}
		if (hp > 1 && hp < 51 && s.ss.healthDegrade == HealthDegrade.PRAYER_FLICKING && s.overloadTimer != null
				&& s.overloadTimer.isRunning() && s.overloadTimer.getElapsed() >= 5100) {
			do {
				s.inventory.getItem("Dwarven rock cake").interact("Guzzle");
				devSleep(1100, 320);
			} while (s.skills.getDynamic(Skill.HITPOINTS) > 1);
			s.mouse.moveOutsideScreen();
		}
		if (s.ss.healthDegrade == HealthDegrade.PRAYER_FLICKING && !s.inventory.contains(OVERLOAD_POTION) && hp > 1) {
			do {
				s.inventory.getItem("Dwarven rock cake").interact("Guzzle");
				devSleep(600, 150);
			} while (s.skills.getDynamic(Skill.HITPOINTS) > 1);
			s.mouse.moveOutsideScreen();
		}

	}

}
