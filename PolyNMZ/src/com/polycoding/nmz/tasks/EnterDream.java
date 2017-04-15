package com.polycoding.nmz.tasks;

import java.util.function.Predicate;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;

public class EnterDream extends Task {

	private Predicate<RS2Object> potionFilter = ob -> ob != null && ob.getName().contains("otion")
			&& !ob.getName().contains("Spectator") && ob.hasAction("Drink");

	public EnterDream(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
	}

	@Override
	public boolean validate() throws Exception {
		if (s.ss.hp1 && !hasRockCake()) {
			log("Can't enter, no rock cake");
			return false;
		}
		boolean retVal = !isInDream() && isInNMZArea() && s.ss.hasSetDream && s.ss.hasGetPotion;
		log("enter: " + retVal);
		return retVal;
	}

	@Override
	public void execute() throws Exception {
		log("enter exec");
		if (hasRockCake() && hp() > 51 && s.ss.hp1) {
			do {

				if (s.inventory.getItem("Dwarven rock cake").interact(hp() - 51 > 10 ? "Guzzle" : "Eat")) {
					devSleep(700, 150);
				}
			} while (hp() > 51);
		}
		RS2Object potion = s.objects.getAll().stream().filter(potionFilter).findFirst().get();
		log(potion == null ? "pot null" : "pot not null");
		if (potion.interact("Drink")) {
			new ConditionalSleep(5000, 600) {

				@Override
				public boolean condition() throws InterruptedException {
					return s.widgets.getWidgetContainingText("Accept") != null;
				}

			}.sleep();
			RS2Widget accept = s.widgets.getWidgetContainingText("Accept");
			if (accept != null) {
				if (accept.interact("Continue")) {
					s.ss.hasEnteredDream = true;
					new ConditionalSleep(6000, 500) {
						@Override
						public boolean condition() throws InterruptedException {
							return isInDream();
						}
					}.sleep();
				}
			}

		}
	}

	@Override
	public String getName() {
		return "EnterDream ";
	}

}
