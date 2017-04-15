package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;

public class SpecialAttacking extends Task {

	public SpecialAttacking(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "SpecialAttacking ";
	}

	@Override
	public boolean validate() throws Exception {
		return s.ss.specialWeapon != null && s.combat.getSpecialPercentage() >= s.ss.specialWeapon.getSpecAmount()
				&& !s.combat.isSpecialActivated() && Script.random(0, 10) % 2 == 0;
	}

	@Override
	public void execute() throws Exception {
		devSleep(3200, 2400);
		if (s.ss.specialWeapon != null && s.combat.getSpecialPercentage() >= s.ss.specialWeapon.getSpecAmount()) {
			if (s.inventory.contains(s.ss.specialWeapon.getName())) {
				s("Equipping " + s.ss.specialWeapon);
				if (s.inventory.getItem(s.ss.specialWeapon.getName()).interact("Wield")) {
					new ConditionalSleep(3500, 500) {
						@Override
						public boolean condition() throws InterruptedException {
							return s.equipment.isWieldingWeapon(s.ss.specialWeapon.getName());
						}
					}.sleep();
				}
			}
			if (s.equipment.isWieldingWeapon(s.ss.specialWeapon.getName())) {
				if (!s.combat.isSpecialActivated()) {
					s("Activating special");
					s.combat.toggleSpecialAttack(true);
					s.mouse.moveOutsideScreen();
					devSleep(3500, 1800);
				}
			}
		}
		if (s.ss.specialWeapon != null && s.combat.getSpecialPercentage() < s.ss.specialWeapon.getSpecAmount()) {
			if (s.equipment.isWieldingWeapon(s.ss.specialWeapon.getName())
					|| !s.equipment.isWieldingWeapon(s.ss.mainWeapon)) {
				if (s.inventory.contains(s.ss.mainWeapon)) {
					s("Re-equipping " + s.ss.mainWeapon);
					if (s.inventory.getItem(s.ss.mainWeapon).interact("Wield")) {
						new ConditionalSleep(5500, 500) {
							@Override
							public boolean condition() throws InterruptedException {
								return s.equipment.isWieldingWeapon(s.ss.mainWeapon);
							}
						}.sleep();
					}
				}
			}
		}
	}

}
