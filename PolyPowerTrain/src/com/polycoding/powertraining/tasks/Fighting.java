package com.polycoding.powertraining.tasks;

import java.util.List;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;
import com.polycoding.powertraining.util.Timer;

@SuppressWarnings("unchecked")
public class Fighting extends Task {

	private PolycodingPowerTrain s;

	public List<NPC> npcFilter = null;

	public Fighting(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
		npcFilter = s.npcs.filter(new Filter<NPC>() {
			@Override
			public boolean match(NPC n) {
				return s.is.npcList.contains(n.getName().toUpperCase())
						&& !s.is.lvlList.contains(String.valueOf(n.getLevel())) && n.isAttackable()
						&& n.getPosition().distance(s.startPosition) < s.is.maxDistance && s.map.canReach(n);
			}
		});
	}

	@Override
	public boolean validate() {
		npcFilter = s.npcs.filter(new Filter<NPC>() {
			@Override
			public boolean match(NPC n) {
				return s.is.npcList.contains(n.getName().toUpperCase())
						&& !s.is.lvlList.contains(String.valueOf(n.getLevel())) && n.isAttackable()
						&& n.getPosition().distance(s.startPosition) < s.is.maxDistance && s.map.canReach(n);
			}
		});
		return s.myPlayer().getHealthPercent() > s.is.eatPercent && !s.myPlayer().isUnderAttack()
				&& !s.myPlayer().isAnimating() && s.myPlayer().getInteracting() == null && !npcFilter.isEmpty();
	}

	@Override
	public void execute() throws InterruptedException {
		s.targetNpc = s.npcs.closest(true, npcFilter);
		if (s.targetNpc == null) {
			logs("Closest npc null");
			return;
		}
		if (!s.targetNpc.isOnScreen()) {
			logs("Moving camera to npc");
			s.camera.toEntity(s.targetNpc);
		}
		if (s.targetNpc.getPosition().distance(s.myPosition()) >= 7) {
			logs("Getting closer to npc");
			s.walking.walk(s.targetNpc);
			if (Script.random(2500) % 250 == 0) {
				if (s.mouse.moveOutsideScreen()) {
					Timer pause = new Timer(Script.random(15000, 30000));
					while (pause.isRunning())
						Script.sleep(10);

				}
			}
		}
		logs("Attacking " + s.targetNpc.getName());
		if (s.inventory.isItemSelected())
			s.inventory.deselectItem();
		if (s.targetNpc.interact("Attack")) {
			new ConditionalSleep(4500, 500) {
				@Override
				public boolean condition() throws InterruptedException {
					return s.combat.isFighting();
				}
			}.sleep();
			if (s.combat.isFighting() || s.myPlayer().isUnderAttack()) {
				if (s.myPlayer().getHealthPercent() >= 50 && Script.random(100) % 10 == 0) {
					s.mouse.moveOutsideScreen();
				}
			}
		}

	}

}
