package com.polycoding.nmz.util.enums;

public enum RegularPotionData {

	SUPER_STRENGTH(new String[] { "Super strength(1)", "Super strength(2)", "Super strength(3)",
			"Super strength(4)" }), SUPER_ATTACK(new String[] { "Super attack(1)",
					"Super attack(2)", "Super attack(3)", "Super attack(4)" }), SUPER_DEFENCE(
							new String[] { "Super defence (1)", "Super defence (2)",
									"Super defence (3)", "Super defence (4)" }), PRAYER(
											new String[] { "Prayer potion (1)", "Prayer potion (2)",
													"Prayer potion (3)",
													"Prayer potion (4)" }), SUPER_RESTORE(
															new String[] {});

	private String[] names;

	RegularPotionData(String[] names) {
		this.names = names;
	}

	public String[] getNames() {
		return this.names;
	}

}
