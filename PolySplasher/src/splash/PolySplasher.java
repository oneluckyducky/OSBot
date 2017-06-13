package splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Polymorphism", info = "", logo = "", name = "PolySplasher", version = 0)
public class PolySplasher extends Script {

	private long lastTime = 0;
	private long startTime = 0;

	Timer timer = null;

	@Override
	public void onStart() throws InterruptedException {
		experienceTracker.start(Skill.MAGIC);
		startTime = System.currentTimeMillis();
		timer = new Timer(random(720000, 900000));
		attack();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onLoop() throws InterruptedException {
		if (!timer.isRunning()) {
			if (combat.isFighting() || myPlayer().isUnderAttack()
					|| npcs.filter(n -> n != null && n.getInteracting() == myPlayer()).size() > 0) {
				int r = random(500);
				if (r % 100 == 0) {
					camera.toEntity(npcs.closest("Seagull"));
				} else if (r % 250 == 0) {
					camera.toTop();
					camera.moveYaw(random(50));
				} else {
					camera.movePitch(random(100));
					camera.toBottom();
				}
			} else {
				attack();
			}
			timer = new Timer(random(720000, 900000));
		}
		if (!isIdle())
			mouse.moveOutsideScreen();
		if (dialogues.isPendingContinuation()) {
			if (dialogues.clickContinue()) {
				sleep(random(1000, 3000));
				if (!isIdle())
					mouse.moveOutsideScreen();
			}
		}
		return 1000;
	}

	void attack() throws InterruptedException {
		NPC gull = npcs.closest(s -> s != null && s.isAttackable() && s.getHealthPercentCache() > 0
				&& s.getName().equalsIgnoreCase("seagull"));
		if (gull != null) {
			if (gull.interact("Attack")) {
				lastTime = System.currentTimeMillis();
				sleep(random(1000, 3000));
				if (!isIdle())
					mouse.moveOutsideScreen();
			}
		}
	}

	@Override
	public void onPaint(Graphics2D g) {
		drawMouse(g);
		g.drawString("Runtime: " + Timing.msToString(System.currentTimeMillis() - startTime), 10, 85);
		g.drawString(String.format("Magic (h): %s (%s)", experienceTracker.getGainedXP(Skill.MAGIC),
				experienceTracker.getGainedXPPerHour(Skill.MAGIC)), 10, 100);
		g.drawString("Time left: " + Timing.msToString(timer.getRemaining()), 10, 115);
	}

	private void stopWithMessage(String msg) {
		log(msg);
		stop(false);
	}

	public boolean isIdle() {
		return myPlayer().getInteracting() == null && !myPlayer().isMoving() && !myPlayer().isAnimating();
	}

	public boolean openTab(Tab tab) {
		if (!tab.isOpen(bot)) {
			return tabs.open(tab);
		}
		return false;
	}

	private void drawMouse(Graphics2D g) {
		g.setColor(Color.CYAN);
		Point p = mouse.getPosition();
		Dimension d = bot.getCanvas().getSize();
		g.drawLine(0, p.y, d.width, p.y);
		g.drawLine(p.x, 0, p.x, d.height);
	}

}
