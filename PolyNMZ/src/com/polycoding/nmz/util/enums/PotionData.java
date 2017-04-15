package com.polycoding.nmz.util.enums;

//this enum contains the widget data for buying potions in nmz chest
//until i find a way to dynamically find the widgets, static ids will be used.
//sorry Alek ;)

public enum PotionData {

	ABSORPTION("Absorption (1)", 9, 1000, 11), OVERLOAD("Overload (1)", 6, 1500, 8), SUPER_MAGIC(
			"Super magic potion (1)", 3, 250, 5), SUPER_RANGING("Super ranging (1)", 0, 250, 2);

	PotionData(String potionName, int potId, int price, int quantityId) {
		this.potionName = potionName;
		this.potId = potId;
		this.price = price;
		this.quantityId = quantityId;
	}

	private String potionName;
	private static int rootId = 206; // 206
	private static int childId = 6; // 6
	private int potId; // image of the potion to buy-x
	private int price;
	private int quantityId;

	public static int getRootId() {
		return rootId;
	}

	public static int getChildId() {
		return childId;
	}

	public String getStoreName() {
		return this.potionName;
	}

	public int getPrice() {
		return this.price;
	}

	public int getBuyingWidgetId() {
		return this.potId;
	}

	public int getQuantityWidgetId() {
		return this.quantityId;
	}

}
