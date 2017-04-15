package com.polycoding.powertraining.tasks;

import org.osbot.rs07.script.Script;

import com.polycoding.powertraining.PolycodingPowerTrain;
import com.polycoding.powertraining.core.Task;

@SuppressWarnings("unchecked")
public class Burying extends Task {

	private PolycodingPowerTrain s;

	public Burying(PolycodingPowerTrain s) {
		super(s);
		this.s = s;
	}

	@Override
	public boolean validate() {
		if (!s.inventory.contains(s.is.bonesName) || s.myPlayer().isUnderAttack()
				|| s.myPlayer().getInteracting() != null)
			return false;
		return s.inventory.getItem(s.is.bonesName).getAmount() >= s.is.buryAt || s.inventory.isFull();
	}

	@Override
	public void execute() {
		logs("Burying some bones");
		int boneCount = 0;
		while (s.inventory.contains(s.is.bonesName)) {
			if (boneCount > 28 || s.myPlayer().isUnderAttack())
				break;
			s.inventory.getItem(s.is.bonesName).interact("Bury");
			try {
				Script.sleep(Script.random(1200, 2500));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boneCount++;
		}
	}

}
