package com.polycoding.powertraining.util;

public enum SpecialWeapon {

	DRAGON_DAGGER_SP("Dragon dagger(p++)", 25), DRAGON_SCIMITAR("Dragon scimitar", 55), ABYSSAL_WHIP("Abyssal whip",
			50), SARADOMIN_SWORD("Saradomin sword", 65), SARADOMIN_GODSWORD("Saradomin godsword",
					100), DRAGON_MACE("Dragon mace", 25), MAGIC_SHORTBOW("Magic shortbow",
							50), MAGIC_SHORTBOW_I("Magic shortbow(i)", 50), TOXIC_BLOWPIPE("Toxic blowpipe", 50);

	private String name;
	private int spec;

	SpecialWeapon(String name, int spec) {
		this.name = name;
		this.spec = spec;
	}

	public String getName() {
		return this.name;
	}

	public int getSpecAmount() {
		return this.spec;
	}

}
