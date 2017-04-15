package com.polycoding.nmz.util.enums;

public enum Dart {

	BRONZE_DART("Bronze dart"), IRON_DART("Iron dart"), STEEL_DART("Steel dart"), BLACK_DART(
			"Black dart"), MITHRIL_DART("Mithril dart"), ADAMANT_DART(
					"Adamant dart"), RUNE_DART("Rune dart"), DRAGON_DART("Dragon dart");

	private String name;

	Dart(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
