

import java.awt.Point;

class PointMath {
	static final Point.Double ORIGIN = new Point.Double(0, 0);

	static void rotate(Point.Double point, Point.Double center, double angle) {
		double x1 = point.getX() - center.getX();
		double y1 = point.getY() - center.getY();
		double x2 = x1 * Math.cos(Math.toRadians(angle)) - y1 * Math.sin(Math.toRadians(angle));
		double y2 = x1 * Math.sin(Math.toRadians(angle)) + y1 * Math.cos(Math.toRadians(angle));
		x2 += center.getX();
		y2 += center.getY();
		point.setLocation(x2, y2);
	}

	static void move(Point.Double point, double x, double y) {
		point.setLocation(point.getX() + x, point.getY() + y);
	}

	static void moveAlong(Point.Double point, double distance, double degrees) {
		double x = point.getX() + (Math.sin(Math.toRadians(degrees)) * distance);
		double y = point.getY() - (Math.cos(Math.toRadians(degrees)) * distance);
		point.setLocation(x, y);
	}	
}
