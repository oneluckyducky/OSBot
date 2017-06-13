package com.polycoding.jades.tasks.enchanting;

import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Spells.NormalSpells;
import org.osbot.rs07.script.Script;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;

public class EEnchanting extends Task {

	public EEnchanting(PolyJadeRings s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() throws Exception {
		return hasEnchantingMaterials();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception {
		if (s.bank.isOpen()) {
			s.logs("Closing bank");
			s.bank.close();
			Script.sleep(Script.random(600, 1700));
		}
		if (!s.equipment.contains("Staff of air") && s.inventory.contains("Staff of air")) {
			s.logs("Wielding staff");
			s.inventory.getItem("Staff of air").interact("Wield");
			Script.sleep(Script.random(700, 1600));
		}
		Item ring = s.inventory.getItem("Jade ring");
		if (s.magic.canCast(NormalSpells.LVL_2_ENCHANT) && ring != null) {
			s.logs("Enchanting Jade ring");
			s.magic.castSpell(NormalSpells.LVL_2_ENCHANT);
			Script.sleep(Script.random(200, 600));
			ring.interact("Cast");
			Script.sleep(Script.random(600, 1200));
		}
	}

}
