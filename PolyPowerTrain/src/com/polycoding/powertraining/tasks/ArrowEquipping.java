package com.polycoding.powertraining.tasks;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;

public class ArrowEquipping extends Task {

	private PolycodingPowerTrain s;

	public ArrowEquipping(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() {
		return s.inventory.contains(s.is.ammoName)
				&& s.inventory.getItem(s.is.ammoName).getAmount() >= 5;
	}

	@Override
	public void execute() throws InterruptedException {
		s.status = "Equipping ammo";
		Item ammo = s.inventory.getItem(s.is.ammoName);
		if (ammo.interact("Wield")) {
			Script.sleep(600);
		}
	}

}
