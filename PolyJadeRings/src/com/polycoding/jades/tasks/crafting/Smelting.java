package com.polycoding.jades.tasks.crafting;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;
import com.polycoding.jades.util.Timer;

public class Smelting extends Task {

	private PolyJadeRings s;

	public Smelting(PolyJadeRings s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() throws Exception {
		return s.is.edgeFurnace.contains(s.myPlayer()) && hasCraftingMaterials();

	}

	@Override
	public void execute() throws Exception {
		RS2Object furnace = s.objects.closest("Furnace");
		if (furnace != null && furnace.exists()) {
			s.logs("Smelting, yo");
			if (s.inventory.getItem("Silver bar").interact("Use")) {
				furnace.interact("Use");
				Timer t = new Timer(4500);
				int resc = 0;
				while (t.isRunning()) {
					Script.sleep(Script.random(250, 700));
					if (!s.widgets.isVisible(446)) {
						t.reset();
						resc++;
					}
					if (s.widgets.isVisible(446) || resc > 6)
						break;
				}
				RS2Widget jadeRingWidget = s.widgets.get(6, 4, 1);
				if (jadeRingWidget != null && jadeRingWidget.isVisible()) {
					jadeRingWidget.interact("Craft All");
					Script.sleep(Script.random(1000, 2000));
					Timer t1 = new Timer(9000);
					while (t1.isRunning()) {
						Script.sleep(Script.random(250, 700));
						if (s.myPlayer().getAnimation() != -1 || s.myPlayer().isMoving())
							t1.reset();
						if (!s.inventory.contains("Silver bar") || !s.inventory.contains("Jade")
								|| mustContinue())
							break;
					}
				}
			}
		}
	}

}
