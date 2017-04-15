package com.polycoding.powertraining.tasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;

public class SpecialAttacking extends Task {

	PolycodingPowerTrain s;

	public SpecialAttacking(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() throws Exception {
		return s.is.specialWeapon != null && s.combat.getSpecialPercentage() >= s.is.specialWeapon.getSpecAmount()
				&& !s.combat.isSpecialActivated() && Script.random(0, 10) % 2 == 0;
	}

	@Override
	public void execute() throws Exception {
		Script.sleep(Script.random(0, 7500));
		if (s.is.specialWeapon != null && s.combat.getSpecialPercentage() >= s.is.specialWeapon.getSpecAmount()) {
			if (s.inventory.contains(s.is.specialWeapon.getName())) {
				logs("Equipping " + s.is.specialWeapon);
				if (s.inventory.getItem(s.is.specialWeapon.getName()).interact("Wield")) {
					new ConditionalSleep(3500, 500) {
						@Override
						public boolean condition() throws InterruptedException {
							return s.equipment.isWieldingWeapon(s.is.specialWeapon.getName());
						}
					}.sleep();
				}
			}
			if (s.equipment.isWieldingWeapon(s.is.specialWeapon.getName())) {
				if (!s.combat.isSpecialActivated()) {
					logs("Activating special");
					s.combat.toggleSpecialAttack(true);
					s.mouse.moveOutsideScreen();
					Script.sleep(Script.random(2400, 4900));
				}
			}
		}
		if (s.is.specialWeapon != null && s.combat.getSpecialPercentage() < s.is.specialWeapon.getSpecAmount()) {
			if (s.equipment.isWieldingWeapon(s.is.specialWeapon.getName())
					|| !s.equipment.isWieldingWeapon(s.is.mainWeapon)) {
				if (s.inventory.contains(s.is.mainWeapon)) {
					logs("Re-equipping " + s.is.mainWeapon);
					if (s.inventory.getItem(s.is.mainWeapon).interact("Wield")) {
						new ConditionalSleep(5500, 500) {
							@Override
							public boolean condition() throws InterruptedException {
								return s.equipment.isWieldingWeapon(s.is.mainWeapon);
							}
						}.sleep();
					}
				}
			}
		}
	}

}
