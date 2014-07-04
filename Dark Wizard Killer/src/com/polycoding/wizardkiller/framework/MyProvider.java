package com.polycoding.wizardkiller.framework;

import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

public class MyProvider extends Data {

	public final Script script;

	public MyProvider(Script script) {
		this.script = script;
	}

	public String status() {
		return status;
	}

	public Data data() {
		return this;
	}

	public boolean isInCombat(org.osbot.rs07.api.model.Character<?> c) {
		return c != null && c.isUnderAttack() && c.getInteracting() != null
				&& !c.isAttackable();
	}

	public List<NPC> getInteractingWith(org.osbot.rs07.api.model.Character<?> c) {
		List<NPC> npcs = new ArrayList<NPC>();
		for (NPC n : script.npcs.getAll()) {
			if (n != null && c != null && !n.getName().contains("dog")
					&& n.isInteracting(c)) {
				npcs.add(n);
			}
		}
		return npcs;
	}

	public int getSkillLevel(Skill skill) {
		return script.skills.getDynamic(skill);
	}

	public int getHealth() {
		return getSkillLevel(Skill.HITPOINTS);
	}

}
