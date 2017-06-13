package com.polycoding.jades.tasks.crafting;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;

public class GWalking extends Task {

	public GWalking(PolyJadeRings s) {
		super(s);
	}

	@Override
	public boolean validate() throws Exception {
		return !s.is.edgeFurnace.contains(s.myPlayer()) && hasCraftingMaterials();
	}

	@Override

	public void execute() throws Exception {
		s.logs("Walking to Edgeville furnace..");
		s.walking.webWalk(s.is.edgeFurnace);
	}

}
