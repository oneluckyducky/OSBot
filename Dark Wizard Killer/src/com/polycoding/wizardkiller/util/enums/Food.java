package com.polycoding.wizardkiller.util.enums;

public enum Food {
	BREAD(2309), WINE(1993), TROUT(333), PIKE(351), SALMON(329), TUNA(361), LOBSTER(
			379), BASS(365), SWORDFISH(373), MONKFISH(7946), SHARK(385), SEA_TURTLE(
			697), MANTA_RAY(391);

	private int id;

	private Food(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}

}
