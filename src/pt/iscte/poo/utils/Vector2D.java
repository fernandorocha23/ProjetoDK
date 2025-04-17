package pt.iscte.poo.utils;

import java.io.Serializable;

//Added 27-Feb-2018, Last Changed 25-Set-2018
public class Vector2D implements Serializable {

	/**
	 * 
	 */
	// final added oct-2024
	private static final long serialVersionUID = -1270671562883374927L;
	private final int x;  
	private final int y;

	public Vector2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public static Vector2D movementVector(Point2D from, Point2D to) {
		Vector2D d = new Vector2D(to.getX() - from.getX(), to.getY() - from.getY());
		if (Math.abs(d.getX()) > Math.abs(d.getY())) {
			d = new Vector2D((int) Math.signum(d.getX()), 0);
		} else if (Math.abs(d.getX()) <= Math.abs(d.getY())) {
			d = new Vector2D(0, (int) Math.signum(d.getY()));
		}
		return d;
	}

	public Vector2D plus(Vector2D v) {
		return new Vector2D(getX() + v.getX(), getY() + v.getY());
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
		Vector2D other = (Vector2D) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	
}
