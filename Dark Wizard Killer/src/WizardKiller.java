import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.listener.MessageListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import com.polycoding.wizardkiller.framework.MyProvider;
import com.polycoding.wizardkiller.framework.Node;
import com.polycoding.wizardkiller.gui.Gui;
import com.polycoding.wizardkiller.tasks.banking.CloseBank;
import com.polycoding.wizardkiller.tasks.banking.Depositing;
import com.polycoding.wizardkiller.tasks.banking.OpenBank;
import com.polycoding.wizardkiller.tasks.banking.Withdrawing;
import com.polycoding.wizardkiller.tasks.combat.Attacking;
import com.polycoding.wizardkiller.tasks.combat.Eating;
import com.polycoding.wizardkiller.tasks.traverse.ToBank;
import com.polycoding.wizardkiller.tasks.traverse.ToWizards;
import com.polycoding.wizardkiller.util.Timer;
import com.polycoding.wizardkiller.util.XWalking;
import com.polycoding.wizardkiller.util.enums.Destination;

@ScriptManifest(author = "OneLuckyDuck", name = "Dark Wizard Killer", version = 1.0, info = "Kills wizards at stone circle in Varrock.\n\nHIGHLY RECOMMEND BANKING\nDUE TO TALISMANS FILLING INVENTORY!", logo = "")
public class WizardKiller extends Script implements Painter, MessageListener {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	int ls = 0;

	public MyProvider provider = new MyProvider(this);

	public Attacking attacking = new Attacking(this, provider);
	public Eating eating = new Eating(this, provider);
	public ToBank toBank = new ToBank(this, provider);
	public ToWizards toWizards = new ToWizards(this, provider);
	public OpenBank openBank = new OpenBank(this, provider);
	public Depositing depositing = new Depositing(this, provider);
	public Withdrawing withdrawing = new Withdrawing(this, provider);
	public CloseBank closeBank = new CloseBank(this, provider);

	public Gui gui = new Gui(this, provider);

	public static final Timer runtimer = new Timer(0);

	private int idleLoops = 0;

	private int startingExperience = 0;
	private int startingHpExperience = 0;

	private long start = System.currentTimeMillis();

	@Override
	public void onMessage(Message m) {
		String msg = m.getMessage();
		if (msg.equalsIgnoreCase("there is no ammo left in your quiver.")) {
			log("Out of ammo. Walking to bank, logging out, and screenshotting!!");
			try {
				XWalking.walkPath(this, Destination.BANK.getRandomPath());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ss();
			this.stop();
		}

	}

	private void ss() {
		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
					.getScreenSize());
			BufferedImage capture = new Robot().createScreenCapture(screenRect);
			ImageIO.write(capture, "png", new File(
					"C:\\Users\\Jacob\\OSBot\\ss.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onLoop() throws InterruptedException {
		if (idleLoops >= 150)
			localWalker.walk(new Position(3227, 3368, 0), true);
		for (Node node : nodes) {
			boolean validate = node.validate();
			if (validate) {
				provider.status = node.status();
				node.execute();
			}
			// log("\n" + node.toString() + ": " + validate);
		}
		ls++;
		if (!myPlayer().isMoving() && !provider.isInCombat(myPlayer()))
			idleLoops++;
		else
			idleLoops = 0;
		return 200;
	}

	@Override
	public void onExit() throws InterruptedException {
		if (gui.isVisible())
			gui.dispose();

	}

	@Override
	public void onStart() throws InterruptedException {
		ss();
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				startingExperience += skills.getExperience(Skill.values()[i]);
		}
		startingHpExperience = this.skills.getExperience(Skill.HITPOINTS);
		gui.setVisible(true);
		if (!this.settings.isRunning())
			settings.setRunning(true);
		if (!this.combat.isAutoRetaliateOn())
			this.combat.activateAutoRetaliate(true);
		while (gui.isVisible()) {
			sleep(100);
		}
		provider.itemsList.add("Coins");
		Collections.addAll(provider.itemsList, provider.ITEM_NAMES);
		log(String
				.format("\nBanking:%s\nFood:%s\nFood amount:%s\nBury bones:%s\nEating at:%s",
						provider.attr.get("banking"),
						provider.attr.get("food"),
						provider.attr.get("foodAmount"),
						provider.attr.get("bones"), provider.attr.get("eatAt")));

		Collections.addAll(nodes, eating, attacking, toBank, toWizards,
				openBank, depositing, withdrawing, closeBank);
	}

	@Override
	public void onPaint(Graphics2D g) {
		super.onPaint(g);

		for (NPC c : this.npcs.getAll())
			if (c.isInteracting(myPlayer()))
				g.draw(c.getPosition().getPolygon(bot));

		if (provider.BANK_AREA.contains(myPlayer()))
			for (Position p : provider.BANK_AREA.positions) {
				if (p.isVisible(bot))
					g.fill(p.getPolygon(bot));
			}
		if (provider.WIZARD_AREA.contains(myPlayer()))
			for (Position p : provider.WIZARD_AREA.getBoundingPositions()) {
				if (p.isVisible(bot))
					g.fill(p.getPolygon(bot));
			}

		int newExp = 0;
		for (int i = 0; i < 7; i++) {
			if (i != 3)
				newExp += skills.getExperience(Skill.values()[i]);
		}

		int newHpExp = 0;
		newHpExp = skills.getExperience(Skill.HITPOINTS);

		int gainedExp = newExp - startingExperience;
		int gainedHpExp = newHpExp - startingHpExperience;

		int expHour = getPerHour(gainedExp, start);
		int hpExpHour = getPerHour(gainedHpExp, start);

		int profitHour = getPerHour((int) provider.profit, start);

		Graphics2D g1 = (Graphics2D) g.create();
		g1.setColor(Color.BLACK);

		g1.drawString("Version: " + getVersion(), 10, 75);
		g1.drawString("Runtime: " + runtimer.toElapsedString(), 10, 100);
		g1.drawString(String.format("Exp gained (hr): %s (%s)", d(gainedExp),
				d(expHour)), 10, 115);
		g1.drawString(String.format("HP exp gained (hr): %s (%s)",
				d(gainedHpExp), d(hpExpHour)), 10, 130);
		g1.drawString(String.format("Money gained (hr): %s (%s)",
				d((int) provider.profit), d(profitHour)), 10, 145);
		g1.drawString("Status: " + provider.status(), 10, 170);

		g1.drawString(String.format("Idle loops: %s", idleLoops), 10, 190);

	}

	public static int getPerHour(final int base, final long time) {
		return (int) ((base) * 3600000D / (System.currentTimeMillis() - time));
	}

	public static String d(int d) {
		return DecimalFormat.getInstance().format(d);
	}

	public static String formatNumbers(final int n) {
		return n / 1000 + "." + (n % 1000) / 100 + "K";
	}

}
