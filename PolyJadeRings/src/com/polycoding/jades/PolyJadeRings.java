package com.polycoding.jades;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.Utilities;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.polycoding.jades.InstanceSettings.ActiveState;
import com.polycoding.jades.core.Task;
import com.polycoding.jades.tasks.crafting.GBankWalking;
import com.polycoding.jades.tasks.crafting.GBanking;
import com.polycoding.jades.tasks.crafting.GWalking;
import com.polycoding.jades.tasks.crafting.Smelting;
import com.polycoding.jades.tasks.enchanting.EBanking;
import com.polycoding.jades.tasks.enchanting.EEnchanting;
import com.polycoding.jades.util.MiscUtils;

@ScriptManifest(author = "Polymorphism", info = "Crafts/enchants Jade rings", logo = "", name = "Poly Jade Rings", version = 1.0)
public class PolyJadeRings extends Script {

	public InstanceSettings is = null;

	public String status = "";

	private LinkedList<Task> craftingTaskList = new LinkedList<Task>();
	private LinkedList<Task> enchantingTaskList = new LinkedList<Task>();

	public long startTime = 0;
	private Font font = null;
	private int returnInterval = 0;

	public Smelting smelting = null;
	public GBankWalking gBankWalking = null;
	public GBanking gBanking = null;
	public GWalking gWalking = null;

	public EBanking eBanking = null;
	public EEnchanting eEnchanting = null;

	public void onStart() {
		try {
			returnInterval = random(600, 1200);
			is = new InstanceSettings();
			is.edgeBank = new Area(3099, 3487, 3090, 3501);
			is.edgeFurnace = new Area(3112, 3496, 3104, 3502);

			font = new Font("Arial", 0, 11);
			startTime = System.currentTimeMillis();
			experienceTracker.start(Skill.CRAFTING);

			smelting = new Smelting(this);
			gBankWalking = new GBankWalking(this);
			gBanking = new GBanking(this);
			gWalking = new GWalking(this);

			eBanking = new EBanking(this);
			eEnchanting = new EEnchanting(this);

			craftingTaskList.add(smelting);
			craftingTaskList.add(gBankWalking);
			craftingTaskList.add(gBanking);
			craftingTaskList.add(gWalking);

			enchantingTaskList.add(eBanking);
			enchantingTaskList.add(eEnchanting);

			if (!is.edgeBank.contains(myPlayer())) {
				logs("Have to walk to Edgeville before starting..");
				walking.webWalk(is.edgeBank);
			}
			if (is.edgeBank.contains(myPlayer())) {
				if (!bank.isOpen()) {
					bank.open();
					sleep(random(800, 1400));
				}
				if (bank.isOpen()) {
					if (canCraft()) {
						is.activeState = ActiveState.CRAFTING;
					} else {
						if (!canCraft() && canEnchant()) {
							is.activeState = ActiveState.ENCHANTING;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onExit() {
		Utilities.takeScreenshot();
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(new File(this.getDirectoryData() + "\\Screenshots\\"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int onLoop() throws InterruptedException {
		try {

			switch (is.activeState) {
			case CRAFTING:
				for (Task t : craftingTaskList) {
					if (t.validate())
						t.execute();
				}
				if (status.contains("melting")) {
					int r = random(0, 1500);
					if (r % 2 == 0) {
						mouse.moveOutsideScreen();
					}
				}
				break;
			case ENCHANTING:
				for (Task t : enchantingTaskList) {
					if (t.validate())
						t.execute();
				}
				break;
			default:
				break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnInterval;
	}

	public void onPaint(Graphics2D g) {
		drawMouse(g);
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(580, 0, 200, 160);

		g.setFont(this.font);
		g.setColor(Color.WHITE);
		g.drawString("Welcome!", 585, 150);
		g.drawString("ActiveState: " + is.activeState.toString(), 585, 10);
		g.drawString("Status: " + status, 585, 25);
		g.drawString("Runtime: " + MiscUtils.getRuntimeFormat(System.currentTimeMillis() - startTime), 585, 40);
		g.drawString("Exp: " + purtify(experienceTracker.getGainedXP(Skill.CRAFTING)), 585, 55);
		g.drawString("Exp/h: " + purtify(experienceTracker.getGainedXPPerHour(Skill.CRAFTING)), 585, 70);
	}

	public boolean canEnchant() {
		return bank.contains("Staff of air") && bank.contains("Cosmic rune") && mg() >= 27;
	}

	public boolean canCraft() {
		return bank.contains("Silver bar") && bank.contains("Jade") && crf() >= 13;
	}

	public void logs(Object o) {
		status = o.toString();
		log(o);
	}

	public int crf() {
		return skills.getStatic(Skill.CRAFTING);
	}

	public int mg() {
		return skills.getStatic(Skill.MAGIC);
	}

	private void drawMouse(Graphics2D g) {
		g.setColor(Color.CYAN);
		Point p = mouse.getPosition();
		Dimension d = bot.getCanvas().getSize();
		g.drawLine(0, p.y, d.width, p.y);
		g.drawLine(p.x, 0, p.x, d.height);
	}

	public void stopWithMessage(String msg) {
		String stopStatus = "";
		stopStatus = "\n*****";
		stopStatus += msg + "\n*******";
		log(stopStatus);
		stop();
	}

	public String purtify(long num) {
		return DecimalFormat.getInstance().format(num);
	}

	public long getPerHour(final long base, final long time) {
		return ((base) * 3600000 / (System.currentTimeMillis() - time));
	}

}
