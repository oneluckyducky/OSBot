package com.polycoding.wizardkiller.tasks.combat;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;

public class Eating extends Node {

	public Eating(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Eating";
	}

	@Override
	public boolean validate() throws InterruptedException {
		return eat();
	}

	@Override
	public void execute() throws InterruptedException {
		script.log("Time to eat!");
	}
	
	
	public boolean eat() throws InterruptedException {
		Item food = script.inventory.getItem(FOOD_NAMES);
		if (getHealth() < methods.attr.getInt("eatAt") && food != null) {
			return food.interact("Eat");
		}
		return false;
	}
}
