package com.polycoding.wizardkiller.tasks.traverse;

import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;
import com.polycoding.wizardkiller.util.XWalking;
import com.polycoding.wizardkiller.util.enums.Destination;

public class ToWizards extends Node {

	public ToWizards(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Walking to wizards";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return !WIZARD_AREA.contains(script.myPlayer())
				&& !isInCombat(script.myPlayer()) && (methods.attr.is("banking") ? script.inventory
				.contains(methods.attr.getInt("food")) : true);
	}

	@Override
	public void execute() throws InterruptedException {
		XWalking.walkPath(script, Destination.WIZARDS.getRandomPath());
	}

}
