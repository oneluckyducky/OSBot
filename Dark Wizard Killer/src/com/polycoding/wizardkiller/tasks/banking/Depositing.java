package com.polycoding.wizardkiller.tasks.banking;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;

public class Depositing extends Node {

	public Depositing(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Depositing inventory";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return BANK_AREA.contains(script.myPlayer()) && script.bank.isOpen()
				&& methods.attr.is("banking") && !script.inventory.isEmpty();
	}

	@Override
	public void execute() throws InterruptedException {
		if (script.bank.isOpen()) {
			if (script.bank.depositAll())
				new ConditionalSleep(600) {
					@Override
					public boolean condition() throws InterruptedException {
						return script.inventory.isEmpty();
					}
				}.sleep();
		}

	}

}
