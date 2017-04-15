package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.ConstantVars;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.Timer;

public class OverloadHandling extends Task {

	public OverloadHandling(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "OverloadHandling ";
	}

	@Override
	public boolean validate() throws Exception {
		return s.inventory.contains(OVERLOAD_POTION) && (s.overloadTimer == null || !s.overloadTimer.isRunning());
	}

	@Override
	public void execute() throws Exception {

		if (s.skills.getDynamic(Skill.HITPOINTS) >= 51 && s.inventory.contains(ConstantVars.OVERLOAD_POTION)) {

			// Script.sleep(Script.random(Script.random(4500, 9000),
			// Script.random(15000, 20000)));
			if (s.inventory.getItem(ConstantVars.OVERLOAD_POTION).interact("Drink")) {
				devSleep(2000,600);
				if (s.inventory.contains(ConstantVars.OVERLOAD_POTION))
					s.overloadTimer = new Timer(300000 + Script.gRandom(0, 1, 0.9));
				s.mouse.moveOutsideScreen();
			}
		}

	}

}
