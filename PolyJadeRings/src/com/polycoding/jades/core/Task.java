package com.polycoding.jades.core;

import com.polycoding.jades.PolyJadeRings;

public abstract class Task extends TaskMethodProvider {

	protected PolyJadeRings s;

	public Task(PolyJadeRings s) {
		super(s);
		this.s = s;
	}

	public abstract boolean validate() throws Exception;

	public abstract void execute() throws Exception;

}
