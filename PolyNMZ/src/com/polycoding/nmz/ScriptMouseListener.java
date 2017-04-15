package com.polycoding.nmz;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ScriptMouseListener implements MouseMotionListener, MouseListener {

	private PolyNMZ s;

	public ScriptMouseListener(PolyNMZ s) {
		this.s = s;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		s.lastMouseMove = System.currentTimeMillis();
		if (s.hidePaintRect.contains(e.getX(), e.getY()))
			s.paintBackgroundColor = new Color(0, 0, 0, 100);
		else
			s.paintBackgroundColor = new Color(0, 0, 0, 200);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (s.hidePaintRect.contains(e.getX(), e.getY())) {
			s.showPaint = !s.showPaint;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
