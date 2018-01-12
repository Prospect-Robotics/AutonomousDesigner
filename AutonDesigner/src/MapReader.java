import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MapReader {
	
	public static ArrayList<Shape> fromfile(File file) {
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		try {
			Scanner in = new Scanner(file);
			while(in.hasNext()){
				ArrayList<Point.Double> pointslist = new ArrayList<Point.Double>();
				String line = in.nextLine();
				String[] points = line.split(" ");
				for(String str:points){
					String[] xy = str.split(",");
					Point.Double point = new Point.Double(Double.valueOf(xy[0]),Double.valueOf(xy[1]));
					pointslist.add(point);
				}
				Path2D.Double path = new Path2D.Double();
				path.moveTo(pointslist.get(0).getX(),pointslist.get(0).getY());
				for(int i=1;i<pointslist.size();i++){
					path.lineTo(pointslist.get(i).getX(),pointslist.get(i).getY());
				}
				path.closePath();
				shapes.add(path);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("FILE NOT FOUND");
		}
		return shapes;
	}
}
