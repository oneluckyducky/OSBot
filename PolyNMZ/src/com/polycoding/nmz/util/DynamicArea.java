package com.polycoding.nmz.util;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.script.MethodProvider;

public class DynamicArea {

	private MethodProvider mp;

	private Area area;

	private int hiX, hiY, loX, loY, z;

	private int width, height;

	private LinkedList<Integer> allX = new LinkedList<Integer>(), allY = new LinkedList<Integer>();

	/**
	 * 
	 * @param area
	 *            A Rectangular org.osbot.rs07.api.map.Area to get PrincipalWind
	 *            positions from
	 * @param mp
	 *            MethodProvider
	 */
	public DynamicArea(Area area, MethodProvider mp) {
		this.mp = mp;
		this.area = area;
		this.z = area.getPlane();
		this.area.getPositions().stream().filter(p -> mp.map.canReach(p))
				.collect(Collectors.toList()).sort(((p1, p2) -> Integer
						.compare(mp.map.realDistance(p1), mp.map.realDistance(p2))));

		gatherAllXY();
	}

	public Area getWorkingArea() {
		return this.area;
	}

	public List<Position> getBoundingPositions() {
		return this.area.getPositions().stream().filter(
				p -> p.distance(getCenter()) == height / 2 || p.distance(getCenter()) == width / 2)
				.collect(Collectors.toList());

	}

	public Position getPosition(PrincipalWind pw) {
		switch (pw) {
		case E:
			return getEast();
		case N:
			return getNorth();
		case NE:
			return getNorthEast();
		case NW:
			return getNorthWest();
		case S:
			return getSouth();
		case SE:
			return getSouthEast();
		case SW:
			return getSouthWest();
		case W:
			return getWest();
		default:
			return null;
		}
	}

	public Position getCenter() {
		Point2D.Double cp = calcPolygonCenter(area.getPolygon());
		return new Position((int) Math.round(cp.x), (int) Math.round(cp.y), z);
	}

	public Position getNorth() {
		return new Position(hiX - (width / 2), hiY, z);
	}

	public Position getSouth() {
		return new Position(hiX - (width / 2), loY, z);
	}

	public Position getWest() {
		return new Position(loX, hiY - (height / 2), z);
	}

	public Position getEast() {
		return new Position(hiX, hiY - (height / 2), z);
	}

	public Position getNorthEast() {
		return new Position(hiX, hiY, z);
	}

	public Position getNorthWest() {
		return new Position(loX, hiY, z);
	}

	public Position getSouthEast() {
		return new Position(hiX, loY, z);
	}

	public Position getSouthWest() {
		return new Position(loX, loY, z);
	}

	public Point2D.Double calcPolygonCenter(Polygon polygon) {
		double x = 0;
		double y = 0;
		int points = polygon.npoints;
		for (int i = 0; i < polygon.xpoints.length; i++) {
			x += polygon.xpoints[i];
		}
		for (int i = 0; i < polygon.ypoints.length; i++) {
			y += polygon.ypoints[i];
		}

		x = x / points;
		y = y / points;

		return new Point2D.Double(x, y);
	}

	private void setHiLoX() {
		int size = allX.size();
		// The values should be sorted low->high, double checking
		if (allX.get(0) < allX.get(size - 1)) {
			loX = allX.get(0);
			hiX = allX.get(size - 1);
		}
	}

	private void setHiLoY() {
		int size = allY.size();
		// The values should be sorted low->high, double checking
		if (allY.get(0) < allY.get(size - 1)) {
			loY = allY.get(0);
			hiY = allY.get(size - 1);
		}
	}

	private void gatherAllXY() {
		this.area.getPositions().forEach(p -> {
			if (!allX.contains(p.getX())) {
				allX.add(p.getX());
			}
			if (!allY.contains(p.getY())) {
				allY.add(p.getY());
			}
		});
		setHiLoX();
		setHiLoY();
		width = hiX - loX;
		height = hiY - loY;
	}

	public enum PrincipalWind {
		N, NE, E, SE, S, SW, W, NW;
	}

}
