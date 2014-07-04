package com.polycoding.wizardkiller.tasks.banking;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;

public class OpenBank extends Node {

	public OpenBank(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Opening bank";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return BANK_AREA.contains(script.myPlayer())
				&& methods.attr.is("banking")
				&& !script.inventory.contains(FOOD_NAMES)
				&& !script.bank.isOpen();
	}

	@Override
	public void execute() throws InterruptedException {
		if (!script.bank.isOpen() && script.bank.open()) {
			new ConditionalSleep(600) {
				@Override
				public boolean condition() throws InterruptedException {
					return script.bank.isOpen();
				}
			}.sleep();
		}

	}
}
