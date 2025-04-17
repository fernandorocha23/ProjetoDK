package pt.iscte.poo.utils;

import java.awt.Rectangle;

public class Ops2D {

	public static int area(Rectangle r) {
		if (r.width <= 0)
			return 0;
		if (r.height <= 0)
			return 0;
		return r.width * r.height;
	}

}
