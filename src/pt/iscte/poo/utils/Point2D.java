package pt.iscte.poo.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Updated 27-Feb-2018
// Updated 28-Nov-2021

public class Point2D implements Serializable { // Added 23-Apr-2018

	/**
	 * 
	 */
	private static final long serialVersionUID = -8606069537456557526L;
	private final int x;
	private final int y;

	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(Point point) {
		x = point.x;
		y = point.y;
	}

	public Point2D(java.awt.geom.Point2D point) {
		x = (int)point.getX();
		y = (int)point.getY();
	}

    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	} 

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2D other = (Point2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public Point2D plus(Vector2D v) {
		return new Point2D(x + v.getX(), y + v.getY());
	}

	public List<Point2D> getNeighbourhoodPoints() {
		
		List<Point2D> neighbours = new ArrayList<>();
		
		for (Direction d : Direction.values()) {
			Vector2D v = d.asVector();
			neighbours.add(this.plus(v));
		}
		
		return neighbours;
	}

	public static Point2D readFrom(Scanner in) {
		String s = in.next();
		int x = Integer.valueOf((s.substring(1, s.length() - 1)));
		s = in.next();
		int y = Integer.valueOf((s.substring(0, s.length() - 1)));
		return new Point2D(x, y);
	}

	public String writeTo(PrintWriter out) {		
		return this.toString();
	}
	
	public List<Point2D> getFrontRect(Direction d, int rect_width, int rect_height) {
		
		List<Vector2D> frontRectVecs = new ArrayList<>();
		
		int a = rect_width/2;	
		Vector2D basis = d.asVector();
		
		for (int dx=-a; dx<=a; dx++)
			for (int dy=0; dy<rect_height; dy++)
				if (basis.getX() != 0) // horizontal direction (swap)
					frontRectVecs.add(new Vector2D(basis.getX()*dy, dx));
				else
					frontRectVecs.add(new Vector2D(dx, basis.getY()*dy));
		
		List<Point2D> frontRectPoints = new ArrayList<>();
		frontRectVecs.forEach( v -> frontRectPoints.add((this).plus(v)));
		
		return frontRectPoints;
	}
	
	// added 28-Nov-2021
	
	public List<Point2D> getWideNeighbourhoodPoints() {
		
		List<Point2D> neighbours = new ArrayList<>();
		
		for (int dx=-1; dx<=1; dx++)
			for (int dy=-1; dy<=1; dy++)
				if (dx!=0 || dy!=0) {
					Vector2D v = new Vector2D(dx, dy);
					neighbours.add(this.plus(v));
				}
		return neighbours;
	}
	
	public Vector2D vectorTo(Point2D p) {
		
		int dx = p.getX() - x;
		int dy = p.getY() - y;
				
		if (Math.abs(dx) > Math.abs(dy))
			return new Vector2D(Integer.signum(dx), 0);
		
		return new Vector2D(0, Integer.signum(dy));	
	}
	
	public Direction directionTo(Point2D p) {		
		return Direction.forVector(vectorTo(p));
	}

	public int distanceTo(Point2D p) {
		return Math.abs(p.getX() - x) + Math.abs(p.getY() - y);
	}

	public Point2D plus(Dimension d) {
		return new Point2D(x + d.width, y + d.height);
	}
}
