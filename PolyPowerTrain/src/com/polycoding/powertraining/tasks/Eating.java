package com.polycoding.powertraining.tasks;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;

public class Eating extends Task {

	private PolycodingPowerTrain s;
	boolean sa = false;

	public Eating(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() {
		return hasFood() && s.myPlayer().getHealthPercent() <= s.is.eatPercent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception {
		//logs("Eat execute start");
		if (!Tab.INVENTORY.isOpen(s.getBot()))
			s.tabs.open(Tab.INVENTORY);
		Item food = s.inventory.getItem(f -> f != null && f.hasAction("Eat"));
		//logs("Eat execute start 2");

		int hp = s.skills.getDynamic(Skill.HITPOINTS);
		//logs("Eat execute start 3");
		logs(String.format("Health at %s, eating...", hp));

	//	logs("Eat execute start 4");
		if (food.interact("Eat")) {
			new ConditionalSleep(4500, 500) {

				@Override
				public boolean condition() throws InterruptedException {
					return s.skills.getDynamic(Skill.HITPOINTS) > hp;
				}

			}.sleep();
		} else {
			logs("Eat execute failed interact");
		}
	}

}
