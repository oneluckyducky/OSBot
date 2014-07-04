package com.polycoding.wizardkiller.tasks.combat;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;
import com.polycoding.wizardkiller.util.Timer;

public class Attacking extends Node {

	NPC npc;

	public Attacking(Script script, MyProvider methods) {
		super(script, methods);
	}

	@Override
	public String status() {
		return "Attacking";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate() throws InterruptedException {
		npc = script.npcs.closest(new Filter<NPC>() {
			@Override
			public boolean match(NPC n) {
				return n.getName().equalsIgnoreCase("dark wizard")
						&& n.getLevel() == 7;
			}

		});
		return WIZARD_AREA.contains(script.myPlayer()) && npc != null
				&& getInteractingWith(script.myPlayer()).size() == 0
				&& !isInCombat(npc) && !isInCombat(script.myPlayer());
	}

	@Override
	public void execute() throws InterruptedException {
		script.log("Checking for loot");
		if (checkForLoot())
			return;
		if (equipArrows())
			return;

		if (npc != null) {
			script.log("Attacking npc");
			if (script.myPlayer().getPosition().distance(npc) > 5)
				script.localWalker.walk(npc, true);
			if (!npc.isVisible())
				script.camera.toEntity(npc, true);
			if (npc.interact("Attack")) {
				final Timer t = new Timer(600);
				while (t.isRunning()) {
					if (script.myPlayer().isMoving())
						t.reset();
					if (!t.isRunning() || script.myPlayer().isUnderAttack())
						break;
					sleep(300, 600);
				}
			}
		}
	}

	public boolean equipArrows() throws InterruptedException {
		Item item = script.inventory.getItem(String.valueOf(methods.attr
				.get("arrow")));
		if (item != null) {
			item.interact("Wield");
			sleep(600, 800);
			return true;
		}
		return false;
	}

	public boolean checkForLoot() throws InterruptedException {
		GroundItem gi = script.groundItems.closest(methods.itemsList
				.toArray(new String[itemsList.size()]));
		if (script.inventory.isFull() && script.inventory.contains(FOOD_NAMES)) {
			script.inventory.getItem(FOOD_NAMES).interact("Eat");
			sleep(600, 800);
		}
		if (gi != null && gi.exists()) {
			if (script.myPlayer().getPosition().distance(gi) > 5)
				script.localWalker.walk(gi, true);
			if (!gi.isVisible())
				script.camera.toEntity(gi, rand(0, 2) == 1 ? true : false);
			script.log("Taking " + gi.getName());
			if (gi.interact("Take")) {
				final Timer timer = new Timer(2400);
				while (gi.exists()) {
					if (script.myPlayer().isMoving())
						timer.reset();
					if (!timer.isRunning() || !gi.exists())
						break;
				}
				methods.profit += methods.prices.getItemPrice(gi.getName());
				return true;
			}
		}
		return false;
	}
}
