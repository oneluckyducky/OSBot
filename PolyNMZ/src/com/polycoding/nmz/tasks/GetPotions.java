package com.polycoding.nmz.tasks;

import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.enums.HealthDegrade;
import com.polycoding.nmz.util.enums.PotionData;

public class GetPotions extends Task {

	private boolean hasCheckedChest = false;

	private List<String> cantPots = new ArrayList<String>();

	public GetPotions(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
	}

	@Override
	public boolean validate() throws Exception {
		if (s.ss.healthDegrade == HealthDegrade.DWARVEN_ROCK_CAKE && !hasRockCake()) {
			return false;
		}
		return !isInDream() && isInNMZArea() && !s.ss.hasGetPotion && !s.ss.potionsData.isEmpty();
	}

	@Override
	public void execute() throws Exception {
		handleInventoryPotions();

		RS2Object chest = s.objects.getAll().stream()
				.filter(ob -> ob != null && ob.hasAction("Search") && ob.getName().equalsIgnoreCase("Rewards chest"))
				.findFirst().get();
		if (s.map.realDistance(s.ss.nmzTile) >= 10) {
			s.walking.webWalk(s.ss.nmzTile);
		}
		RS2Widget rewardShop = s.widgets.getWidgetContainingText("Reward Shop");
		if (rewardShop == null || !rewardShop.isVisible()) {
			logs("Opening rewards chest");
			if (chest.interact("Search")) {
				Script.sleep(1500);
				new ConditionalSleep(12000, 500) {
					@Override
					public boolean condition() throws InterruptedException {
						return rewardShop != null || rewardShop.isVisible();
					}

				}.sleep();
			}
		}
		if (rewardShop != null || rewardShop.isVisible()) {
			if (s.widgets.getWidgetContainingText("Benefits").interact("Benefits")) {
				Script.sleep(Script.random(1200, 2500));
			}
			s.ss.potionsData.keySet().stream().forEach(pot -> {
				String potName = pot.getStoreName().trim();
				log(String.format("Need: %s x%s", potName, s.ss.potionsData.get(pot)));
				RS2Widget potWidgets = s.widgets.get(PotionData.getRootId(), PotionData.getChildId());
				if (potWidgets != null) {
					int curAmount = Integer.parseInt(potWidgets.getChildWidget(pot.getQuantityWidgetId()).getMessage()
							.replace("(", "").replace(")", ""));
					if (curAmount >= s.ss.potionsData.get(pot))
						return;
					RS2Widget buyingWidget = potWidgets.getChildWidget(pot.getBuyingWidgetId());
					int needAmount = s.ss.potionsData.get(pot) - curAmount;
					if (getDreamPoints() < needAmount * pot.getPrice()) {
						needAmount = Math.floorDiv(getDreamPoints(), pot.getPrice());
					}
					if (needAmount == 0) {
						if (!cantPots.contains(potName))
							cantPots.add(potName);
						logs("Can't buy " + potName + ", must be low on points.");
						return;
					}

					if (needAmount > 0 && buyingWidget.interact("Buy-X")) {
						logs(String.format("Buying %s :-> %s", potName, needAmount));
						new ConditionalSleep(5500, 500) {

							@Override
							public boolean condition() throws InterruptedException {
								return isAmountPromptVisible();
							}

						}.sleep();
						try {
							Script.sleep(Script.random(800, 1800));
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						if (s.keyboard.typeString(String.valueOf(needAmount))) {
							try {
								Script.sleep(Script.random(1200, 1600));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				} else
					log("potwidgets null");
			});
			hasCheckedChest = true;
		}
		if (hasCheckedChest) {
			s.ss.potionsData.keySet().stream().forEach(pot -> {
				if (cantPots.contains(pot.getStoreName()))
					return;
				logs(String.format("Getting %s from barrel", pot.getStoreName()));
				try {
					takePotionsFromBarrel(pot.getStoreName().replace("Super ", "").substring(1, 4),
							s.ss.potionsData.get(pot));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			s.ss.hasGetPotion = true;
		}
	}

	private void handleInventoryPotions() throws Exception {
		RS2Object barrel = null;
		if (s.inventory.contains(ABSORPTION_POTION)) {
			barrel = s.objects.getAll().stream()
					.filter(b -> b != null && b.getName().contains("bsorp") && b.hasAction("Store")).findFirst().get();
			if (barrel.interact("Store")) {
				new ConditionalSleep(15000, 1000) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.dialogues.isPendingOption();
					}
				}.sleep();
				s.dialogues.selectOption(1);
				devSleep(2000, 1200);
			}
		}
		if (s.inventory.contains(OVERLOAD_POTION)) {
			barrel = s.objects.getAll().stream()
					.filter(b -> b != null && b.getName().contains("verload") && b.hasAction("Store")).findFirst()
					.get();
			if (barrel.interact("Store")) {
				new ConditionalSleep(15000, 1000) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.dialogues.isPendingOption();
					}
				}.sleep();
				s.dialogues.selectOption(1);
				devSleep(2400, 800);
			}
		}
		if (s.inventory.contains(SUPER_MAGE_POTION)) {
			barrel = s.objects.getAll().stream()
					.filter(b -> b != null && b.getName().contains("mag") && b.hasAction("Store")).findFirst().get();
			if (barrel.interact("Store")) {
				new ConditionalSleep(15000, 1000) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.dialogues.isPendingOption();
					}
				}.sleep();
				s.dialogues.selectOption(1);
				devSleep(2100, 750);
			}
		}
		if (s.inventory.contains(SUPER_RANGE_POTION)) {
			barrel = s.objects.getAll().stream()
					.filter(b -> b != null && b.getName().contains("rang") && b.hasAction("Store")).findFirst().get();
			if (barrel.interact("Store")) {
				new ConditionalSleep(15000, 1000) {
					@Override
					public boolean condition() throws InterruptedException {
						return s.dialogues.isPendingOption();
					}
				}.sleep();
				s.dialogues.selectOption(1);
				devSleep(2200, 725);
			}
		}
	}

	@Override
	public String getName() {
		return "GetPotions ";
	}
}
