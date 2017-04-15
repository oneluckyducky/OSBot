package com.polycoding.powertraining.core;

import com.polycoding.powertraining.PolycodingPowerTrain;

public abstract class Task extends TaskMethodProvider {

	PolycodingPowerTrain s;

	public Task(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	public abstract boolean validate() throws Exception;

	public abstract void execute() throws Exception;

}
