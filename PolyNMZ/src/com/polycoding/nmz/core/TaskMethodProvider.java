package com.polycoding.nmz.core;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.util.enums.HealthDegrade;
import com.polycoding.nmz.util.enums.RegularPotionData;

public class TaskMethodProvider extends ConstantVars {

	protected PolyNMZ s;

	public TaskMethodProvider(PolyNMZ s) {
		this.s = s;
	}

	public boolean needsBankPots() {

		return false;
	}

	private int meleePotCount = 0;

	public boolean invHasAnySuperMeleePot() {
		Arrays.asList(RegularPotionData.values()).stream().forEach(pd -> {
			if (s.inventory.contains(pd.getNames()))
				meleePotCount++;
		});
		return meleePotCount > 0;
	}

	public boolean isPrayerFlicking() {
		return s.ss.healthDegrade == HealthDegrade.PRAYER_FLICKING;
	}

	public int getPrayerPoints() {
		return s.skills.getDynamic(Skill.PRAYER);
	}

	/**
	 * Gets absorption points left in NMZ
	 * 
	 * @return -1 if not in dream or if absorption widget is null
	 */
	public int getAbsorptionLeft() {
		if (!isInDream())
			return -1;
		RS2Widget absorption = s.widgets.getWidgetContainingText("bsorption");
		if (absorption == null)
			return 0;
		RS2Widget absNum = s.widgets.get(absorption.getRootId(), absorption.getSecondLevelId(),
				absorption.getThirdLevelId() + 1);
		if (absNum != null) {
			return Integer.parseInt(absNum.getMessage());
		}
		return -1;
	}

	public void s(Object o) {
		s.status = o.toString();
	}

	public boolean isAmountPromptVisible() {
		Optional<RS2Widget> amountWidget = s.getWidgets().getAll().stream().filter(
				widg -> widg.getMessage().contains("Enter amount") || widg.getMessage().contains("How many doses"))
				.findFirst();
		return amountWidget.isPresent() && amountWidget.get().isVisible();
	}

	public boolean interactItem(String action, String... names) {
		if (!s.inventory.contains(names))
			return false;
		Item item = s.inventory.getItem(names);
		RS2Widget owner = item.getOwner();
		if (!item.hasAction(action))
			return false;
		if (s.mouse.move(s.inventory.getMouseDestination(s.inventory.getSlot(item)))) {
			if (s.menu.getTooltip().contains(action)) {
				return s.mouse.click(s.mouse.getPosition().x, s.mouse.getPosition().y, false);
			} else
				item.interact(action);
		}
		return false;
	}

	public int hp() {
		return s.skills.getDynamic(Skill.HITPOINTS);
	}

	/**
	 * My ghetto ass way to not have to rewrite this crap
	 * 
	 * @param potName
	 *            Partial name of the potion (barrel name)
	 * @param quantity
	 *            Amount to take
	 * @param hasAction
	 *            Barrel has action "Take"
	 * @throws InterruptedException
	 */
	public void takePotionsFromBarrel(String potName, int quantity) throws Exception {
		RS2Object barrel = s.objects.getAll().stream()
				.filter(b -> b != null && b.getName().contains(potName) && b.hasAction("Take")).findFirst().get();
		if (barrel.interact("Take")) {
			new ConditionalSleep(25000, 500) {
				@Override
				public boolean condition() throws InterruptedException {
					return s.widgets.getWidgetContainingText(162, "many doses of") != null;
				}
			}.sleep();
			try {
				Script.sleep(Script.random(750, 1900));
				if (s.keyboard.typeString(String.valueOf(quantity))) {
					Script.sleep(Script.random(900, 1900));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isInBankArea(Entity entity) {
		return s.ss.bankArea.contains(entity);
	}

	public boolean isInBankArea() {
		return isInBankArea(s.myPlayer());
	}

	public boolean isInNMZArea(Entity entity) {
		return s.ss.nmzOutsideArea.contains(entity);
	}

	public boolean isInNMZArea() {
		return isInNMZArea(s.myPlayer());
	}

	public int getDreamPoints() {
		return s.configs.get(1060);
	}

	public boolean isInDream() {
		return s.configs.get(1021) != 0;
	}

	public boolean hasRockCake() {
		return s.inventory.contains("Dwarven rock cake");
	}

	public DreamType getDreamType() {
		switch (s.configs.get(1058)) {
		case 736768:
			return DreamType.NO_DREAM;
		case 766848:
			return DreamType.UNSET;
		case 766972:
			return DreamType.ENDURANCE_NORMAL;
		case 766973:
			return DreamType.ENDURANCE_HARD;
		case 766974:
			return DreamType.RUMBLE_NORMAL;
		case 766976:
			return DreamType.RUMBLE_HARD;
		default:
			return null;
		}
	}

	public void logs(Object o) {
		s.status = o.toString();
		s.log(o);
	}

	public void log(Object o) {
		s.log(o);
	}

	public enum DreamType {
		NO_DREAM, UNSET, ENDURANCE_NORMAL, ENDURANCE_HARD, RUMBLE_NORMAL, RUMBLE_HARD;
	}

	public void devSleep(long time, long dev) {
		try {
			Script.sleep(Math.round(nextDeviation(time, dev)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double nextDeviation(double mean, double stDev) {
		Random r = new Random();
		long val = Math.round(((r.nextGaussian() * (stDev / 3)) + mean));
		if (val < 0)
			return nextDeviation(mean, stDev);
		return val;
	}

}
