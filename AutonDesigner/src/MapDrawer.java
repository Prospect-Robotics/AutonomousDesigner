import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class MapDrawer {
	public static void drawObjects(Graphics2D g2d, ArrayList<Shape> objects, Color lineColor,Color background, int lineWidth) 
	{
		BasicStroke stroke = new BasicStroke(lineWidth);
		g2d.setColor(lineColor);
		for (Shape s : objects) {
			g2d.draw(s);
		}
		MapWindow.dialog.repaint();
	}

	public static void drawRobot(Graphics2D g2d, FakeBot robo, Color lineColor, int lineWidth) {
		Point.Double center = robo.position;
		double w = robo.width;
		double l = robo.length;
		Point.Double tl = new Point.Double(center.getX()-(w/2),center.getY()+(l/2));
		Point.Double tr = new Point.Double(center.getX()+(w/2),center.getY()+(l/2));
		Point.Double bl = new Point.Double(center.getX()-(w/2),center.getY()-(l/2));
		Point.Double br = new Point.Double(center.getX()+(w/2),center.getY()-(l/2));
		ArrayList<Point.Double> points = new ArrayList<Point.Double>();
		points.add(tl);
		points.add(tr);
		points.add(br);
		points.add(bl);
		for(Point.Double p:points){
			PointMath.rotate(p, center, robo.angle);
		}
		Path2D.Double path = new Path2D.Double();
		path.moveTo(tl.getX(),tl.getY());
		for(Point.Double p:new Point.Double[] {tr,br,bl}){
			path.lineTo(p.getX(),p.getY());
		}
		path.closePath();
		g2d.setStroke(new BasicStroke(lineWidth));
		g2d.setColor(lineColor);
		g2d.draw(path);
		MapWindow.dialog.repaint();
	}
}
