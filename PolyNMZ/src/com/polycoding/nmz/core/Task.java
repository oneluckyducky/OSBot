package com.polycoding.nmz.core;

import com.polycoding.nmz.PolyNMZ;

public abstract class Task extends TaskMethodProvider {

	public Task(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
	}

	public abstract String getName();

	public abstract boolean validate() throws Exception;

	public abstract void execute() throws Exception;

}
