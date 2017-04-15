package com.polycoding.nmz.tasks;

import java.util.function.Predicate;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;

public class SetupDream extends Task {

	public SetupDream(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	private Predicate<NPC> onionFilter = n -> n != null && isInNMZArea(n) && n.getName().contains("nion")
			&& n.hasAction("Dream");

	@Override
	public boolean validate() throws Exception {
		return !isInDream() && isInNMZArea() && !s.ss.hasSetDream && (s.ss.hasGetPotion || s.ss.potionsData.isEmpty());
	}

	@Override
	public void execute() throws Exception {
		NPC onion = s.npcs.getAll().stream().filter(onionFilter).findFirst().get();
		if (!s.dialogues.inDialogue() || s.widgets.getWidgetContainingText("come back another time") != null) {
			if (onion.interact("Dream")) {
				devSleep(2000, 500);
				new ConditionalSleep(10000, 1000) {
					@Override
					public boolean condition() throws InterruptedException {
						return !s.myPlayer().isMoving() || s.widgets.getWidgetContainingText("already created") != null
								|| s.widgets.getWidgetContainingText("dream would you like") != null;
					}
				}.sleep();
			}
		}
		if (s.widgets.getWidgetContainingText("Previous:") != null) {
			if (s.dialogues.selectOption(4)) {
				Script.sleep(Script.random(1400, 2000));
				if (s.dialogues.isPendingContinuation()) {
					if (s.dialogues.clickContinue()) {
						devSleep(2000, 750);
					}
				}
				if (s.widgets.getWidgetContainingText("Agree to pay") != null) {
					if (s.dialogues.selectOption(1))
						devSleep(1800, 350);
				}
				if (s.dialogues.isPendingContinuation()) {
					if (s.dialogues.clickContinue()) {
						devSleep(2000, 300);
						s.ss.hasSetDream = true;
						return;
					}
				}
			}
		}

		if (s.widgets.getWidgetContainingText("already created") != null) {
			devSleep(1750, 325);
			s.dialogues.completeDialogue("Click here to continue", "Yes, please cancel it.", "Click here to continue",
					"Click here to continue");
		}

	}

	@Override
	public String getName() {
		return "SetupDream ";
	}
}
