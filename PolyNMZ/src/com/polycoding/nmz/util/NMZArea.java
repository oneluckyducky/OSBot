package com.polycoding.nmz.util;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

public class NMZArea extends DynamicArea {

	private Position pos;
	private MethodProvider mp;

	public NMZArea(Position pos, MethodProvider mp) {
		super(new Area(new Position(pos.getX() - 20, pos.getY() + 31, pos.getZ()),
				new Position(pos.getX() + 11, pos.getY(), pos.getZ())), mp);
		this.pos = pos;
		this.mp = mp;
	}
	
	public Position getStartingPosition(){
		return this.pos;
	}

	
	
}
