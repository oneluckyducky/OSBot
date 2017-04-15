package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;

public class AbsorptionHandling extends Task {

	public AbsorptionHandling(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "AbsorptionHandling ";
	}

	@Override
	public boolean validate() throws Exception {
		return s.inventory.contains(ABSORPTION_POTION) && getAbsorptionLeft() <= Script.gRandom(25, 100, 1.0);
	}

	@Override
	public void execute() throws Exception {
		s("Drinking Absorption");
		if (!s.dev) {
			do {
				if (!isInDream())
					break;
				if (interactItem("Drink", ABSORPTION_POTION)) {
					new ConditionalSleep(2500, 500) {
						@Override
						public boolean condition() throws InterruptedException {
							return getAbsorptionLeft() >= 25;
						}
					}.sleep();
					devSleep(800,200);
				}
			} while (getAbsorptionLeft() <= 950 && s.inventory.contains(ABSORPTION_POTION));
		} else {// me
			for (int i = 0; i < Script.random(2, 4); i++) {
				if (interactItem("Drink", ABSORPTION_POTION)) {
					new ConditionalSleep(2500, 500) {
						@Override
						public boolean condition() throws InterruptedException {
							return getAbsorptionLeft() >= 25;
						}
					}.sleep();
					devSleep(800,200);
				}
			}
		}
	}

}
