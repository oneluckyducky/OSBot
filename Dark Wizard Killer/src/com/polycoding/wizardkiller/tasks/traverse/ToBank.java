package com.polycoding.wizardkiller.tasks.traverse;

import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;
import com.polycoding.wizardkiller.util.XWalking;
import com.polycoding.wizardkiller.util.enums.Destination;

public class ToBank extends Node {

	public ToBank(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Walking to bank";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return !BANK_AREA.contains(script.myPlayer())
				&& methods.attr.is("banking")
				&& !script.inventory.contains(FOOD_NAMES);
	}

	@Override
	public void execute() throws InterruptedException {
		XWalking.walkPath(script, Destination.BANK.getRandomPath());
	}

}
