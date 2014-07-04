package com.polycoding.wizardkiller.util.enums;

import java.util.Random;

import org.osbot.rs07.api.map.Position;

public enum Destination {

	BANK(new Position[][] {
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
					new Position(3183, 3438, 0) } }),

	WIZARDS(new Position[][] {
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
					new Position(3228, 3371, 0) } });

	private Position[][] paths;

	private Destination(final Position[][] paths) {
		this.paths = paths;
	}

	private int random(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	public Position[] getRandomPath() {
		return paths[random(0, paths.length - 1)];
	}
	
	public Position[] getPathByIndex(int index) {
		return paths[index];
	}

};
