package com.polycoding.wizardkiller.util;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;

/**
 * Created with IntelliJ IDEA User: Anthony Date: 3/23/2014
 */

public class PolygonArea {

	public Polygon polygon;
	public final Position[] positions;
	private Position[] tileArrayCache = null;
	protected int plane = -1;

	public PolygonArea(Position... positions) {
		this.positions = positions;
		this.plane = positions[0].getZ();
		polygon = new Polygon(retrieveXPoints(positions),
				retrieveYPoints(positions), positions.length);
	}

	public boolean contains(Entity entity) {
		return polygon.contains(entity.getX(), entity.getY());
	}

	public boolean contains(Position position) {
		return polygon.contains(position.getX(), position.getY());
	}

	/**
	 * @return the tiles backing this Area.
	 */
	public Position[] getBoundingPositions() {
		final Position[] bounding = new Position[polygon.npoints];
		for (int i = 0; i < polygon.npoints; i++) {
			bounding[i] = new Position(polygon.xpoints[i], polygon.ypoints[i],
					plane);
		}
		return bounding;
	}

	/**
	 * @return a bounding rectangle of this area.
	 */
	public Rectangle getBounds() {
		return polygon.getBounds();
	}

	public boolean contains(final int x, final int y) {
		return polygon.contains(x, y);
	}

	/**
	 * @return an array of all the contained tiles in this area.
	 */
	public Position[] getArray() {
		if (tileArrayCache == null) {
			final Rectangle bounds = getBounds();
			final ArrayList<Position> positions = new ArrayList<Position>(
					bounds.width * bounds.height);
			final int xMax = bounds.x + bounds.width, yMax = bounds.y
					+ bounds.height;
			for (int x = bounds.x; x < xMax; x++) {
				for (int y = bounds.y; y < yMax; y++) {
					if (contains(x, y)) {
						positions.add(new Position(x, y, plane));
					}
				}
			}
			tileArrayCache = positions.toArray(new Position[positions.size()]);
		}
		return tileArrayCache;
	}

	private int[] retrieveXPoints(Position[] positions) {
		int[] xPoints = new int[positions.length];
		for (int i = 0; i < positions.length; i++)
			xPoints[i] = positions[i].getX();
		return xPoints;
	}

	private int[] retrieveYPoints(Position[] positions) {
		int[] yPoints = new int[positions.length];
		for (int i = 0; i < positions.length; i++)
			yPoints[i] = positions[i].getY();
		return yPoints;
	}

}