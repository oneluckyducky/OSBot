package com.polycoding.jades.tasks.crafting;

import org.osbot.rs07.script.Script;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;
import com.polycoding.jades.util.Timer;

public class GBanking extends Task {
	private PolyJadeRings s;

	public GBanking(PolyJadeRings s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() throws Exception {
		return !hasCraftingMaterials() && isInEdgeBank();
	}

	@Override
	public void execute() throws Exception {
		if (!s.bank.isOpen()) {
			s.bank.open();
			Timer t = new Timer(2000);
			int resCount = 0;
			while (t.isRunning()) {
				if (s.bank.isOpen() || resCount >= 5)
					break;
				if (!s.bank.isOpen()) {
					resCount++;
					t.reset();
				}
			}
		}

		if (s.bank.isOpen()) {
			Script.sleep(Script.random(250, 500));
			if (!s.inventory.isEmpty()) {
				s.bank.depositAll();
				checkActiveStates();
			}
			if (!s.inventory.isFull()) {
				Script.sleep(Script.random(650, 1150));
				if (!s.inventory.contains("Ring mould")) {
					if (!s.bank.contains("Ring mould")) {
						s.stopWithMessage("No Ring mould found, stopping script.");
					}
					s.logs("Have to withdraw mould");
					s.bank.withdraw("Ring mould", 1);
					Timer t = new Timer(2000);
					int resCount = 0;
					while (t.isRunning()) {
						if (s.inventory.contains("Ring mould") || resCount >= 5)
							break;
						if (!s.inventory.contains("Ring mould")) {
							resCount++;
							t.reset();
						}
					}
				}
				if (!s.inventory.contains("Silver bar")) {
					if (!s.bank.contains("Silver bar") || !s.bank.contains("Jade")) {
						s.stopWithMessage("Out of material, stopping script.");
					}
					s.logs("Have to withdraw Silver bars");
					s.bank.withdraw("Silver bar", 13);
					Timer t = new Timer(2000);
					int resCount = 0;
					while (t.isRunning()) {
						if (s.inventory.contains("Silver bar") || resCount >= 5)
							break;
						if (!s.inventory.contains("Silver bar")) {
							resCount++;
							t.reset();
						}
					}
					s.logs("Have to withdraw Jade");
					s.bank.withdraw("Jade", 13);
					t = new Timer(2000);
					resCount = 0;
					while (t.isRunning()) {
						if (s.inventory.contains("Jade") || resCount >= 5)
							break;
						if (!s.inventory.contains("Jade")) {
							resCount++;
							t.reset();
						}
					}
				}
			}

		}
	}

}
