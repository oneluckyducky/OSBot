package com.polycoding.powertraining;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.powertraining.core.Task;
import com.polycoding.powertraining.tasks.ArrowEquipping;
import com.polycoding.powertraining.tasks.Burying;
import com.polycoding.powertraining.tasks.Eating;
import com.polycoding.powertraining.tasks.Fighting;
import com.polycoding.powertraining.tasks.Looting;
import com.polycoding.powertraining.tasks.SpecialAttacking;
import com.polycoding.powertraining.ui.Gui;
import com.polycoding.powertraining.util.MiscUtils;

@ScriptManifest(author = "Polymorphism", info = "AIO-ish power trainer", logo = "", name = "Poly PowerTrainer", version = 1.43)

// 1.43 added checking for starting weapon, will re-equip if not wielding
// anything, stops if not have
public class PolycodingPowerTrain extends Script {

	public InstanceSettings is = null;
	private Gui gui = null;

	public boolean uiDone = false;

	private LinkedList<Task> taskList = new LinkedList<Task>();

	private long startExp = 0;
	public String status = "something";
	public String stopStatus = "";
	public long startTime = 0;
	private Font font = null;

	public NPC targetNpc = null;

	public Position startPosition = null;

	public Looting looting = null;
	public Eating eating = null;
	public Fighting fighting = null;
	public Burying burying = null;

	public String valuableLootName = "";

	public Area safeArea = null;
	public List<Position> safeTiles = null;
	public Position[] bounding = null;

	int sX = 0;
	int sY = 0;
	int sZ = 0;
	String antiban = "";
	int startingTick = 0;
	int lastEnergyTick = 0;

	String startingWeapon = "";

	public Filter<Item> combatPotFilter = cbp -> cbp != null && cbp.getName().contains("ombat pot");

