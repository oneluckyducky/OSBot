package com.polycoding.nmz.tasks.dreamtasks;

import org.osbot.rs07.api.ui.PrayerButton;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.polycoding.nmz.PolyNMZ;
import com.polycoding.nmz.core.Task;
import com.polycoding.nmz.util.Timer;
import com.polycoding.nmz.util.enums.HealthDegrade;

public class PrayerFlicking extends Task {

	public PrayerFlicking(PolyNMZ polycodingNMZ) {
		super(polycodingNMZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "PrayerFlicking ";
	}

	@Override
	public boolean validate() throws Exception {
		return s.ss.healthDegrade == HealthDegrade.PRAYER_FLICKING
				&& (s.prayerFlickTimer == null || !s.prayerFlickTimer.isRunning());
	}

	@Override
	public void execute() throws Exception {
		if (s.ss.healthDegrade == HealthDegrade.PRAYER_FLICKING) {
			if (s.prayerFlickTimer == null) {
				if (s.prayer.set(PrayerButton.RAPID_HEAL, true)) {
					new ConditionalSleep(4500, 100) {

						@Override
						public boolean condition() throws InterruptedException {
							return s.prayer.isActivated(PrayerButton.RAPID_HEAL);
						}

					}.sleep();
				}
				devSleep(300, 50);
				if (s.prayer.set(PrayerButton.RAPID_HEAL, false)) {
					s.mouse.moveOutsideScreen();
					s.prayerFlickTimer = new Timer(Math.round(nextDeviation(36000, 15000)));
				}

			}
			if (!s.prayerFlickTimer.isRunning()) {
				Script.sleep(Script.random((int) s.prayerFlickTimer.getPeriod() / 8,
						(int) s.prayerFlickTimer.getPeriod() / 6));
				if (s.prayer.set(PrayerButton.RAPID_HEAL, true)) {
					new ConditionalSleep(4500, 100) {

						@Override
						public boolean condition() throws InterruptedException {
							return s.prayer.isActivated(PrayerButton.RAPID_HEAL);
						}

					}.sleep();
				}
				devSleep(350, 75);
				if (s.prayer.set(PrayerButton.RAPID_HEAL, false)) {
					s.mouse.moveOutsideScreen();
					s.prayerFlickTimer = new Timer(Math.round(nextDeviation(30000, 17000)));
				}
			}
		}
	}

}
