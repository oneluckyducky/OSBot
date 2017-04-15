package com.polycoding.powertraining.core;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.util.PriceLookup;
import com.polycoding.powertraining.util.Timer;

public class TaskMethodProvider {

	private PolycodingPowerTrain s;

	protected PriceLookup prices;

	public TaskMethodProvider(PolycodingPowerTrain s) {
		this.s = s;
		this.prices = new PriceLookup();
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

	public boolean explicitLoot(String... names) throws Exception {
		final GroundItem g = s.groundItems.closest(names);
		if (!g.isOnScreen()) {
			logs("Turning camera to loot");
			s.camera.toTop();
			s.camera.toPosition(g.getPosition());
		}

		logs("Looting " + g.getName() + " x" + g.getAmount());
		if (g.interact("Take")) {
			new ConditionalSleep(1300) {
				@Override
				public boolean condition() throws InterruptedException {
					return g == null && !g.isOnScreen() && !g.exists();
				}

			}.sleep();
			if (g.getName().equalsIgnoreCase("bones") || g.getName().equalsIgnoreCase(s.is.ammoName)) {
				s.is.profit += 0;
			} else {
				s.is.profit += Integer.parseInt(prices.getPriceById(g.getId())) * g.getAmount();
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
	@Deprecated
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
