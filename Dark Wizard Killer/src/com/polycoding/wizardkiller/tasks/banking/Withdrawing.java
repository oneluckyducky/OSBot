package com.polycoding.wizardkiller.tasks.banking;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;

public class Withdrawing extends Node {

	public Withdrawing(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Withdrawing food";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return BANK_AREA.contains(script.myPlayer()) && script.bank.isOpen()
				&& methods.attr.is("banking") && script.inventory.isEmpty();
	}

	@Override
	public void execute() throws InterruptedException {
		final int foodId = methods.attr.getInt("food");
		final byte amount = methods.attr.getByte("foodAmount");
		if (script.bank.isOpen()) {
			if (script.bank.contains(foodId)){
				if(script.bank.withdraw(foodId, amount))
				new ConditionalSleep(600) {
					@Override
					public boolean condition() throws InterruptedException {
						return script.inventory.contains(foodId);
					}
				}.sleep();
			} else {
				script.log("Out of food, logging out!");
				script.stop();
			}
		}
	}
}
