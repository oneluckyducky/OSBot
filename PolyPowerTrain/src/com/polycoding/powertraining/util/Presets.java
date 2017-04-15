package com.polycoding.powertraining.util;

public enum Presets {

	MINOTAURS("minotaur",
			"coins,iron arrow,steel arrow,rune arrow,pure essence,rune essence,tin ore"), CHICKENS(
					"chicken", "feather,coins"), COWS("cow", "cowhide"), GOBLINS("goblin",
							"coins,earth rune,water rune,fire rune,mind rune,body rune"), DARK_WIZARD(
									"dark wizard",
									"coins,air rune,mind rune,fire rune,body rune,water rune,earth rune,chaos rune,nature rune,death rune,law rune");

	private String npc, loot;

	Presets(String npc, String loot) {
		this.npc = npc;
		this.loot = loot;
	}

	public String getNpc() {
		return npc;
	}

	public String getLoot() {
		return loot;
	}

}
