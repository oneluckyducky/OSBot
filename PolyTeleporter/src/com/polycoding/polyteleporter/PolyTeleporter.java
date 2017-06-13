package com.polycoding.polyteleporter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import org.osbot.rs07.api.ui.MagicSpell;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

@ScriptManifest(author = "Polymorphism", info = "Simple teleporter", logo = "https://avatars2.githubusercontent.com/u/15859406?v=3&s=180", name = "PolyTeleporter", version = 1.0)
public class PolyTeleporter extends Script {

	private String teleInput = "";

	private long startExp = 0;
	private String status = "something";
	private String stopStatus = "";
	private long startTime = 0;
	private Font font = null;

	private int casts = 0;
	private int returnInterval = 600;

	private MagicSpell spell = null;

	@Override
	public void onStart() throws InterruptedException {

		teleInput = JOptionPane.showInputDialog(
				"Which teleport? Type the FIRST LETTER of the tele name\n\nEX: 'v' = Varrock or 'l' = Lumbridge");
		switch (teleInput.toUpperCase()) {
		case "V":
			spell = Spells.NormalSpells.VARROCK_TELEPORT;
			break;
		case "L":
			spell = Spells.NormalSpells.LUMBRIDGE_TELEPORT;
			break;
		case "F":
			spell = Spells.NormalSpells.FALADOR_TELEPORT;
			break;
		case "H":
			spell = Spells.NormalSpells.HOME_TELEPORT;
			break;
		case "C":
			spell = Spells.NormalSpells.CAMELOT_TELEPORT;
			break;
		case "A":
			spell = Spells.NormalSpells.ARDOUGNE_TELEPORT;
			break;
		default:
			stopWithMessage("Sorry, that spell either is invalid or isn't yet supported.", false);
		}

		if (!magic.canCast(spell)) {
			stopWithMessage("Cannot cast the spell for some reason", false);
		}
		log("Starting up script now!");
		startTime = System.currentTimeMillis();
		font = new Font("Arial", 0, 11);
		startExp = skills.getExperience(Skill.MAGIC);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onLoop() throws InterruptedException {
		if (!Tab.MAGIC.isOpen(getBot()))
			tabs.magic.open();
		if (magic.canCast(spell)) {
			status = "Casting " + spell.toString().replace("_", " ");
			if (magic.castSpell(spell)) {
				status = "Waiting...";
				new ConditionalSleep(1000) {

					@Override
					public boolean condition() throws InterruptedException {
						return !myPlayer().isAnimating() || myPlayer().getAnimation() == -1;
					}

				};
				casts++;
			}
		} else {
			stopWithMessage("Can't cast this teleport any longer, please check your inventory!", true);
		}

		return returnInterval;
	}

	public void onPaint(Graphics2D g) {

		long tmpExp = 0;
		tmpExp = skills.getExperience(Skill.MAGIC);

		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(580, 0, 200, 160);

		g.setFont(this.font);

		g.setColor(Color.WHITE);
		g.drawString("Welcome!", 585, 150);
		g.drawString("Version: " + getVersion(), 585, 10);
		g.drawString("Status: " + status, 585, 25);
		g.drawString("Runtime: " + MiscUtils.getRuntimeFormat(System.currentTimeMillis() - startTime), 585, 40);
		g.drawString("Exp: " + purtify((tmpExp - startExp)), 585, 55);
		g.drawString("Exp/h: " + purtify(getPerHour(tmpExp - startExp, startTime)), 585, 70);

		g.drawString("Casts: " + purtify((casts)), 585, 55);
		g.drawString("Casts/h: " + purtify(getPerHour(casts, startTime)), 585, 70);
	}

	public void stopWithMessage(String msg, boolean dumpRunStats) {
		long tmpExp = 0;
		for (Skill skill : Skill.values()) {
			tmpExp += skills.getExperience(skill);
		}
		stopStatus = "\n*****";
		stopStatus += msg;
		stopStatus += String.format("\nRuntime: %s\nExp Gained: %s\n*****",
				MiscUtils.getRuntimeFormat(System.currentTimeMillis() - startTime), purtify((tmpExp - startExp)));
		stop();
	}

	public String purtify(long num) {
		return DecimalFormat.getInstance().format(num);
	}

	public long getPerHour(final long base, final long time) {
		return ((base) * 3600000 / (System.currentTimeMillis() - time));
	}

}
