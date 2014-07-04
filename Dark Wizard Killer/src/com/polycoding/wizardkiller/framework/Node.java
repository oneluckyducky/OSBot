package com.polycoding.wizardkiller.framework;

import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

public abstract class Node extends MyProvider {

	public Script script;
	public MyProvider methods;
	public Data data;

	public Node(Script script, MyProvider methods) {
		super(script);
		this.script = script;
		this.methods = methods;
		this.data = methods.data();
	}

	public void sleep(int min, int max) throws InterruptedException {
		MethodProvider.sleep(MethodProvider.random(min, max));
	}

	public int rand(int min, int max) throws InterruptedException {
		return MethodProvider.random(min, max);
	}

	public abstract String status();

	public abstract boolean validate() throws InterruptedException;

	public abstract void execute() throws InterruptedException;

}
