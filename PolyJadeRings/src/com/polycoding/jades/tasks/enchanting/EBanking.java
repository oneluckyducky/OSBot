package com.polycoding.jades.tasks.enchanting;

import org.osbot.rs07.script.Script;

import com.polycoding.jades.PolyJadeRings;
import com.polycoding.jades.core.Task;

public class EBanking extends Task {

	public EBanking(PolyJadeRings s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() throws Exception {
		return !hasEnchantingMaterials() && isInEdgeBank();
	}

	@Override
	public void execute() throws Exception {
		if (!s.bank.isOpen()) {
			s.bank.open();
			Script.sleep(Script.random(700, 1600));
		}
		if (s.bank.isOpen()) {
			s.bank.depositAll();
			checkActiveStates();
			Script.sleep(Script.random(600, 1500));
			if (!s.equipment.contains("Staff of air") || !s.inventory.contains("Staff of air")) {
				if (s.bank.contains("Staff of air")) {
					s.logs("Withdrawing Staff of air");
					s.bank.withdraw("Staff of air", 1);
					Script.sleep(Script.random(600, 1600));
				} else {
					s.stopWithMessage("Can't enchant, no Staff of air");
				}
			}
			if (!s.inventory.contains("Cosmic rune")) {
				s.logs("Withdrawing Cosmic runes");
				if (s.bank.contains("Cosmic rune")) {
					s.bank.withdrawAll("Cosmic rune");
					Script.sleep(Script.random(700, 1700));
				} else {
					s.stopWithMessage("Can't enchant, no Cosmic runes");
				}
			}
			if (s.bank.contains("Jade ring")) {
				s.logs("Withdrawing rings");
				s.bank.withdrawAll("Jade ring");
				Script.sleep(Script.random(600, 1700));
			} else {
				s.stopWithMessage("Can't enchant, no Jade rings left");
			}
		}

	}

}