	public void onStart() throws InterruptedException {
		String osbUser = getClient().getUsername();
		this.log("Starting up");

		if (equipment.isWearingItem(EquipmentSlot.WEAPON)) {
			startingWeapon = equipment.getItemInSlot(EquipmentSlot.WEAPON.slot).getName();
		}

		for (Skill skill : Skill.values()) {
			startExp += skills.getExperience(skill);
		}
		font = new Font("Arial", 0, 11);
		is = new InstanceSettings();
		looting = new Looting(this);
		eating = new Eating(this);
		fighting = new Fighting(this);
		burying = new Burying(this);
		gui = new Gui(this);
		gui.setVisible(true);
		status = "Waiting on ui";
		while (!uiDone) {
			// log("ui");
			if (!gui.isVisible() || uiDone)
				break;
			try {
				Script.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log("Burying bones at: " + is.buryAt);
		startPosition = myPosition();
		safeTiles = new ArrayList<Position>();
		sX = startPosition.getX();
		sY = startPosition.getY();
		sZ = startPosition.getZ();
		int gX = (sX + (is.maxDistance * 2) + 1);
		int gY = (sY + (is.maxDistance * 2) + 1);

		/**
		 * #getArea()?????
		 */
		safeArea = new Area(new Position(sX - is.maxDistance, sY - is.maxDistance, sZ),
				new Position(sX + is.maxDistance, sY + is.maxDistance, sZ));

		safeTiles = safeArea.getPositions();
		bounding = new Position[safeTiles.size()];
		for (int i = 0; i < safeTiles.size(); i++) {
			if (safeTiles.get(i).distance(startPosition) == is.maxDistance)
				bounding[i] = safeTiles.get(i);
		}

		if (is.lootBuryBones) {
			is.itemList.add(is.bonesName);
			taskList.add(burying);
		}
		if (is.specialWeapon != null) {
			taskList.add(new SpecialAttacking(this));
		}
		if (is.isRanging) {
			is.itemList.add(is.ammoName.toUpperCase());
			taskList.add(new ArrowEquipping(this));
		}

		if (!is.npcList.isEmpty()) {
			log("not empty, adding npcs");
			taskList.add(fighting);
		}

		startingTick = client.getCurrentTick();
		log("onStart done");
	}

	@Override
	public void onMessage(Message m) throws InterruptedException {

		String msg = m.getMessage().toLowerCase();
		if (m.getType() == Message.MessageType.GAME) {
			if (msg.contains("you just advanced")) {
				if (!Tab.SKILLS.isOpen(bot)) {
					tabs.open(Tab.SKILLS);
					for (Skill sk : Skill.values()) {
						if (msg.toUpperCase().contains(sk.toString())) {
							antiban = "Lv. up, checking next exp " + sk.toString();
							skills.hoverSkill(sk);
							sleep(random(600, 1600));
						}
					}
				}
			}
			if (is.lootValuables) {
				String tmpLootName = "";
				// working on valuable loot msg, have to replace 5x, 3x, etc
				// amounts
				if (msg.contains("aluable drop")) {
					// Will try out MethodProvider#stripFormatting() later this
					// evening
					// For now this works..
					tmpLootName = msg.replace("<col=ef1020>", "").replace("</col>", "").replaceFirst("(.{0,15})", "")
							.replaceFirst("(\\d x)", "").replaceFirst("\\(([^\\)]+)\\)", "").trim();
					log("DROP MESSAGE: " + tmpLootName);
					valuableLootName = tmpLootName;
				}
			}
			if (msg.contains("no ammo")) {
				stopWithMessage("Ran out of ammo, stopping for safety. Below are the runtime details.", true);

			}
		}
	}

	@Override
	public void onExit() {
		if (!stopStatus.trim().isEmpty()) {
			log(stopStatus);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onLoop() throws InterruptedException {
		try {

			if (!uiDone) {
				log("not done");
				return 1000;
			}
			if (!equipment.isWearingItem(EquipmentSlot.WEAPON)) {
				if (inventory.contains(startingWeapon)) {
					if (inventory.getItem(startingWeapon).interact("Wield")) {
						new ConditionalSleep(5000, 250) {
							@Override
							public boolean condition() throws InterruptedException {
								return equipment.isWieldingWeapon(startingWeapon);
							}
						}.sleep();
					}
				} else {
					this.stopWithMessage("Somehow you lost your starting weapon..Stopping script now", true);
				}
			}
			if (targetNpc == null) {
				log("npc null");
				targetNpc = npcs.closest(fighting.npcFilter);
			}

			boolean curValidate = false;
			if (!safeArea.contains(myPlayer()) && !myPlayer().isUnderAttack()) {
				status = "Returning to start location";
				walking.webWalk(safeArea.getRandomPosition());
			}
			curValidate = eating.validate();
			log(String.format("%s -> %s", "Eating", curValidate));
			if (curValidate) {
				eating.execute();
				return 200;
			}

			if (skills.getDynamic(Skill.PRAYER) < random(5, 10) || npcs.filter(fighting.npcFilter).isEmpty()) {
				log("Walking to ruins");
				if (walking.webWalk(new Position(3290, 3885, 0))) {
					log("Waiting for a bit..");
					new ConditionalSleep(20000, 500) {

						@Override
						public boolean condition() throws InterruptedException {
							return skills.getDynamic(Skill.PRAYER) >= 35 || myPlayer().getInteracting() != null;
						}

					}.sleep();
				}
			}

			if (!prayer.isActivated(PrayerButton.PROTECT_FROM_MELEE)
					&& skills.getDynamic(Skill.PRAYER) >= random(15, 20)) {
				log("Activating protect from melee");
				if (prayer.set(PrayerButton.PROTECT_FROM_MELEE, true)) {
					sleep(random(1200, 1800));
				}
			}

			if (inventory.contains(v -> v != null && v.getName().contains("ial"))) {
				inventory.dropAll(v -> v.getName().contains("ial"));
			}

			if (inventory.contains(this.combatPotFilter) && needToCombatPot()) {
				log("Super combat pot");
				if (inventory.interact("Drink", combatPotFilter)) {
					sleep(random(1200, 1800));
				}
			}

			if (!valuableLootName.isEmpty()) {
				if (!inventory.isFull()) {
					looting.logs("Attempting valuable loot");
					if (looting.explicitLoot(valuableLootName)) {
						valuableLootName = "";
					}
				}
			}
			if (!settings.isRunning()) {
				if (settings.getRunEnergy() >= 30 + random(5, 20)) {
					settings.setRunning(true);
				}
			}

			curValidate = looting.validate();
			log(String.format("%s -> %s", "Looting", curValidate));
			if (curValidate) {
				looting.execute();
				return 500;
			}

			curValidate = fighting.validate();
			log(String.format("%s -> %s", "Fighting", curValidate));
			if (curValidate) {
				fighting.execute();
			}

			if (is.stopOutOfFood && !eating.hasFood()) {
				taskList.removeAll(taskList);
				uiDone = false;
				stopWithMessage("Ran out of food, safety stopping.", true);
			}

			for (Task t : taskList) {
				curValidate = t.validate();
				log(String.format("%s -> %s", t.getClass().getSimpleName(), curValidate));
				if (curValidate)
					t.execute();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return random(950, 2300);
	}

	boolean needToCombatPot() {
		int a = skills.getDynamic(Skill.ATTACK);
		int s = skills.getDynamic(Skill.STRENGTH);
		int d = skills.getDynamic(Skill.DEFENCE);

		return a <= skills.getStatic(Skill.ATTACK) || s <= skills.getStatic(Skill.STRENGTH)
				|| d <= skills.getStatic(Skill.DEFENCE);
	}

	public void onPaint(Graphics2D g) {

		if (!uiDone)
			return;

		g.setColor(Color.RED);
		g.drawString("Ticks passed: " + (client.getCurrentTick() - startingTick), 15, 25);
		g.drawString("Game Clock MS: " + client.gameClockMs(), 15, 40);

		g.setColor(Color.WHITE);
		for (Position pp : bounding) {
			if (pp == null || pp.distance(startPosition) > is.maxDistance)
				continue;
			if (pp.isVisible(getBot()))
				g.drawPolygon(pp.getPolygon(getBot()));
		}

		for (NPC n : npcs.filter(fighting.npcFilter)) {
			if (n.getPosition().equals(targetNpc.getPosition()) || n == null)
				continue;
			if (is.npcList.contains(n.getName().toUpperCase()) && !is.lvlList.contains(String.valueOf(n.getLevel()))
					&& n.isOnScreen() && n.getInteracting() == null) {
				g.setColor(Color.GREEN);
				g.drawPolygon(n.getPosition().getPolygon(getBot()));
			}
		}
		if (targetNpc != null) {
			Position targetPosition = targetNpc.getPosition();
			g.setColor(Color.RED);
			g.drawPolygon(targetPosition.getPolygon(getBot()));
		}

		long tmpExp = 0;
		for (Skill skill : Skill.values()) {
			tmpExp += skills.getExperience(skill);
		}

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 340, 200, 160);

		g.setFont(this.font);
		if (is.lootValuables) {
			g.drawString("Current Valuable:" + valuableLootName, 10, 10);
		}
		g.setColor(Color.WHITE);
		g.drawString("Version: " + getVersion(), 585, 10);

		g.drawString("Status: " + status, 5, 365);
		g.drawString("Runtime: " + MiscUtils.getRuntimeFormat(System.currentTimeMillis() - startTime), 5, 380);
		g.drawString("Exp: " + purtify((tmpExp - startExp)), 5, 395);
		g.drawString("Exp/h: " + purtify(getPerHour(tmpExp - startExp, startTime)), 5, 410);
		if (!is.itemList.isEmpty() || is.lootValuables) {
			g.drawString("Profit: " + purtify(is.profit), 5, 435);
			g.drawString("Profit/h: " + purtify(getPerHour(is.profit, startTime)), 5, 4500);
		}
		drawMouse(g);
	}

	public void stopWithMessage(String msg, boolean dumpRunStats) {
		long tmpExp = 0;
		for (Skill skill : Skill.values()) {
			tmpExp += skills.getExperience(skill);
		}
		stopStatus = "\n*****";
		stopStatus += msg;
		stopStatus += String.format("\nRuntime: %s\nExp Gained: %s\nProfit Gained: %s*****",
				MiscUtils.getRuntimeFormat(System.currentTimeMillis() - startTime), purtify((tmpExp - startExp)),
				purtify(is.profit));
		stop();
	}

	private void drawMouse(Graphics2D g) {
		g.setColor(Color.CYAN);
		Point p = mouse.getPosition();
		Dimension d = bot.getCanvas().getSize();
		g.drawLine(0, p.y, d.width, p.y);
		g.drawLine(p.x, 0, p.x, d.height);
	}

	public String purtify(long num) {
		return DecimalFormat.getInstance().format(num);
	}

	public long getPerHour(final long base, final long time) {
		return ((base) * 3600000 / (System.currentTimeMillis() - time));
	}
}
