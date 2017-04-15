package com.polycoding.nmz.tasks;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;

public class WalkToNMZ extends Task {

	public WalkToNMZ(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() throws Exception {
		return !isInDream() && !isInNMZArea() && !s.ss.hasEnteredDream;
	}

	@Override
	public void execute() throws Exception {
		logs("Walking to NMZ Area");
		s.walking.webWalk(s.ss.nmzOutsideArea);
	}

	@Override
	public String getName() {
		return "WalkToNMZ ";
	}

}
