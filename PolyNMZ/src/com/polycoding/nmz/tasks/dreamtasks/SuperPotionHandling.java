package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.enums.RegularPotionData;

public class SuperPotionHandling extends Task {

	public SuperPotionHandling(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
	}

	@Override
	public String getName() {
		return "SuperPotionHandling ";
	}

	@Override
	public boolean validate() throws Exception {
		return invHasAnySuperMeleePot() && !s.inventory.contains(OVERLOAD_POTION);
	}

	@Override
	public void execute() throws Exception {
		int atk = s.skills.getDynamic(Skill.ATTACK);
		int str = s.skills.getDynamic(Skill.STRENGTH);
		int def = s.skills.getDynamic(Skill.DEFENCE);
		if (atk <= s.skills.getStatic(Skill.ATTACK)
				&& s.inventory.contains(RegularPotionData.SUPER_ATTACK.getNames())) {
			if (s.inventory.getItem(RegularPotionData.SUPER_ATTACK.getNames()).interact("Drink")) {
				new ConditionalSleep(5500, 500) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.skills.getDynamic(Skill.ATTACK) >= s.skills.getStatic(Skill.ATTACK);
					}

				}.sleep();
			}
		}
		devSleep(1500, 250);
		if (str <= s.skills.getStatic(Skill.STRENGTH)
				&& s.inventory.contains(RegularPotionData.SUPER_STRENGTH.getNames())) {
			if (s.inventory.getItem(RegularPotionData.SUPER_STRENGTH.getNames()).interact("Drink")) {
				new ConditionalSleep(5500, 500) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.skills.getDynamic(Skill.STRENGTH) >= s.skills.getStatic(Skill.STRENGTH);
					}

				}.sleep();
			}
		}

		devSleep(1500, 250);
		if (def <= s.skills.getStatic(Skill.DEFENCE)
				&& s.inventory.contains(RegularPotionData.SUPER_DEFENCE.getNames())) {
			if (s.inventory.getItem(RegularPotionData.SUPER_DEFENCE.getNames()).interact("Drink")) {
				new ConditionalSleep(5500, 500) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.skills.getDynamic(Skill.DEFENCE) >= s.skills.getStatic(Skill.DEFENCE);
					}

				}.sleep();
			}
		}
		s.mouse.moveOutsideScreen();
	}

}
