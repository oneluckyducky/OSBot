package com.polycoding.nmz.tasks.dreamtasks;

import java.util.stream.Stream;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.script.Script;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.Timer;

public class CombatTimer extends Task {

	public CombatTimer(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "CombatTimer ";
	}

	@Override
	public boolean validate() throws Exception {
		return !s.combat.isFighting() && !s.combatTimer.isRunning() && isInDream();
	}

	@Override
	public void execute() throws Exception {
		Stream<NPC> stream = s.npcs.getAll().stream().filter(n -> n != null && n.isAttackable())
				.sorted((n1, n2) -> Integer.compare(n1.getLevel(), n2.getLevel()));
		stream.forEach(n -> log(n.getName() + " -> " + n.getLevel()));
		NPC npc = null;
		if (stream.findFirst().get() != null)
			npc = stream.findFirst().get();
		else
			return;
		log("Haven't found in a bit, attacking " + npc.getName());
		if (npc != null)
			npc.interact("Attack");
		s.combatTimer = new Timer(
				Script.random(Script.random(25000, 45000), Script.random(56000, 123000)));
		s.mouse.moveOutsideScreen();

	}

}
