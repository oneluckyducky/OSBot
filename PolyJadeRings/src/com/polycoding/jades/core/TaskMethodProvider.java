package com.polycoding.jades.core;

import java.util.Optional;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;

import com.polycoding.jades.InstanceSettings.ActiveState;
import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.util.PriceLookup;
import com.polycoding.jades.util.Timer;

public class TaskMethodProvider {

	protected PolyJadeRings s;

	protected PriceLookup prices;

	public TaskMethodProvider(PolyJadeRings s) {
		this.s = s;
		this.prices = new PriceLookup();
	}

	public void checkActiveStates() {
		if (!s.bank.isOpen())
			return;
		if (!s.canCraft() && s.canEnchant()) {
			s.is.activeState = ActiveState.ENCHANTING;
		} else if (s.canCraft()) {
			s.is.activeState = ActiveState.CRAFTING;
		}
	}

	public boolean mustContinue() {
		return s.getDialogues().isPendingContinuation();
	}

	public boolean isAmountPromptVisible() {
		Optional<RS2Widget> amountWidget = s.getWidgets().getAll().stream()
				.filter(widg -> widg.getMessage().contains("Enter amount")).findFirst();
		return amountWidget.isPresent() && amountWidget.get().isVisible();
	}

	public boolean isInEdgeBank() {
		return s.is.edgeBank.contains(s.myPlayer());
	}

	public boolean hasEnchantingMaterials() {
		return s.inventory.contains("Jade ring") && s.inventory.contains("Cosmic rune")
				&& s.equipment.contains("Staff of air");
	}

	public boolean hasCraftingMaterials() {
		return s.inventory.contains("Silver bar") && s.inventory.contains("Ring mould")
				&& s.inventory.contains("Jade");
	}

	@SuppressWarnings("unchecked")
	public boolean hasFood() {
		return !s.inventory.filter(new Filter<Item>() {
			@Override
			public boolean match(Item it) {
				return it.hasAction("Eat");
			}
		}).isEmpty();
	}

	public boolean customInteract(Entity i, String... actions) {
		if (i != null) {
			if (s.mouse.move(i.getX(), i.getY())) {
				return i.interact(actions);
			}
		}
		return false;
	}

	public void log(Object o) {
		s.log("[Polycoding]-> " + o.toString());
	}

	/**
	 * Console and paint status
	 * 
	 * @param o
	 */
	public void logs(Object o) {
		log(o);
		s.status = String.valueOf(o);
	}

	/**
	 * Sleeps until a condition or time has reached, has safety for while
	 * locking
	 * 
	 * @param timeout
	 *            Interval between checks in ms
	 * @param condition
	 *            Whatever will eval to true/false
	 * @param safetyTimeout
	 *            Maximum sleep time, regardless of condition
	 */
	public void dynamicSleep(int timeout, boolean condition, int safetyTimeout) {
		try {
			long waitTime = System.currentTimeMillis();
			Timer t = new Timer(timeout);
			while (t.isRunning()) {
				if (!condition)
					t.reset();
				if (System.currentTimeMillis() - waitTime >= safetyTimeout)
					break;
				Script.sleep(timeout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
