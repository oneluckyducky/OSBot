package com.polycoding.nmz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Message.MessageType;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.api.util.Utilities;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.polycoding.nmz.core.ConstantVars;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.core.TaskMethodProvider;
import com.polycoding.nmz.tasks.Banking;
import com.polycoding.nmz.tasks.EnterDream;
import com.polycoding.nmz.tasks.GetPotions;
import com.polycoding.nmz.tasks.SetupDream;
import com.polycoding.nmz.tasks.WalkToNMZ;
import com.polycoding.nmz.tasks.dreamtasks.AbsorptionHandling;
import com.polycoding.nmz.tasks.dreamtasks.CombatTimer;
import com.polycoding.nmz.tasks.dreamtasks.OverloadHandling;
import com.polycoding.nmz.tasks.dreamtasks.PrayerFlicking;
import com.polycoding.nmz.tasks.dreamtasks.RockCaking;
import com.polycoding.nmz.tasks.dreamtasks.SpecialAttacking;
import com.polycoding.nmz.tasks.dreamtasks.SuperPotionHandling;
import com.polycoding.nmz.util.MiscUtils;
import com.polycoding.nmz.util.NMZArea;
import com.polycoding.nmz.util.StatSubmission;
import com.polycoding.nmz.util.Timer;
import com.polycoding.nmz.util.enums.HealthDegrade;
import com.polycoding.ui.Gui;

import javafx.scene.shape.Rectangle;

@ScriptManifest(author = "Polymorphism", info = "Does NMZ", logo = "https://avatars2.githubusercontent.com/u/15859406?v=3&s=180", name = "Poly NMZ2", version = 0.26)
public class PolyNMZ extends Script {

	// v0.25 - Added standard deviation to sleeps, also played with interactItem
	// method
	public boolean dev = false;

	public ScriptSettings ss = null;
	public TaskMethodProvider tm = null;

	private LinkedList<Task> tasks = new LinkedList<Task>();
	private LinkedList<Task> dreamTasks = new LinkedList<Task>();

	public long startTime = 0;
	public int startingTick = 0;
	public String status = "";
	public String antiban = "";

	public boolean uiDone = false;

	public Gui gui = null;

	private EnterDream enterDream = null;
	private SetupDream setupDream = null;
	private Banking banking = null;
	private GetPotions getPotions;
	private WalkToNMZ walkToNMZ;

	private SpecialAttacking specialAttacking = null;
	private RockCaking rockCaking = null;
	private CombatTimer cmbTimer = null;
	private PrayerFlicking prayerFlicking = null;
	private OverloadHandling overloadHandling = null;
	private AbsorptionHandling absorptionHandling = null;
	private SuperPotionHandling superPotHandling = null;

	Area tmpArea = null;

	Thread configThread = null;
	int lastGameConfig = -1;

	protected Rectangle hidePaintRect = new Rectangle(470, 339, 50, 30);
	protected boolean showPaint = true;

	public Color paintBackgroundColor = new Color(0, 0, 0, 200);

	public Thread statsThread = null;
	public StatSubmission statsRunnable = null;
	public Timer statsTimer = null;

	private WalkingEvent dreamWalker = null;
	public Timer rockCakeTimer = null;
	public Timer prayerFlickTimer = null;
	public Timer overloadTimer = null;
	public Timer combatTimer = null;;// used so that player wont get stuck by
										// big npc
	// like witchs bear

	private boolean verboseDebug = true;

	Robot rb = null; // doesn't work

	private BufferedImage hitpointsIcon = null;
	private BufferedImage overloadIcon = null;
	private BufferedImage rockCakeIcon = null;
	private BufferedImage combatIcon = null;

	public long lastMouseMove = 0;

