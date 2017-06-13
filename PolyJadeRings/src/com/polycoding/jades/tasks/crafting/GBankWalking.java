package com.polycoding.jades.tasks.crafting;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;

public class GBankWalking extends Task {

	public GBankWalking(PolyJadeRings s) {
		super(s);
	}

	@Override
	public boolean validate() throws Exception {
		return !hasCraftingMaterials() && !isInEdgeBank();
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		s.logs("Walking to Edgeville bank");
		s.walking.webWalk(s.is.edgeBank);
	}

}
