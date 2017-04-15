package com.polycoding.powertraining.tasks;

import java.util.List;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;
import com.polycoding.powertraining.util.Timer;

@SuppressWarnings("unchecked")
public class Looting extends Task {

	private List<GroundItem> lootFilter = null;

	private PolycodingPowerTrain s;

	public Looting(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() {
		lootFilter = s.groundItems.filter(new Filter<GroundItem>() {
			@Override
			public boolean match(GroundItem g) {
				return g != null && s.is.itemList.contains(g.getName().toUpperCase()) && s.map.canReach(g)
						&& g.getPosition().distance(s.startPosition) <= s.is.maxDistance;
			}
		});

		return !s.myPlayer().isUnderAttack() && !s.myPlayer().isAnimating() && s.myPlayer().getInteracting() == null
				&& !lootFilter.isEmpty();
	}

	@Override
	public void execute() throws Exception {
		final GroundItem g = s.groundItems.closest(lootFilter);
		// if(s.inventory.contains(s.alchys) && s.){

		if (!g.isOnScreen()) {
			logs("Turning camera to loot");
			s.camera.toEntity(g);
		}
		if (s.inventory.isFull() && s.inventory.contains(g.getName()) && !s.inventory.getItem(g.getName()).isNote()) {
			if (g.getName().toLowerCase().contains("bone")) {
				s.burying.execute();
				return;
			}
		}
		if (Script.random(2200) % 130 == 0) {
			if (s.mouse.moveOutsideScreen()) {
				Timer pause = new Timer(Script.random(4000, 9000));
				while (pause.isRunning())
					Script.sleep(10);

			}
		}
		logs("Looting " + g.getName() + " x" + g.getAmount());
		if (g.interact("Take")) {
			new ConditionalSleep(1300) {
				@Override
				public boolean condition() throws InterruptedException {
					return g == null && !g.isOnScreen() && !g.exists();
				}

			}.sleep();
			if (g.getName().equalsIgnoreCase(s.is.bonesName) || g.getName().equalsIgnoreCase(s.is.ammoName)) {
				s.is.profit += 0;
			} else {
				s.is.profit += Integer.parseInt(prices.getPriceById(g.getId())) * g.getAmount();
			}
		}
	}

}