	public void onStart() throws InterruptedException {
		log(String.format("Welcome, %s\nPolyNMZ v%s", client.getUsername(), getVersion()));

		widgets.get(548, 8).interact("Look North");

		bot.addMouseListener(new ScriptMouseListener(this));
		ss = new ScriptSettings();

		gui = new Gui(this);

		// configThread.start();
		gui.setVisible(true);
		try {
			log("downloading images");
			hitpointsIcon = ImageIO.read(new URL(
					"http://vignette3.wikia.nocookie.net/2007scape/images/9/96/Hitpoints_icon.png/revision/latest?cb=20141020205148"));
			overloadIcon = ImageIO.read(new URL("http://vignette2.wikia.nocookie.net/2007scape/"
					+ "images/8/82/Overload_%284%29.png/revision/latest?cb=20131104213506"));
			rockCakeIcon = ImageIO.read(new URL(
					"http://vignette4.wikia.nocookie.net/2007scape/images/6/69/Dwarven_rock_cake_cooled.png/revision/latest?cb=20131105173605"));
			combatIcon = ImageIO.read(new URL(
					"http://vignette4.wikia.nocookie.net/2007scape/images/9/9d/Multicombat.png/revision/latest?cb=20140130025700"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!uiDone) {
			if (!gui.isVisible())
				break;
			sleep(150);
		}
		log("Gui done");

		if (ss.isBlowpiping && !inventory.contains(ss.blowpipeItem) && !equipment.isWieldingWeapon(ss.blowpipeItem)) {
			log("Either equip or have blowpipe in inventory.");
			stop(false);
		}

		if (ss.potionsData.isEmpty()) {
			ss.hasGetPotion = true;
		}

		ss.bankArea = new Area(2608, 3097, 2618, 3087);
		ss.bankTile = new Position(2611, 3092, 0);
		ss.nmzTile = new Position(2609, 3115, 0);

		ss.nmzOutsideArea = new Area(
				new Position[] { new Position(2597, 3112, 0), new Position(2598, 3119, 0), new Position(2608, 3121, 0),
						new Position(2618, 3117, 0), new Position(2620, 3107, 0), new Position(2618, 3085, 0),
						new Position(2607, 3086, 0), new Position(2599, 3094, 0), new Position(2601, 3104, 0) });
		ss.nmzInsideArea = new Area(new Position[] { new Position(9952, 3383, 3), new Position(9983, 3352, 3) });

		tm = new TaskMethodProvider(this);
		enterDream = new EnterDream(this);
		setupDream = new SetupDream(this);
		banking = new Banking(this);
		getPotions = new GetPotions(this);
		walkToNMZ = new WalkToNMZ(this);

		specialAttacking = new SpecialAttacking(this);
		cmbTimer = new CombatTimer(this);
		rockCaking = new RockCaking(this);
		prayerFlicking = new PrayerFlicking(this);
		overloadHandling = new OverloadHandling(this);
		absorptionHandling = new AbsorptionHandling(this);
		superPotHandling = new SuperPotionHandling(this);

		ss.startPoints = tm.getDreamPoints();

		dreamWalker = new WalkingEvent();
		dreamWalker.setMinDistanceThreshold(3);
		dreamWalker.setMiniMapDistanceThreshold(3);

		startTime = System.currentTimeMillis();

		for (Skill skill : ss.trackedSkills) {
			log("Starting " + skill.toString());
			experienceTracker.start(skill);
		}
		tasks.add(banking);
		tasks.add(getPotions);
		tasks.add(enterDream);
		tasks.add(setupDream);
		tasks.add(walkToNMZ);

		dreamTasks.add(prayerFlicking);
		dreamTasks.add(absorptionHandling);
		dreamTasks.add(rockCaking);
		dreamTasks.add(overloadHandling);
		dreamTasks.add(specialAttacking);
		dreamTasks.add(cmbTimer);
		dreamTasks.add(superPotHandling);

		if (!combat.isAutoRetaliateOn())
			combat.toggleAutoRetaliate(true);

		startingTick = client.getCurrentTick();

		statsRunnable = new StatSubmission(this);
		statsTimer = new Timer(10000);

	}

	public void onMessage(Message msg) {

		if (msg.getType() == MessageType.GAME) {
			String m = msg.getMessage();
			if (m.contains("wake up feeling") && ss.only1Round && ss.rounds == 1) {
				stopWithMessage("1 round done.", true);
			}
		}
	}

	@Override
	public void onConfig(int id, int val) throws InterruptedException {
		if (id == 1021) {
			if (tm.isInDream()) {

				log("Setting dream positions");
				ss.dreamStartPos = myPlayer().getPosition();
				ss.dreamDynamicArea = new NMZArea(myPosition(), bot.getMethods());
				ss.dreamGoalPos = ss.dreamDynamicArea.getPosition(ss.afkLocation);
				ss.dreamCenterPos = ss.dreamDynamicArea.getCenter();
				ss.hasGetPotion = false;
				ss.hasSetDream = false;
				ss.hasBanked = false;
				ss.hasEnteredDream = true;
				ss.rounds++;
				if (overloadTimer == null && inventory.contains(ConstantVars.OVERLOAD_POTION)) {
					inventory.getItem(ConstantVars.OVERLOAD_POTION).interact("Drink");
					tm.devSleep(3000, 1000);
					overloadTimer = new Timer(300000 + Script.gRandom(0, 1, 0.9));
				}

				/*
				 * if (ss.healthDegrade == HealthDegrade.PRAYER_FLICKING) { if
				 * (prayerFlickTimer == null || !prayerFlickTimer.isRunning()) {
				 * prayer.set(PrayerButton.RAPID_HEAL, true); sleep(random(400,
				 * 800)); prayer.set(PrayerButton.RAPID_HEAL, false);
				 * prayerFlickTimer = new Timer(random(14000, 48000)); } }
				 */

				if (overloadTimer == null && inventory.contains(ConstantVars.OVERLOAD_POTION)) {
					inventory.getItem(ConstantVars.OVERLOAD_POTION).interact("Drink");
					tm.devSleep(3000, 1000);
					overloadTimer = new Timer(300000 + Script.gRandom(0, 1, 0.9));
				}
			} else {
				ss.hasEnteredDream = false;
				overloadTimer = null;
				prayerFlickTimer = null;
				combatTimer = null;
				ss.dreamCenterPos = null;
				ss.dreamStartPos = null;
				ss.dreamArea = null;
				ss.dreamDynamicArea = null;
				ss.dreamGoalPos = null;
				sleep(Math.round(tm.nextDeviation(45000, 10000)));
				// statsRunnable.sendStats(username, runtime, experience,
				// dreams, points);
			}
		}
	}

	public void onExit() {
		if (gui.isVisible())
			gui.dispose();
		Utilities.takeScreenshot();
		submitRuntimeStats();
	}

	public void submitRuntimeStats() {
		log("Submitting stats");
		long exp = 0;
		for (Skill skill : ss.trackedSkills) {
			if (experienceTracker.getGainedXP(skill) > 0) {
				exp += experienceTracker.getGainedXP(skill);
			}
		}
		statsRunnable.sendStats(client.getUsername(), String.valueOf((System.currentTimeMillis() - startTime) / 1000),
				String.valueOf(exp), String.valueOf(ss.rounds), String.valueOf(tm.getDreamPoints() - ss.startPoints));

		exp = 0;
	}

	@Override
	public int onLoop() throws InterruptedException {
		if (!combat.isAutoRetaliateOn())
			combat.toggleAutoRetaliate(true);

		if (tm.isInDream()) {
			widgets.closeOpenInterface();
			if (!tabs.getOpen().equals(Tab.INVENTORY)) {
				keyboard.pressKey(27);
			}
			if (tm.isPrayerFlicking() && tm.getPrayerPoints() == 0) {
				objects.getAll().stream().filter(obp -> obp != null && obp.getName().contains("pot")).findFirst().get()
						.interact("Drink");
				tm.devSleep(4000, 1000);
			}

			if (combatTimer == null) {
				combatTimer = new Timer(random(23000, 56000));
			}
			if (!combatTimer.isRunning()) {
				NPC nearestNpc = npcs.getAll().get(random(0, npcs.getAll().size() - 1));
				if (nearestNpc != null && map.canReach(nearestNpc)) {
					if (nearestNpc.interact("Attack")) {
						tm.devSleep(2200, 200);
						combatTimer = new Timer(random(27000, 48000));
					}
				}
			}
			if (combat.isFighting()) {
				combatTimer.reset();
			}

			if (map.realDistance(ss.dreamGoalPos) > ss.maxDistFromAfk) {
				walking.walk(ss.dreamGoalPos);
			}

			if (inventory.contains(ConstantVars.SUPER_RANGE_POTION)
					&& skills.getDynamic(Skill.RANGED) == skills.getStatic(Skill.RANGED)) {
				if (inventory.interact("Drink", ConstantVars.SUPER_RANGE_POTION)) {
					tm.devSleep(2000, 500);

				}
			}

			try {
				for (Task task : dreamTasks) {
					boolean taskVal = task.validate();
					if (verboseDebug)
						log(task.getName() + taskVal);

					if (taskVal) {
						status = task.getName();
						task.execute();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
			 * If it's been 35s-67s since the last mouse movement
			 */
			if (System.currentTimeMillis() - lastMouseMove > Script.random(350000, 670000)) {
				if (mouse.isOnScreen())
					mouse.moveOutsideScreen();
			}
			return 600;
		}
		if (!tm.isInDream()) {
			try {
				if (ss.isBlowpiping && !ss.hasCheckedBlowpipe) {
					if (inventory.contains(ss.blowpipeItem)) {
						Item bp = inventory.getItem(ss.blowpipeItem);
						if (bp.hasAction("Check")) {
							if (bp.interact("Check")) {
								sleep(gRandom(1600, 2400, 500));
							}
						}
					} else if (equipment.isWieldingWeapon(ss.blowpipeItem)) {
						Item bp = inventory.getItem(ss.blowpipeItem);
						if (bp.hasAction("Check")) {
							if (bp.interact("Check")) {
								sleep(gRandom(1600, 2400, 500));
							}
						}
					}
				}
				for (Task task : tasks) {
					boolean taskVal = task.validate();
					if (verboseDebug)
						log(task.getName() + taskVal);

					if (taskVal) {
						status = task.getName();
						task.execute();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 600;
		}
		return 600;
	}

	public void onPaint(Graphics2D g) {
		drawMouse(g);
		if (!showPaint) {
			drawShowButton(g);
		}
		if (showPaint) {

			drawPaintBackground(g);

			g.setFont(new Font("Times new roman", 0, 20));
			g.drawString("Poly NMZ", 150, 360);

			g.setFont(new Font("times new roman", 0, 14));
			g.drawString("Status: " + (tm.isInDream() ? "Dreaming.." : status), 10, 385);
			g.drawString("Runtime: " + MiscUtils.runtimeFormat(System.currentTimeMillis() - startTime), 10, 400);
			g.drawString(String.format("Points (h): %s (%s)", (tm.getDreamPoints() - ss.startPoints),
					purtify(getPerHour(tm.getDreamPoints() - ss.startPoints, startTime))), 10, 415);
			g.drawString("Rounds: " + ss.rounds, 10, 430);
			if (tm.isInDream()) {
				g.drawPolygon(ss.dreamGoalPos.getPolygon(bot));
			}

			int skillsY = 385;
			g.setColor(Color.MAGENTA);
			for (Skill skill : ss.trackedSkills) {
				if (experienceTracker.getGainedXP(skill) > 0) {
					String exp = purtify(experienceTracker.getGainedXP(skill));
					String exph = purtify(experienceTracker.getGainedXPPerHour(skill));
					g.drawString(String.format("%s (+%s): %s (%s)", skill.toString(),
							experienceTracker.getGainedLevels(skill), exp, exph), 300, skillsY);
					skillsY += 20;
				}
			}
			if (tm.isInDream()) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("Times new roman", 0, 14));
				g.drawImage(combatIcon, null, 10, 175);
				g.drawString(MiscUtils.runtimeFormat(combatTimer.getRemaining()), 45, 195);
				if (ss.healthDegrade != null) {
					if (ss.healthDegrade == HealthDegrade.DWARVEN_ROCK_CAKE) {
						g.drawImage(rockCakeIcon, null, 10, 275);
						g.drawString(MiscUtils.runtimeFormatS(rockCakeTimer.getRemaining()), 45, 295);
					}
					if (ss.healthDegrade == HealthDegrade.PRAYER_FLICKING) {
						g.drawImage(hitpointsIcon, null, 10, 210);
						g.drawString(MiscUtils.runtimeFormatS(prayerFlickTimer.getRemaining()), 45, 225);
					}
				}
				if (inventory.contains(ConstantVars.OVERLOAD_POTION)) {
					g.drawImage(overloadIcon, null, 10, 245);
					g.drawString(MiscUtils.runtimeFormatS(overloadTimer.getRemaining()), 45, 260);
				}
			}
		}
	}

	public void stopWithMessage(String msg, boolean runtimeStats) {
		StringBuilder sb = new StringBuilder();
		Predicate<Skill> p = s -> s != null && Arrays.asList(ss.trackedSkills).contains(s);
		sb.append(msg + "\n");
		sb.append(String.format("Runtime: %\nRounds: %s (%s)\nPoints (h): %s (%s)\n",
				MiscUtils.runtimeFormat(System.currentTimeMillis() - startTime), ss.rounds,
				banking.getDreamPoints() - ss.startPoints, getPerHour(ss.startPoints, startTime)));
		sb.append("SKILL----EXP--EXP/h");
		Stream.of(Skill.values()).filter(p).forEach(s -> {
			sb.append(String.format("%s----%s--%s", s.toString(), purtify(experienceTracker.getGainedXP(s)),
					purtify(experienceTracker.getGainedXPPerHour(s))));
		});

		stopWithMessage(sb.toString());
	}

	public void stopWithMessage(String msg) {
		String stopStatus = "";
		stopStatus = "\n*****";
		stopStatus += msg;
		stopStatus += "*******";
		log(stopStatus);
		stop(false);
	}

	private void drawShowButton(Graphics2D g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(470, 339, 50, 30);
		g.setColor(new Color(0, 0, 0, 200));
		g.drawRect(470, 339, 50, 30);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Show", 477, 360);
	}

	private void drawPaintBackground(Graphics2D g) {
		g.setColor(paintBackgroundColor);
		g.fillRect(0, 339, 520, 140);
		g.setColor(new Color(0, 0, 0, 200));
		g.drawRect(470, 339, 50, 30);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Hide", 480, 360);
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

	public boolean shouldAntiBan() {
		return ((random(5, client.getMyPlayerIndex()) * random(1, 10)) % 6 == 0);
	}

	private void antiban() throws InterruptedException {

		if (shouldAntiBan()) {
			mouse.moveVerySlightly();

			int gameTicksPassed = (client.getCurrentTick() - startingTick);
			if (gameTicksPassed % 2 == 0) {
				int rando = random(0, 15);
				if (rando % 2 == 0) {
					antiban = "Checking inventory";
					tabs.open(Tab.INVENTORY);
					inventory.hover(random(0, 28));
					sleep(random(500, 1500));
					tabs.open(Tab.SKILLS);
					sleep(random(500, 1500));
					tabs.open(Tab.INVENTORY);
				} else if (rando < random(4, 7)) {
					antiban = "Checking equipment";
					tabs.open(Tab.EQUIPMENT);
					sleep(random(500, 1500));
					tabs.open(Tab.INVENTORY);
				} else {
					antiban = "Camera top";
					camera.toTop();
				}
			}
			if (myPlayer().isUnderAttack())
				if (random(0, 50) < random(20, 50)) {
					antiban = "Random mouse";
					mouse.moveRandomly();
				}
			if (random(6, 60) % 10 == 0 || gameTicksPassed % random(20, 250) == 0) {
				antiban = "Mouse bump";
				mouse.moveVerySlightly();
			}

			if (random(0, random(20, 50)) % 5 == 0) {
				antiban = "Mouse random wait";

				mouse.moveRandomly(random(250, 2500));
			}

			if (gameTicksPassed % random(8500, 25500) == 0 && myPlayer().isUnderAttack()
					|| myPlayer().getInteracting() != null) {
				if (gameTicksPassed % random(23, 152) == 0) {
					antiban = "Switch screens for few seconds";
					mouse.moveOutsideScreen();
					sleep(random(2500, 8000));
				} else {
					if (random((int) Math.round(startingTick / 0.65), client.getCurrentTick() - startingTick)
							% 500 == 0) {
						antiban = "Hover npc, quick switch";
						npcs.closest(true, npcs.getAll()).hover();
						sleep(random(500, 1500));
						mouse.moveOutsideScreen();
					} else if (random(0, 500) % 250 == 0) {
						antiban = "Camera adjustment";
						camera.movePitch(random(15, 30));
						camera.moveYaw(random(random(10, 50), random(50, 85)));
					}

				}

			}

		}
	}

}
