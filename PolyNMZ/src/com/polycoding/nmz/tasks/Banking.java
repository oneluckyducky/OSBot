package com.polycoding.nmz.tasks;

import java.util.Arrays;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.enums.HealthDegrade;

public class Banking extends Task {

	public Banking(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() throws Exception {
		boolean a = s.ss.hp1 && !hasRockCake();
		boolean b = (s.ss.specialWeapon != null && !s.inventory.contains(s.ss.specialWeapon.getName())
				&& !s.equipment.isWieldingWeapon(s.ss.specialWeapon.getName()));
		boolean c = (!s.ss.bankPotionData.isEmpty() && !s.ss.hasBanked);
		boolean d = s.ss.isBlowpiping && s.equipment.isWieldingWeaponThatContains("oxic blowp") && s.ss.blowpipeCharges<=s.ss.blowpipeRechargePercent;
		log("A: " + a);
		log("B: " + b);
		log("C: " + c);
		return a || b || c;
	}

	@Override
	public void execute() throws Exception {
		RS2Object booth = s.objects.getAll().stream()
				.filter(ob -> ob != null && ob.getName().contains("booth") && ob.hasAction("Bank") && isInBankArea(ob))
				.findAny().get();
		if (!isInBankArea()) {
			logs("Walking to bank");
			s.walking.webWalk(s.ss.bankTile);
		}
		if (booth == null)
			return;

		if (s.myPlayer().isMoving())
			s.camera.toEntity(booth);
		logs("Getting supplies");
		if (!s.bank.isOpen()) {
			if (booth.interact("Bank")) {
				new ConditionalSleep(15000, 500) {

					@Override
					public boolean condition() throws InterruptedException {
						return s.bank.isOpen();
					}

				}.sleep();
			}
		}
		if (s.bank.isOpen()) {
			if (s.ss.hp1 && !hasRockCake()) {
				if (s.bank.contains("Dwarven rock cake") && !s.inventory.isFull()) {
					if (s.bank.withdraw("Dwarven rock cake", 1)) {
						new ConditionalSleep(5000, 500) {

							@Override
							public boolean condition() throws InterruptedException {
								return s.inventory.contains("Dwarven rock cake");
							}

						}.sleep();
					}
				} else {
					if (s.prayer.hasLevelFor(PrayerButton.RAPID_HEAL)) {
						logs("Either don't have rock cake or inventory is full, prayer flicking to keep HP at 1");
						s.ss.healthDegrade = HealthDegrade.PRAYER_FLICKING;
					} else {
						s.stopWithMessage("Either don't have rock cake or inventory is full, also can't flick prayer",
								true);
					}
				}
			}
			if (!s.ss.bankPotionData.isEmpty() && !s.ss.hasBanked) {
				s.ss.bankPotionData.keySet().stream().sorted((s1, s2) -> s2.compareTo(s1)).forEach(p -> {
					if (s.inventory.contains(p.getNames())) {
						if (s.bank.depositAll(p.getNames())) {
							new ConditionalSleep(5500, 500) {
								@Override
								public boolean condition() throws InterruptedException {
									return !s.inventory.contains(p.getNames());
								}
							}.sleep();
						}
					}
					if (s.bank.contains(p.getNames())) {
						Filter<Item> itf = it -> it != null && Arrays.asList(p.getNames()).contains(it.getName())
								&& s.bank.contains(it.getName());
						log("Gotta get " + p.getNames()[0]);
						if (s.bank.withdraw(itf, s.ss.bankPotionData.get(p))) {
							new ConditionalSleep(5500, 500) {
								@Override
								public boolean condition() throws InterruptedException {
									return s.inventory.contains(p.getNames());
								}
							}.sleep();
						}
					}
				});
				s.ss.hasBanked = true;
			}
			if (s.ss.isBlowpiping && s.ss.blowpipeCharges < 25.0) {

			}
		}
		s.walking.webWalk(s.ss.nmzTile);
	}

	@Override
	public String getName() {
		return "Banking ";
	}

}
