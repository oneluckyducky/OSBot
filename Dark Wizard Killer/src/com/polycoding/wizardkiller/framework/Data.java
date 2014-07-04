package com.polycoding.wizardkiller.framework;

import java.util.ArrayList;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.utility.Attributes;

import com.polycoding.wizardkiller.util.PolygonArea;
import com.polycoding.wizardkiller.util.PriceWrapper;

public class Data extends MethodProvider {

	public Attributes attr = new Attributes();
	public ArrayList<String> itemsList = new ArrayList<String>();

	public String status = "null";

	public long profit = 0;

	public final String[] FOOD_NAMES = { "Bread", "Wine", "Trout", "Pike",
			"Salmon", "Tuna", "Lobster", "Bass", "SwordFish", "MonkFish",
			"Shark", "Sea Turtle", "Manta Ray" };

	public final String[] ITEM_NAMES = { "Earth rune", "Air rune",
			"Water rune", "Fire rune", "Mind rune", "Body rune", "Chaos rune",
			"Nature rune", "Law rune", "Cosmic rune", "Blood rune",
			"Staff of air", "Water talisman", "Fire talisman",
			"Earth talisman", "Staff of water" };

	public PriceWrapper prices = new PriceWrapper(ITEM_NAMES);

	public final int[] WIZARD_IDS = { 8871, 8872, 8873, 8874 };

	public final int[] LOOTABLE_ITEMS = { 556, 555, 554, 557, 561, 565, 562,
			559, 563, 564, 558, 8015, 24154 };

	public final PolygonArea BANK_AREA = new PolygonArea(new Position[] {
			new Position(3176, 3448, 0), new Position(3176, 3445, 0),
			new Position(3177, 3442, 0), new Position(3177, 3439, 0),
			new Position(3176, 3436, 0), new Position(3177, 3432, 0),
			new Position(3178, 3430, 0), new Position(3179, 3427, 0),
			new Position(3182, 3427, 0), new Position(3185, 3429, 0),
			new Position(3188, 3429, 0), new Position(3191, 3431, 0),
			new Position(3193, 3432, 0), new Position(3194, 3435, 0),
			new Position(3194, 3438, 0), new Position(3195, 3441, 0),
			new Position(3194, 3445, 0), new Position(3193, 3447, 0),
			new Position(3190, 3449, 0), new Position(3187, 3449, 0),
			new Position(3184, 3449, 0) });

	public final PolygonArea WIZARD_AREA = new PolygonArea(new Position[] {
			new Position(3227, 3357, 0), new Position(3236, 3360, 0),
			new Position(3239, 3367, 0), new Position(3240, 3371, 0),
			new Position(3239, 3374, 0), new Position(3238, 3377, 0),
			new Position(3236, 3379, 0), new Position(3234, 3379, 0),
			new Position(3233, 3380, 0), new Position(3231, 3380, 0),
			new Position(3229, 3380, 0), new Position(3228, 3380, 0),
			new Position(3226, 3380, 0), new Position(3224, 3380, 0),
			new Position(3223, 3380, 0), new Position(3222, 3380, 0),
			new Position(3220, 3380, 0), new Position(3219, 3378, 0),
			new Position(3218, 3376, 0), new Position(3218, 3372, 0),
			new Position(3218, 3370, 0), new Position(3219, 3367, 0),
			new Position(3219, 3364, 0), new Position(3220, 3362, 0),
			new Position(3221, 3361, 0), new Position(3221, 3360, 0) });

	public final Position[][] TO_BANK_PATHS = {

			{ new Position(3226, 3370, 0), new Position(3222, 3372, 0),
					new Position(3214, 3374, 0), new Position(3214, 3378, 0),
					new Position(3213, 3384, 0), new Position(3213, 3390, 0),
					new Position(3213, 3394, 0), new Position(3212, 3400, 0),
					new Position(3208, 3404, 0), new Position(3204, 3409, 0),
					new Position(3198, 3417, 0), new Position(3195, 3421, 0),
					new Position(3192, 3427, 0), new Position(3185, 3428, 0),
					new Position(3184, 3431, 0), new Position(3183, 3433, 0),
					new Position(3183, 3435, 0) },

			{ new Position(3228, 3371, 0), new Position(3228, 3371, 0),
					new Position(3222, 3372, 0), new Position(3216, 3374, 0),
					new Position(3213, 3380, 0), new Position(3213, 3386, 0),
					new Position(3213, 3392, 0), new Position(3212, 3398, 0),
					new Position(3212, 3404, 0), new Position(3212, 3410, 0),
					new Position(3212, 3416, 0), new Position(3212, 3422, 0),
					new Position(3207, 3426, 0), new Position(3201, 3428, 0),
					new Position(3195, 3428, 0), new Position(3189, 3428, 0),
					new Position(3183, 3430, 0), new Position(3183, 3436, 0),
					new Position(3183, 3438, 0) } };

	public final Position[][] FROM_BANK_PATHS = {

			{ new Position(3183, 3434, 0), new Position(3187, 3432, 0),
					new Position(3193, 3432, 0), new Position(3197, 3430, 0),
					new Position(3203, 3430, 0), new Position(3208, 3425, 0),
					new Position(3210, 3419, 0), new Position(3210, 3413, 0),
					new Position(3210, 3407, 0), new Position(3210, 3401, 0),
					new Position(3210, 3395, 0), new Position(3210, 3389, 0),
					new Position(3210, 3383, 0), new Position(3215, 3377, 0),
					new Position(3218, 3374, 0), new Position(3224, 3372, 0),
					new Position(3227, 3373, 0) },

			{ new Position(3183, 3435, 0), new Position(3186, 3432, 0),
					new Position(3192, 3431, 0), new Position(3196, 3425, 0),
					new Position(3200, 3421, 0), new Position(3200, 3415, 0),
					new Position(3208, 3410, 0), new Position(3210, 3405, 0),
					new Position(3210, 3399, 0), new Position(3210, 3395, 0),
					new Position(3210, 3389, 0), new Position(3210, 3383, 0),
					new Position(3210, 3377, 0), new Position(3212, 3371, 0),
					new Position(3218, 3374, 0), new Position(3224, 3372, 0),
					new Position(3228, 3371, 0) } };

}
