import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;

public class MapDrawer {
	public static void drawObjects(Graphics2D g2d, ArrayList<Shape> objects,Color lineColor,int lineWidth){
		BasicStroke stroke = new BasicStroke(lineWidth);
		g2d.setColor(lineColor);
		for(Shape s:objects){
			g2d.draw(s);
		}
		MapWindow.dialog.repaint();
	}
}
