package com.polycoding.wizardkiller.tasks.banking;

import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;

public class CloseBank extends Node {

	public CloseBank(Script script, MyProvider methods) {
		super(script, methods);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String status() {
		return "Closing bank";
	}

	@Override
	public boolean validate() throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}

}
